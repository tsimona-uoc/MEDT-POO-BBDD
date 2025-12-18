package MEDT.MEDT.DAO;

import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.Pedido;
import MEDT.MEDT.modelo.excepciones.TipoClienteInvalidoException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface IPedidoDAO {

    /// Insert a new order
    void insert(Pedido pedido) throws SQLException;
    /// Update a given order
    void update(Pedido pedido) throws SQLException;
    /// Delete a given order by code
    void delete(int code) throws SQLException;
    /// Find an order by code
    Pedido findByCode(int code) throws SQLException;
    /// Returns a list of all orders
    Collection<Pedido> findAll() throws SQLException;
    /// Returns a list of all orders by client
    Collection<Pedido> findByCliente(Cliente cliente) throws SQLException;
    /// Returns a list of pedidos enviados
    List<Pedido> findPedidosEnviados(String nif) throws SQLException;
    /// Returns a list of pedidos pendientes
    List<Pedido> findPedidosPendientes(String nif) throws SQLException;
    /// Add pedido y cliente atomico
    boolean addPedidoYClienteAtomico(Pedido pedido, Cliente cliente) throws SQLException;
}
