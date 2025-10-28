package MEDT.MEDT.controlador;

import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.Datos;
import MEDT.MEDT.modelo.Pedido;
import MEDT.MEDT.modelo.excepciones.ArticuloNoEncontradoException;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ControladorPedidos {

    private Datos datos;

    public ControladorPedidos(Datos datos){
        this.datos = datos;
    }

    // =======================
    //  PEDIDOS
    // =======================
    public String addPedido(int numPedido, int cantidad, LocalDateTime fechaHora, String codigoArticulo, String nifCliente) {
        try {
            Articulo articulo = datos.getArticulo(codigoArticulo);
            Cliente cliente = datos.getCliente(nifCliente);

            if (articulo == null)
                return "Error: el artículo no existe.";
            if (cliente == null)
                return "Error: el cliente no existe. Debe crearlo antes de continuar.";

            Pedido pedido = new Pedido(numPedido, cantidad, fechaHora, articulo, cliente);
            if (datos.addPedido(pedido)) {
                return "Pedido añadido correctamente.";
            } else {
                return "Error: el pedido no se pudo añadir (posible duplicado).";
            }
        } catch (ArticuloNoEncontradoException e) {
            return "Error: Artículo no encontrado." + e.getMessage();
        } catch (Exception e) {
            return "Error inesperado al añadir pedido: " + e.getMessage();
        }
    }

    public boolean eliminarPedido(int numPedido) throws PedidoNoCancelableException {
        Pedido pedido = datos.getPedido(numPedido);
        if (pedido == null) {
            throw new IllegalArgumentException("No existe ningún pedido con ese número.");
        }
        datos.cancelarPedido(pedido); // si no se puede cancelar, lanza la excepción
        return false;
    }


    public List<Pedido> getPedidosPendientes(String nif) {
        if (Objects.equals(nif, "")){
            return datos.getPedidosPendientes();
        }

        return datos.getPedidosPendientes().stream().filter(x -> x.getCliente().getNif().equals(nif)).toList();
    }

    public List<Pedido> getPedidosEnviados(String nif) {
        if (Objects.equals(nif, "")){
            return datos.getPedidosEnviados();
        }
        return datos.getPedidosEnviados().stream().filter(x -> x.getCliente().getNif().equals(nif)).toList();
    }
}
