package MEDT.MEDT.DAO;

import MEDT.MEDT.modelo.Cliente;

import java.sql.SQLException;

public interface IClienteDAO {
    void insert(Cliente cliente) throws SQLException;
//    void update(Cliente cliente) throws SQLException;
//    void delete(Cliente cliente) throws SQLException;
}
