package MEDT.MEDT.controlador;

import MEDT.MEDT.modelo.*;
import MEDT.MEDT.modelo.excepciones.ArticuloNoEncontradoException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class Controlador {

    private final Datos datos;

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


    public Collection<Cliente> getClientes() {
        return datos.getClientes();
    }

    public Collection<ClienteEstandar> getClientesEstandar() {
        return datos.getClientesEstandar();
    }

    public Collection<ClientePremium> getClientesPremium() {
        return datos.getClientesPremium();
    }

    // Gestión Pedidos Eva
    public boolean buscarArticulo(String codigo_articulo){
        return datos.buscarArticulo(codigo_articulo);
    }
    public Articulo getArticulo(String codigo_articulo) throws ArticuloNoEncontradoException {
        return datos.getArticulo(codigo_articulo);
    }
    public boolean buscarCliente(String nif){
        return datos.buscarCliente(nif);
    }
    public Cliente getCliente(String nif){
        return datos.getCliente(nif);
    }
    public void addPedido(int numPedido, int cantidad, LocalDateTime fechaHora, Articulo articulo, Cliente cliente){
        Pedido nuevo = new Pedido(numPedido, cantidad, fechaHora, articulo, cliente);
        datos.addPedido(nuevo);
    }
    public void addClienteEstandar(String nombre, String domicilio, String nif, String email) {
        datos.addCliente(new ClienteEstandar(nombre, domicilio, nif, email));
    }
    public void addClientePremium(String nombre, String domicilio, String nif, String email) {
        datos.addCliente(new ClientePremium(nombre, domicilio, nif, email));
    }
    public boolean buscarPedido(int numero_pedido){
        return datos.buscarPedido(numero_pedido);
    }
    public boolean pedidoEliminable(int numero_pedido) {
        return datos.pedidoEliminable(numero_pedido);
    }
    public boolean eliminarPedido(int numero_pedido){
        return datos.eliminarPedido(numero_pedido);
    }
    public List<Pedido> getPedidosPendientesCliente(String nif){
        return datos.getPedidosPendientesCliente(nif);
    }
    public List<Pedido> getPedidosPendientes(){
        return datos.getPedidosPendientes();
    }
    public List<Pedido> getPedidosEnviadosCliente(String nif){
        return datos.getPedidosEnviadosCliente(nif);
    }
    public List<Pedido> getPedidosEnviados(){
        return datos.getPedidosEnviados();
    }
}

