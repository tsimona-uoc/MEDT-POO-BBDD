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

    public ControladorPedidos(){
        this.pedidoDAO = new PedidoJDBC();
        this.articuloDAO = new ArticuloJDBC();
        this.clienteDAO = new ClienteJDBC();
    }

    // =======================
    //  PEDIDOS
    // =======================
    public String addPedido(int numPedido, int cantidad, LocalDateTime fechaHora, String codigoArticulo, String nifCliente) {
        try {
            Articulo articulo = articuloDAO.getArticulo(codigoArticulo);
            Cliente cliente = clienteDAO.getCliente(nifCliente);

            if (articulo == null)
                return "Error: el artículo no existe.";
            if (cliente == null)
                return "Error: el cliente no existe. Debe crearlo antes de continuar.";

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

    public boolean eliminarPedido(int numPedido) throws PedidoNoCancelableException {
        Pedido pedido = pedidoDAO.getPedido(numPedido);
        if (pedido == null) {
            throw new IllegalArgumentException("No existe ningún pedido con ese número.");
        }
        pedidoDAO.cancelarPedido(pedido); // si no se puede cancelar, lanza la excepción
        return false;
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
