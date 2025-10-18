package MEDT.MEDT.modelo;

import MEDT.MEDT.modelo.excepciones.ArticuloNoEncontradoException;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
