package MEDT.MEDT.controlador;

import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.ClientePremium;
import MEDT.MEDT.modelo.Datos;
import MEDT.MEDT.persistencia.DAO.ClienteDAO;
import MEDT.MEDT.persistencia.JDBC.ClienteJDBC;

import java.util.List;

public class ControladorClientes {

    private ClienteDAO clienteDAO;

    public ControladorClientes() {
        this.clienteDAO = new ClienteJDBC();
    }

    // =======================
    //  CLIENTES
    // =======================
    public boolean addClienteEstandar(String nombre, String domicilio, String nif, String email) {
        ClienteEstandar cliente = new ClienteEstandar(nombre, domicilio, nif, email);
        return clienteDAO.addCliente(cliente);
    }

    public boolean addClientePremium(String nombre, String domicilio, String nif, String email) {
        ClientePremium cliente = new ClientePremium(nombre, domicilio, nif, email);
        return clienteDAO.addCliente(cliente);
    }

    public List<Cliente> getClientes() {
        return clienteDAO.getClientes().stream().toList();
    }

    public List<Cliente> getClientesEstandar() {
        return clienteDAO.getClientesEstandar().stream().toList();
    }

    public List<Cliente> getClientesPremium() {
        return clienteDAO.getClientesPremium().stream().toList();
    }



    public boolean existeCliente(String nif){
        try {
            return clienteDAO.getCliente(nif) != null;
        }
        catch (Exception e) {
            return false;
        }
    }
}
