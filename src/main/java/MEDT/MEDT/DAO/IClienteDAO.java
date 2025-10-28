package MEDT.MEDT.DAO;

import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.ClientePremium;
import MEDT.MEDT.modelo.excepciones.TipoClienteInvalidoException;

import java.sql.SQLException;
import java.util.Collection;

public interface IClienteDAO {

    /// Insert a new client
    public void insert(Cliente cliente) throws SQLException;
    /// Update a given client
    public void update(Cliente cliente) throws SQLException;
    /// Delete a given client by nif
    public void delete(String nif) throws SQLException;
    /// Find a client by nif
    public Cliente findByNIF(String codigo) throws SQLException, TipoClienteInvalidoException;
    /// Returns a list of all clients
    public Collection<Cliente> findAll() throws SQLException, TipoClienteInvalidoException;
    /// Returns a list of all standard clients
    public Collection<ClienteEstandar> findStandardClients() throws SQLException;
    /// Returns a list of all premium clients
    public Collection<ClientePremium> findPremiumClients() throws SQLException;
    /// Return whether client exists or not
    public boolean existsClient(String nif) throws SQLException;
}
