package MEDT.MEDT.DAO;

import MEDT.MEDT.modelo.Cliente;

import java.sql.SQLException;
import java.util.List;

public interface IClienteDAO {
    void insert(Cliente cliente) throws SQLException;
    Cliente findByNif(String nif) throws SQLException;
    List<Cliente> findAll() throws SQLException;
    List<Cliente> findEstandar() throws SQLException;
    List<Cliente> findPremium() throws SQLException;
}
