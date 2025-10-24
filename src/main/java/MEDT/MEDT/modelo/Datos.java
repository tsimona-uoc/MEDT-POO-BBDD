package MEDT.MEDT.modelo;

import MEDT.MEDT.modelo.excepciones.ArticuloNoEncontradoException;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Datos {

    private final Map<String, Articulo> articulos = new HashMap<>();
    private final Map<String, Cliente> clientes = new HashMap<>();
    private final Map<Integer, Pedido> pedidos = new HashMap<>();

    // ============================================================
    // GESTIÓN DE ARTÍCULOS
    // ============================================================

    public boolean addArticulo(Articulo articulo) {
        if (articulo == null || articulos.containsKey(articulo.getCodigo())) {
            return false; // Evita duplicados
        }
        articulos.put(articulo.getCodigo(), articulo);
        return true;
    }

    public Articulo getArticulo(String codigo) throws ArticuloNoEncontradoException {
        Articulo art = articulos.get(codigo);
        if (art == null) {
            throw new ArticuloNoEncontradoException("El artículo con código " + codigo + " no existe.");
        }
        return art;
    }

    public Collection<Articulo> getArticulos() {
        return articulos.values();
    }

    // ============================================================
    // GESTIÓN DE CLIENTES
    // ============================================================

    public boolean addCliente(Cliente cliente) {
        if (cliente == null || clientes.containsKey(cliente.getNif())) {
            return false; // Evita duplicados
        }
        clientes.put(cliente.getNif(), cliente);
        return true;
    }

    public Cliente getCliente(String nif) {
        return clientes.get(nif);
    }

    public Collection<Cliente> getClientes() {
        return clientes.values();
    }

    public List<Cliente> getClientesEstandar() {
        return clientes.values().stream()
                .filter(c -> c instanceof ClienteEstandar)
                .collect(Collectors.toList());
    }

    public List<Cliente> getClientesPremium() {
        return clientes.values().stream()
                .filter(c -> c instanceof ClientePremium)
                .collect(Collectors.toList());
    }

    // ============================================================
    // GESTIÓN DE PEDIDOS
    // ============================================================

    public boolean addPedido(Pedido pedido) {
        if (pedido == null || pedidos.containsKey(pedido.getNumPedido())) {
            return false;
        }
        pedidos.put(pedido.getNumPedido(), pedido);
        return true;
    }

    public Pedido getPedido(int numPedido) {
        return pedidos.get(numPedido);
    }

    public boolean eliminarPedido(int numPedido) {
        if (pedidos.containsKey(numPedido)) {
            pedidos.remove(numPedido);
            return true;
        }
        return false;
    }

    public void cancelarPedido(Pedido pedido) throws PedidoNoCancelableException {
        if (!pedido.esCancelable()) {
            throw new PedidoNoCancelableException(
                    "El pedido no puede cancelarse, ya pasó el tiempo de preparación."
            );
        }
        pedidos.remove(pedido.getNumPedido());
    }


    // ============================================================
    // CONSULTAS DE PEDIDOS (pendientes / enviados)
    // ============================================================

    public List<Pedido> getPedidosPendientes() {
        return pedidos.values().stream()
                .filter(p -> Duration.between(p.getFechaHora(), LocalDateTime.now()).toMinutes()
                        <= p.getArticulo().getTiempoPrep())
                .collect(Collectors.toList());
    }

    public List<Pedido> getPedidosPendientesCliente(String nif) {
        return getPedidosPendientes().stream()
                .filter(p -> p.getCliente().getNif().equalsIgnoreCase(nif))
                .collect(Collectors.toList());
    }

    public List<Pedido> getPedidosEnviados() {
        return pedidos.values().stream()
                .filter(p -> Duration.between(p.getFechaHora(), LocalDateTime.now()).toMinutes()
                        > p.getArticulo().getTiempoPrep())
                .collect(Collectors.toList());
    }

    public List<Pedido> getPedidosEnviadosCliente(String nif) {
        return getPedidosEnviados().stream()
                .filter(p -> p.getCliente().getNif().equalsIgnoreCase(nif))
                .collect(Collectors.toList());
    }
}
