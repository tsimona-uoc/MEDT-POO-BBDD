package MEDT.MEDT.controlador;

import MEDT.MEDT.DAO.IArticuloDAO;
import MEDT.MEDT.DAO.IClienteDAO;
import MEDT.MEDT.DAO.IPedidoDAO;
import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.Pedido;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ControladorPedidos {

    /// Articulo DAO
    private IArticuloDAO articuloDAO;

    /// Pedido DAO
    private IPedidoDAO pedidoDAO;

    /// Cliente DAO
    private IClienteDAO clienteDAO;

    public ControladorPedidos(IArticuloDAO articuloDAO, IPedidoDAO pedidoDAO, IClienteDAO clienteDAO) {
        this.articuloDAO = articuloDAO;
        this.pedidoDAO = pedidoDAO;
        this.clienteDAO = clienteDAO;
    }

    public String addPedido(int numPedido, int cantidad, LocalDateTime fechaHora, String codigoArticulo, String nifCliente) {
        try {
            Articulo articulo = this.articuloDAO.findByCodigo(codigoArticulo);
            Cliente cliente = this.clienteDAO.findByNIF(nifCliente);

            if (articulo == null)
                return "Error: el artículo no existe.";
            if (cliente == null)
                return "Error: el cliente no existe. Debe crearlo antes de continuar.";

            Pedido pedido = new Pedido(numPedido, cantidad, fechaHora, articulo, cliente);

            try {
                this.pedidoDAO.insert(pedido);
                return "Pedido añadido correctamente.";
            }
            catch (SQLException ex){
                return "Error: el pedido no se pudo añadir (posible duplicado).";
            }
        } catch (Exception e) {
            return "Error inesperado al añadir pedido: " + e.getMessage();
        }
    }

    public boolean eliminarPedido(int numPedido) throws PedidoNoCancelableException {

        try {
            Pedido pedido = this.pedidoDAO.findByCode(numPedido);
            if (pedido == null){
                throw new IllegalArgumentException("No existe ningún pedido con ese número.");
            }

            if (!pedido.esCancelable()){
                throw new PedidoNoCancelableException("No se puede eliminar el pedido.");
            }

            this.pedidoDAO.delete(numPedido);

            return true;

        }
        catch (SQLException ex){
            System.out.println("Error al eliminar el pedido: " + ex.getMessage());
        }

        return false;
    }


    public List<Pedido> getPedidosPendientes(String nif) {
        /// If no NIF is given, return all pending orders
        if (Objects.equals(nif, "")){
            try {
                return this.pedidoDAO.findAll()
                        .stream()
                        .filter(p -> Duration.between(p.getFechaHora(), LocalDateTime.now()).toMinutes() <= p.getArticulo().getTiempoPrep())
                        .toList();
            } catch (SQLException e) {
                return null;
            }
        }

        Cliente cliente = null;

        try {
            cliente = this.clienteDAO.findByNIF(nif);
        } catch (Exception e) {
            return null;
        }

        if (cliente == null){
            throw new RuntimeException("Cliente no encontrado");
        }

        try {
            return this.pedidoDAO.findByCliente(cliente)
                    .stream()
                    .filter(p -> Duration.between(p.getFechaHora(), LocalDateTime.now()).toMinutes() <= p.getArticulo().getTiempoPrep())
                    .toList();
        } catch (SQLException e) {
            return null;
        }
    }

    public List<Pedido> getPedidosEnviados(String nif) {
        /// If no NIF is given, return all pending orders
        if (Objects.equals(nif, "")){
            try {
                return this.pedidoDAO.findAll()
                        .stream()
                        .filter(p -> Duration.between(p.getFechaHora(), LocalDateTime.now()).toMinutes() > p.getArticulo().getTiempoPrep())
                        .toList();
            } catch (SQLException e) {
                return null;
            }
        }

        Cliente cliente = null;

        try {
            cliente = this.clienteDAO.findByNIF(nif);
        } catch (Exception e) {
            return null;
        }

        if (cliente == null){
            throw new RuntimeException("Cliente no encontrado");
        }

        try {
            return this.pedidoDAO.findByCliente(cliente)
                    .stream()
                    .filter(p -> Duration.between(p.getFechaHora(), LocalDateTime.now()).toMinutes() > p.getArticulo().getTiempoPrep())
                    .toList();
        } catch (SQLException e) {
            return null;
        }
    }
}
