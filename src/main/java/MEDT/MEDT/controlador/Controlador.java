package MEDT.MEDT.controlador;

import MEDT.MEDT.modelo.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

public class Controlador {

    private final Datos datos;
    //private Arrays clientes;

    public Controlador() {
        this.datos = new Datos();
    }

    // Gestión Artículos
    public void addArticulo(String codigo, String descripcion, double precio, double gastosEnvio, int tiempoPrep) {
        Articulo articulo = new Articulo(codigo, descripcion, precio, gastosEnvio, tiempoPrep);
        datos.addArticulo(articulo);
    }

    public Collection<Articulo> getArticulos() {
        return datos.getArticulos();
    }

    // Gestión Clientes
    public void addClienteEstandar(String nombre, String domicilio, String nif, String email) {
        datos.addCliente(new ClienteEstandar(nombre, domicilio, nif, email));
    }

    public void addClientePremium(String nombre, String domicilio, String nif, String email) {
        datos.addCliente(new ClientePremium(nombre, domicilio, nif, email));
    }

    public Collection<Cliente> getClientes() {
        return datos.getClientes();
    }

    public Collection<Cliente> getClientesEstandar() {
        return datos.getClientes().stream()
                .filter(c -> c instanceof ClienteEstandar)
                .collect(java.util.stream.Collectors.toList());
    }

    public Collection<Cliente> getClientesPremium() {
        return datos.getClientes().stream()
                .filter(c -> c instanceof ClientePremium)
                .collect(java.util.stream.Collectors.toList());
    }

    // Gestión Pedidos
    public void addPedido(String emailCliente, String codigoArticulo, int cantidad) throws IllegalArgumentException {
        Articulo articulo = datos.getArticuloByCodigo(codigoArticulo);
        if (articulo == null) {
            throw new IllegalArgumentException("El articulo con codigo " + codigoArticulo + " no existe.");
        }

        Cliente cliente = datos.getClienteByMail(emailCliente);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no existe. Debe ser creado en primer lugar.");
        }

        Pedido pedido = new Pedido(datos.generarNumeroPedido(), LocalDateTime.now(), cantidad, articulo, cliente);
        datos.addPedido(pedido);
    }

    public boolean eliminarPedido(int numeroPedido) {
        Pedido pedido = datos.getPedidoByNumero(numeroPedido);
        if (pedido != null && !pedido.pedidoEnviado(LocalDateTime.now())) {
            return datos.removePedido(pedido);
        }
        return false;
    }

    public Collection<Pedido> getPedidosPendientes(String emailCliente) {
        return datos.getPedidos().stream()
                .filter(p -> !p.pedidoEnviado(LocalDateTime.now()))
                .filter(p -> emailCliente == null || emailCliente.isEmpty() || p.getCliente().getEmail().equalsIgnoreCase(emailCliente))
                .collect(Collectors.toList());
    }

    public Collection<Pedido> getPedidosEnviados(String emailCliente) {
        return datos.getPedidos().stream()
                .filter(p -> p.pedidoEnviado(LocalDateTime.now()))
                .filter(p -> emailCliente == null || emailCliente.isEmpty() || p.getCliente().getEmail().equalsIgnoreCase(emailCliente))
                .collect(Collectors.toList());
    }

}

