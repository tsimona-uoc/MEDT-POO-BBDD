package MEDT.MEDT.controlador;

import MEDT.MEDT.DAO.IArticuloDAO;
import MEDT.MEDT.DAO.IClienteDAO;
import MEDT.MEDT.DAO.IPedidoDAO;
import MEDT.MEDT.modelo.*;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ControladorPedidos implements IControladorPedidos {

    /// Articulo DAO
    private IArticuloDAO articuloDAO;

    /// Pedido DAO
    private IPedidoDAO pedidoDAO;

    /// Cliente DAO
    private IClienteDAO clienteDAO;

    public ControladorPedidos(IArticuloDAO articuloDAO, IPedidoDAO pedidoDAO, IClienteDAO clienteDAO) {
        this.articuloDAO = articuloDAO;
        this.pedidoDAO = pedidoDAO;
        this.clienteDAO = clienteDAO;
    }

    public int addPedido(int numPedido, int cantidad, LocalDateTime fechaHora, String codigoArticulo, String nifCliente) {
        try {
            Articulo articulo = this.articuloDAO.findByCodigo(codigoArticulo);
            Cliente cliente = this.clienteDAO.findByNIF(nifCliente);

            if (articulo == null)
                return 1;
            if (cliente == null)
                return 2;

            Pedido pedido = new Pedido(numPedido, cantidad, fechaHora, articulo, cliente);

            try {
                this.pedidoDAO.insert(pedido);
                return 0;
            }
            catch (SQLException ex){
                return 3;
            }
        } catch (Exception e) {
            return 9000;
        }
    }

    public int eliminarPedido(int numPedido) throws PedidoNoCancelableException {

        try {
            Pedido pedido = this.pedidoDAO.findByCode(numPedido);
            if (pedido == null){
                return 1;
            }

            if (!pedido.esCancelable()){
                return 2;
            }

            this.pedidoDAO.delete(numPedido);

            return 0;
        }
        catch (SQLException ex){
            System.out.println("Error al eliminar el pedido: " + ex.getMessage());
        }

        return 9000;
    }

    /// Get pedido by numPedido
    public Pedido getPedido(int numPedido){
        try {
            return this.pedidoDAO.findByCode(numPedido);
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public List<Pedido> getPedidos() {
        try {
            return this.pedidoDAO.findAll().stream().toList();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public List<Pedido> getPedidosPendientes(String nif) {
        try {
            return this.pedidoDAO.findPedidosPendientes(nif);
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public List<Pedido> getPedidosEnviados(String nif) {
        try {
            return this.pedidoDAO.findPedidosEnviados(nif);
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public boolean addPedidoYClienteAtomico(int numeroPedido, int cantidad, LocalDateTime fechaHora, String codigoArticulo, String nif, String nombre, String domicilio, String email, int tipo) {
        try {
            Articulo articulo = this.articuloDAO.findByCodigo(codigoArticulo);
            Cliente cliente = null;

            if (tipo == 1){
                cliente = new ClienteEstandar(nombre, domicilio, nif, email);
            }
            else{
                cliente = new ClientePremium(nombre, domicilio, nif, email);
            }

            Pedido pedido = new Pedido(numeroPedido, cantidad, fechaHora, articulo, cliente);
            return this.pedidoDAO.addPedidoYClienteAtomico(pedido, cliente);
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }
}
