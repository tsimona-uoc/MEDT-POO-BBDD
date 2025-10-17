package MEDT.MEDT.modelo;

import java.time.LocalDateTime;
import java.util.*;
import MEDT.MEDT.modelo.excepciones.*;

public class Datos {
    // Usamos colecciones genéricas
    private Map<String, Articulo> articulos = new HashMap<>();
    private Map<String, Cliente> clientes = new HashMap<>();
    private List<Pedido> pedidos = new ArrayList<>();

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

    public List<ClienteEstandar> getClientesEstandar() {
        return List.of();
    }

    public List<ClientePremium> getClientesPremium() {
        return List.of();
    }

    // ---- Gestión de pedidos ----
    public void addPedido(Pedido pedido) {
        pedidos.add(pedido);
        pedido.getCliente().addPedido(pedido);
    }


    public List<Pedido> getPedidos() {
        return pedidos;
    }

    // Cancelar pedido con excepción personalizada
    public void cancelarPedido(Pedido pedido) throws PedidoNoCancelableException {
        if (!pedido.esCancelable()) {
            throw new PedidoNoCancelableException("El pedido no puede cancelarse, ya pasó el tiempo de preparación.");
        }
        pedidos.remove(pedido);
    }


}
