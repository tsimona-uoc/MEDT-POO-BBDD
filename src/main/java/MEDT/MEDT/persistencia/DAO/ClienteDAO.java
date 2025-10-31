package MEDT.MEDT.persistencia.DAO;

import MEDT.MEDT.modelo.Cliente;

import java.util.Collection;
import java.util.List;

public interface ClienteDAO {

    boolean addCliente(Cliente cliente);
    Cliente getCliente(String nif);
    Collection<Cliente> getClientes();
    List<Cliente> getClientesEstandar();
    List<Cliente> getClientesPremium();
}
