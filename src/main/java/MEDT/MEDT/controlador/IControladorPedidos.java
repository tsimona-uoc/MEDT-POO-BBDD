package MEDT.MEDT.controlador;

import MEDT.MEDT.DAO.IArticuloDAO;
import MEDT.MEDT.DAO.IClienteDAO;
import MEDT.MEDT.DAO.IPedidoDAO;
import MEDT.MEDT.modelo.*;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface IControladorPedidos {

    /// Add pedido
    int addPedido(int numPedido, int cantidad, LocalDateTime fechaHora, String codigoArticulo, String nifCliente);

    /// Eliminar pedido
    int eliminarPedido(int numPedido) throws PedidoNoCancelableException;

    /// Get pedidos pendientes
    List<Pedido> getPedidos();

    /// Get pedido by numPedido
    Pedido getPedido(int numPedido);

    /// Get pedidos pendientes
    List<Pedido> getPedidosPendientes(String nif);

    /// Get pedidos enviados
    List<Pedido> getPedidosEnviados(String nif);

    /// Add pedido y cliente atomic
    boolean addPedidoYClienteAtomico(int numeroPedido, int cantidad, LocalDateTime fechaHora, String codigoArticulo, String nif, String nombre, String domicilio, String email, int tipo);
}