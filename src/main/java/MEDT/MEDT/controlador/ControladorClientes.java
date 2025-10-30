package MEDT.MEDT.controlador;

import MEDT.MEDT.DAO.IClienteDAO;
import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.ClientePremium;
import MEDT.MEDT.modelo.Datos;

import java.util.List;

public class ControladorClientes {
    private IClienteDAO clienteDAO;

    public ControladorClientes(IClienteDAO clienteDAO){
        this.clienteDAO = clienteDAO;
    }
    public boolean addClienteEstandar(String nombre, String domicilio, String nif, String email) {
        try {
            ClienteEstandar cliente = new ClienteEstandar(nombre, domicilio, nif, email);
            clienteDAO.insert(cliente);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public boolean addClientePremium(String nombre, String domicilio, String nif, String email) {
        try{
            ClientePremium cliente = new ClientePremium(nombre, domicilio, nif, email);
            clienteDAO.insert(cliente);
        }catch(Exception e){
            return false;
        }
        return true;
    }
//
//    public List<Cliente> getClientes() {
//        return datos.getClientes().stream().toList();
//    }
//
//    public List<Cliente> getClientesEstandar() {
//        return datos.getClientesEstandar().stream().toList();
//    }
//
//    public List<Cliente> getClientesPremium() {
//        return datos.getClientesPremium().stream().toList();
//    }
}
