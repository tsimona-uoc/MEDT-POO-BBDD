package MEDT.MEDT.DAO;

import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.Pedido;
import MEDT.MEDT.modelo.excepciones.TipoClienteInvalidoException;

import java.sql.SQLException;
import java.util.Collection;

public interface IPedidoDAO {

    /// Insert a new order
    public void insert(Pedido pedido) throws SQLException;
    /// Update a given order
    public void update(Pedido pedido) throws SQLException;
    /// Delete a given order by code
    public void delete(int code) throws SQLException;
    /// Find an order by code
    public Pedido findByCode(int code) throws SQLException;
    /// Returns a list of all orders
    public Collection<Pedido> findAll() throws SQLException;
    /// Returns a list of all orders by client
    public Collection<Pedido> findByCliente(Cliente cliente) throws SQLException;
}
