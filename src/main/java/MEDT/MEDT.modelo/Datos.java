package MEDT.MEDT.modelo;

import MEDT.MEDT.modelo.excepciones.ArticuloNoEncontradoException;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Datos {
    // Usamos colecciones genéricas
    private Map<String, Articulo> articulos = new HashMap<>();
    private Map<String, Cliente> clientes = new HashMap<>();
    private Map<Integer, Pedido> pedidos = new HashMap<>();

    // ---- Gestión de artículos ----
    public void addArticulo(Articulo articulo) {
        articulos.put(articulo.getCodigo(), articulo);
    }

    public Articulo getArticulo(String codigo) throws ArticuloNoEncontradoException {
        Articulo a = articulos.get(codigo);
        if (a == null) {
            throw new ArticuloNoEncontradoException("Artículo con código " + codigo + " no existe.");
        }
        return a;
    }

    public Collection<Articulo> getArticulos() {
        return articulos.values();
    }

    // ---- Gestión de clientes ----
    public void addCliente(Cliente cliente) {
        clientes.put(cliente.getNif(), cliente);
    }

    public Cliente getCliente(String nif) {
        return clientes.get(nif);
    }

    public Collection<Cliente> getClientes() {
        return clientes.values();
    }

    public Collection<ClienteEstandar> getClientesEstandar() {
        return clientes.values()
                .stream()
                .filter(cliente -> cliente instanceof ClienteEstandar)
                .map(cliente -> (ClienteEstandar)cliente)
                .collect(Collectors.toList());
    }

    public List<ClientePremium> getClientesPremium() {
        return clientes.values()
                .stream()
                .filter(cliente -> cliente instanceof ClientePremium)
                .map(cliente -> (ClientePremium)cliente)
                .collect(Collectors.toList());
    }

    // ---- Gestión de pedidos ----
    public void addPedido(Pedido pedido) {
        pedidos.put(pedido.getNumPedido(), pedido);
    }

//    public List<Pedido> getPedidos() {
//        return pedidos;
//    }

    // Cancelar pedido con excepción personalizada
    public void cancelarPedido(Pedido pedido) throws PedidoNoCancelableException {
        if (!pedido.esCancelable()) {
            throw new PedidoNoCancelableException("El pedido no puede cancelarse, ya pasó el tiempo de preparación.");
        }
        pedidos.remove(pedido);
    }
    // Eva
    public boolean buscarArticulo(String codigo_articulo){
        return articulos.containsKey(codigo_articulo);
    }
    public boolean buscarCliente(String nif){
        return clientes.containsKey(nif);
    }
    public void addClienteEstandar(String nombre, String domicilio, String nif, String email){
        clientes.put(nif, new ClienteEstandar(nombre, domicilio, nif, email));
    }
    public void addClientePremium(String nombre, String domicilio, String nif, String email) {
        clientes.put(nif, new ClientePremium(nombre, domicilio, nif, email));
    }
    public boolean buscarPedido(int numero_pedido){
        return pedidos.containsKey(numero_pedido);
    }
    public boolean pedidoEliminable(int numero_pedido){
        Pedido pedido = pedidos.get(numero_pedido);
            if(pedido == null){
                return false;
            }
                long diasTranscurridos = Duration.between(pedido.getFechaHora(), LocalDateTime.now()).toDays();
                int tiempoPreparacion = pedido.getArticulo().getTiempoPrep();

                return diasTranscurridos <= tiempoPreparacion;
    }
    public boolean eliminarPedido(int numero_pedido){
        if(pedidos.containsKey(numero_pedido)){
            pedidos.remove(numero_pedido);
            return true;
        }
        return false;
    }
    public List<Pedido> getPedidosPendientesCliente(String nif){
        List<Pedido> pendientes = new ArrayList<>();
        for (Pedido pedido : pedidos.values()){
            long dias = Duration.between(pedido.getFechaHora(), LocalDateTime.now()).toDays();
            int preparacion = pedido.getArticulo().getTiempoPrep();

            if(dias <= preparacion && pedido.getCliente().getNif().equals(nif)){
                pendientes.add(pedido);
            }
        }
        return pendientes;
    }
    public List<Pedido> getPedidosPendientes(){
        List<Pedido> pendientes = new ArrayList<>();
        for (Pedido pedido : pedidos.values()){
            long dias = Duration.between(pedido.getFechaHora(), LocalDateTime.now()).toDays();
            int preparacion = pedido.getArticulo().getTiempoPrep();

            if(dias <= preparacion){
                pendientes.add(pedido);
            }
        }
        return pendientes;
    }
    public List<Pedido> getPedidosEnviadosCliente(String nif){
        List<Pedido> enviados = new ArrayList<>();
        for (Pedido pedido : pedidos.values()){
            long dias = Duration.between(pedido.getFechaHora(), LocalDateTime.now()).toDays();
            int preparacion = pedido.getArticulo().getTiempoPrep();

            if(dias > preparacion && pedido.getCliente().getNif().equals(nif)){
                enviados.add(pedido);
            }
        }
        return enviados;
    }
    public List<Pedido> getPedidosEnviados(){
        List<Pedido> enviados = new ArrayList<>();
        for (Pedido pedido : pedidos.values()){
            long dias = Duration.between(pedido.getFechaHora(), LocalDateTime.now()).toDays();
            int preparacion = pedido.getArticulo().getTiempoPrep();

            if(dias > preparacion){
                enviados.add(pedido);
            }
        }
        return enviados;
    }
}
