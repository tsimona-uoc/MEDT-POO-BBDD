package MEDT.MEDT.controlador;

import MEDT.MEDT.modelo.*;
import MEDT.MEDT.modelo.excepciones.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public List<Articulo> getArticulos() {
        return datos.getArticulos().stream().toList();
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

    public List<Cliente> getClientes() {
        return datos.getClientes().stream().toList();
    }

    public List<Cliente> getClientesEstandar() {
        return datos.getClientesEstandar().stream().toList();
    }

    public List<Cliente> getClientesPremium() {
        return datos.getClientesPremium().stream().toList();
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

    public boolean existeCliente(String nif){
        try {
            return datos.getCliente(nif) != null;
        }
        catch (Exception e) {
            return false;
        }
    }
}
