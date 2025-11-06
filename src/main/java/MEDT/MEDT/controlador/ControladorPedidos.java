package MEDT.MEDT.controlador;

import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.Pedido;
import MEDT.MEDT.modelo.excepciones.ArticuloNoEncontradoException;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;
import MEDT.MEDT.persistencia.DAO.PedidoDAO;
import MEDT.MEDT.persistencia.DAO.ClienteDAO;
import  MEDT.MEDT.persistencia.DAO.ArticuloDAO;
import MEDT.MEDT.persistencia.JDBC.PedidoJDBC;
import MEDT.MEDT.persistencia.JDBC.ArticuloJDBC;
import MEDT.MEDT.persistencia.JDBC.ClienteJDBC;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ControladorPedidos {

    private PedidoDAO pedidoDAO;
    private ArticuloDAO articuloDAO;
    private ClienteDAO clienteDAO;
    private ControladorClientes controladorClientes;

    public ControladorPedidos(){
        this.pedidoDAO = new PedidoJDBC();
        this.articuloDAO = new ArticuloJDBC();
        this.clienteDAO = new ClienteJDBC();
        this.controladorClientes = new ControladorClientes();
    }

    // =======================
    //  PEDIDOS
    // =======================
    public String addPedido(int numPedido, int cantidad, LocalDateTime fechaHora, String codigoArticulo, String nifCliente) {
        try {
            Articulo articulo = articuloDAO.getArticulo(codigoArticulo);

            if (articulo == null)
                return "Error: el artículo no existe.";

            Cliente cliente = clienteDAO.getCliente(nifCliente);

            if (cliente == null) {
                boolean resultado = clienteDAO.addCliente(cliente);

                if (!resultado) {
                    return "Error: no se pudo crear el cliente. " + resultado;
                }
                cliente = clienteDAO.getCliente(nifCliente);
            }

            Pedido pedido = new Pedido(numPedido, cantidad, fechaHora, articulo, cliente);
            if (pedidoDAO.addPedido(pedido)) {
                return "Pedido añadido correctamente.";
            } else {
                return "Error: el pedido no se pudo añadir (posible duplicado).";
            }
        } catch (Exception e) {
            return "Error inesperado al añadir pedido: " + e.getMessage();
        }
    }

    public String eliminarPedido(int numPedido) {
        try {
            if (pedidoDAO.eliminarPedido(numPedido)) {
                return "Pedido eliminado correctamente.";
            } else {
                return "No existe ningún pedido con ese número.";
            }
        } catch (PedidoNoCancelableException e) {
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            return "Error inesperado al eliminar pedido: " + e.getMessage();
        }
    }

    public List<Pedido> getPedido() {
        return pedidoDAO.getPedido();
    }



    public List<Pedido> getPedidosPendientes(String nif) {
        if (Objects.equals(nif, "")){
            return pedidoDAO.getPedidosPendientes(nif);
        }

        return pedidoDAO.getPedidosPendientes(nif).stream().filter(x -> x.getCliente().getNif().equals(nif)).toList();
    }

    public List<Pedido> getPedidosEnviados(String nif) {
        if (Objects.equals(nif, "")){
            return pedidoDAO.getPedidosEnviados(nif);
        }
        return pedidoDAO.getPedidosEnviados(nif).stream().filter(x -> x.getCliente().getNif().equals(nif)).toList();
    }
}
