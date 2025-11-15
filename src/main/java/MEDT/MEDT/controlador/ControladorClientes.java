package MEDT.MEDT.controlador;

import MEDT.MEDT.DAO.IClienteDAO;
import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.ClientePremium;
import MEDT.MEDT.modelo.Datos;
import MEDT.MEDT.modelo.excepciones.TipoClienteInvalidoException;

import java.sql.SQLException;
import java.util.List;

public class ControladorClientes {

    private IClienteDAO clienteDAO;

    public ControladorClientes(IClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    // =======================
    //  CLIENTES
    // =======================
    public boolean addClienteEstandar(String nombre, String domicilio, String nif, String email) {
        ClienteEstandar cliente = new ClienteEstandar(nombre, domicilio, nif, email);

        try {
            this.clienteDAO.insert(cliente);
        } catch (SQLException e) {
            System.out.println("Error al insertar el cliente");
            return false;
        }

        return true;
    }

    public boolean addClientePremium(String nombre, String domicilio, String nif, String email) {
        ClientePremium cliente = new ClientePremium(nombre, domicilio, nif, email);

        try {
            this.clienteDAO.insert(cliente);
        } catch (SQLException e) {
            System.out.println("Error al insertar el cliente");
            return false;
        }

        return true;
    }

    public List<Cliente> getClientes() {
        try {
            return this.clienteDAO.findAll().stream().toList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (TipoClienteInvalidoException e) {
            System.out.println("Error al obtener los clientes");
        }

        return null;
    }

    public List<Cliente> getClientesEstandar() {
        try {
            return this.clienteDAO.findStandardClients().stream().map(x -> (Cliente)x).toList();
        } catch (SQLException e) {
            System.out.println("Error al obtener los clientes");
        }

        return null;
    }

    public List<Cliente> getClientesPremium() {
        try {
            return this.clienteDAO.findPremiumClients().stream().map(x -> (Cliente)x).toList();
        } catch (SQLException e) {
            System.out.println("Error al obtener los clientes");
        }

        return null;
    }

    public boolean existeCliente(String nif){
        try {
            return this.clienteDAO.existsClient(nif);
        }
        catch (Exception e) {
            return false;
        }
    }
}
