package MEDT.MEDT.controlador;

import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.ClientePremium;
import MEDT.MEDT.modelo.Datos;
import MEDT.MEDT.modelo.Pedido;
import MEDT.MEDT.modelo.excepciones.ArticuloNoEncontradoException;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Controlador {

    /// Datos
    private Datos datos;

    public Controlador(Datos datos) {
        this.datos = datos;
    }

    // =======================
    //  ARTÍCULOS
    // =======================
    public boolean addArticulo(String codigo, String descripcion, double precio, double gastosEnvio, int tiempoPrep) {
        try {
            Articulo articulo = new Articulo(codigo, descripcion, precio, gastosEnvio, tiempoPrep);
            return datos.addArticulo(articulo);
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getArticulosStr() {
        List<String> listaStr = new ArrayList<>();
        for (Articulo art : datos.getArticulos()) {
            listaStr.add(art.toString());
        }
        return listaStr;
    }

    // =======================
    //  CLIENTES
    // =======================
    public boolean addClienteEstandar(String nombre, String domicilio, String nif, String email) {
        ClienteEstandar cliente = new ClienteEstandar(nombre, domicilio, nif, email);
        return datos.addCliente(cliente);
    }

    public boolean addClientePremium(String nombre, String domicilio, String nif, String email) {
        ClientePremium cliente = new ClientePremium(nombre, domicilio, nif, email);
        return datos.addCliente(cliente);
    }

    public List<String> getClientesStr() {
        List<String> listaStr = new ArrayList<>();
        for (Cliente c : datos.getClientes()) {
            listaStr.add(c.toString());
        }
        return listaStr;
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


    public List<String> getPedidosPendientesStr() {
        List<String> listaStr = new ArrayList<>();
        for (Pedido p : datos.getPedidosPendientes()) {
            listaStr.add(p.toString());
        }
        return listaStr;
    }

    public List<String> getPedidosEnviadosStr() {
        List<String> listaStr = new ArrayList<>();
        for (Pedido p : datos.getPedidosEnviados()) {
            listaStr.add(p.toString());
        }
        return listaStr;
    }
}
