package MEDT.MEDT.controlador;

import MEDT.MEDT.DAO.IArticuloDAO;
import MEDT.MEDT.DAO.IClienteDAO;
import MEDT.MEDT.DAO.IPedidoDAO;
import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.Pedido;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ControladorPedidos {
    private IPedidoDAO pedidoDAO;
    private IClienteDAO clienteDAO;
    private IArticuloDAO articuloDAO;

    public ControladorPedidos(IPedidoDAO pedidoDAO, IClienteDAO clienteDAO, IArticuloDAO articuloDAO) {
        this.pedidoDAO = pedidoDAO;
        this.clienteDAO = clienteDAO;
        this.articuloDAO = articuloDAO;
    }
    public String addPedido(int numPedido, int cantidad, LocalDateTime fechaHora, String codigoArticulo, String nifCliente) {
        try {
            // Verificar artículo
            Articulo articulo = articuloDAO.findAll().stream()
                    .filter(a -> a.getCodigo().equals(codigoArticulo))
                    .findFirst()
                    .orElse(null);

            if (articulo == null)
                return "Error: el artículo no existe.";

            // Verificar cliente
            Cliente cliente = clienteDAO.findByNif(nifCliente);
            if (cliente == null)
                return "Error: el cliente no existe.";

            // Crear pedido
            Pedido pedido = new Pedido(numPedido, cantidad, fechaHora, articulo, cliente);

            // Insertar en BD
            pedidoDAO.insert(pedido);

            return "Pedido añadido correctamente.";

        } catch (SQLException e) {
            return "Error SQL al añadir pedido: " + e.getMessage();
        } catch (Exception e) {
            return "Error inesperado: " + e.getMessage();
        }
    }
    public boolean eliminarPedido(int numPedido) throws PedidoNoCancelableException {
        try {
            Pedido pedido = pedidoDAO.findById(numPedido);

            if (pedido == null)
                throw new IllegalArgumentException("No existe ningún pedido con ese número.");

            // Regla: solo se puede eliminar si no ha sido enviado
            if (!pedido.esCancelable())
                throw new PedidoNoCancelableException("Ha pasado el tiempo de preparación del artículo.");

            pedidoDAO.delete(numPedido);
            return true;

        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al eliminar pedido.", e);
        }
    }
    public List<Pedido> getPedidosPendientes(String nifCliente) {
        try {
            List<Pedido> todos = pedidoDAO.findAll();

            return todos.stream()
                    .filter(Pedido::esCancelable) // esCancelable() = pendiente
                    .filter(p -> nifCliente.isEmpty() || p.getCliente().getNif().equals(nifCliente))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            return List.of();
        }
    }
    public List<Pedido> getPedidosEnviados(String nifCliente) {
        try {
            List<Pedido> todos = pedidoDAO.findAll();

            return todos.stream()
                    .filter(p -> !p.esCancelable()) // enviado
                    .filter(p -> nifCliente.isEmpty() || p.getCliente().getNif().equals(nifCliente))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            return List.of();
        }
    }
    public boolean existeCliente(String nif) {
        try {
            return clienteDAO.findByNif(nif) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
