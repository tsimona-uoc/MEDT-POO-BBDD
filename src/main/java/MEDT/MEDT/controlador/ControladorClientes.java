package MEDT.MEDT.controlador;

import MEDT.MEDT.DAO.IClienteDAO;
import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.ClientePremium;
import MEDT.MEDT.modelo.Datos;

import java.util.List;
import java.util.Collections;
import java.sql.SQLException;

public class ControladorClientes {
    private IClienteDAO clienteDAO;

    public ControladorClientes(IClienteDAO clienteDAO){
        this.clienteDAO = clienteDAO;
    }
    public boolean addClienteEstandar(String nombre, String domicilio, String nif, String email) {
        try {
            ClienteEstandar cliente = new ClienteEstandar(nombre, domicilio, nif, email);
            clienteDAO.insert(cliente);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public boolean addClientePremium(String nombre, String domicilio, String nif, String email) {
        try{
            ClientePremium cliente = new ClientePremium(nombre, domicilio, nif, email);
            clienteDAO.insert(cliente);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    public List<Cliente> getClientes() {
        try{
            return clienteDAO.findAll();
        }catch (SQLException e){
            return Collections.emptyList();
        }
    }

    public List<Cliente> getClientesEstandar() {
        try{
            return clienteDAO.findEstandar();
        }catch (SQLException e){
            return Collections.emptyList();
        }
    }

    public List<Cliente> getClientesPremium() {
        try{
            return clienteDAO.findPremium();
        }catch (SQLException e){
            return Collections.emptyList();
        }
    }
    public Cliente getClienteByNif(String nif){
        try{
            return clienteDAO.findByNif(nif);
        }catch (SQLException e){
            return null;
        }
    }
}
