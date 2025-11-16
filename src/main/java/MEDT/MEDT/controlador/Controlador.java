package MEDT.MEDT.controlador;

import MEDT.MEDT.modelo.*;
import MEDT.MEDT.modelo.excepciones.*;
import MEDT.MEDT.modelo.persistencia.DAOFactory;
import MEDT.MEDT.modelo.persistencia.dao.*;

import java.time.LocalDateTime;
import java.util.List;

public class Controlador {

    private final ArticuloDAO articuloDAO;
    private final ClienteDAO clienteDAO;
    private final PedidoDAO pedidoDAO;

    // Constructor vacío que llama a la Factory
    public Controlador() {
        this.articuloDAO = DAOFactory.getArticuloDAO();
        this.clienteDAO = DAOFactory.getClienteDAO();
        this.pedidoDAO = DAOFactory.getPedidoDAO();
    }

    // =======================
    //  ARTÍCULOS
    // =======================
    public boolean addArticulo(String codigo, String descripcion, double precio, double gastosEnvio, int tiempoPrep) {
        try {
            articuloDAO.obtenerPorCodigo(codigo);
            // Si llega aquí, es que existe
            return false;
        } catch (ArticuloNoEncontradoException e) {
            // Si no lo encuentra, perfecto, lo insertamos
            Articulo articulo = new Articulo(codigo, descripcion, precio, gastosEnvio, tiempoPrep);
            articuloDAO.insertar(articulo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Articulo> getArticulos() {
        return articuloDAO.obtenerTodos();
    }

    // =======================
    //  CLIENTES
    // =======================
    public boolean addClienteEstandar(String nombre, String domicilio, String nif, String email) {
        try {
            if (clienteDAO.obtenerPorNif(nif) != null || clienteDAO.obtenerPorEmail(email) != null) {
                return false; // Ya existe NIF o Email
            }
            ClienteEstandar cliente = new ClienteEstandar(nombre, domicilio, nif, email);
            clienteDAO.insertar(cliente);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addClientePremium(String nombre, String domicilio, String nif, String email) {
        try {
            if (clienteDAO.obtenerPorNif(nif) != null || clienteDAO.obtenerPorEmail(email) != null) {
                return false; // Ya existe NIF o Email
            }
            ClientePremium cliente = new ClientePremium(nombre, domicilio, nif, email);
            clienteDAO.insertar(cliente);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Cliente> getClientes() {
        return clienteDAO.obtenerTodos();
    }

    // Métodos extra pedidos por la Vista
    public List<Cliente> getClientesEstandar() {
        return clienteDAO.obtenerEstandar();
    }

    public List<Cliente> getClientesPremium() {
        return clienteDAO.obtenerPremium();
    }

    // =======================
    //  PEDIDOS
    // =======================

    // Nuevo método 'addPedido' adaptado a la Vista
    public void addPedido(String emailCliente, String codigoArticulo, int cantidad)
            throws IllegalArgumentException, ArticuloNoEncontradoException {

        Cliente cliente = clienteDAO.obtenerPorEmail(emailCliente);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no existe con email: " + emailCliente);
        }

        Articulo articulo = articuloDAO.obtenerPorCodigo(codigoArticulo);
        // La línea anterior ya lanza ArticuloNoEncontradoException si no existe

        LocalDateTime fechaHora = LocalDateTime.now();

        // Generamos un numPedido único
        int numPedido = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
        if (pedidoDAO.obtenerPorNumPedido(numPedido) != null) {
            numPedido++; // Desempate simple
        }

        Pedido pedido = new Pedido(numPedido, fechaHora, cantidad, articulo, cliente);
        pedidoDAO.insertar(pedido);
    }

    public void eliminarPedido(int numPedido) throws PedidoNoCancelableException, IllegalArgumentException {
        pedidoDAO.eliminar(numPedido);
    }

    // Métodos de filtro adaptados a la Vista
    public List<Pedido> getPedidosPendientes(String emailFiltro) {
        return pedidoDAO.obtenerPendientes(emailFiltro);
    }

    public List<Pedido> getPedidosEnviados(String emailFiltro) {
        return pedidoDAO.obtenerEnviados(emailFiltro);
    }
}