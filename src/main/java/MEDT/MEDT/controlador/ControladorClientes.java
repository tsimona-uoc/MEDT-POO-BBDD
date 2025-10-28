package MEDT.MEDT.controlador;

import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.ClientePremium;
import MEDT.MEDT.modelo.Datos;

import java.util.List;

public class ControladorClientes {

    private Datos datos;

    public ControladorClientes(Datos datos) {
        this.datos = datos;
    }

    // =======================
    //  CLIENTES
    // =======================
    public boolean addClienteEstandar(String nombre, String domicilio, String nif, String email) {
        ClienteEstandar cliente = new ClienteEstandar(nombre, domicilio, nif, email);
        return datos.addCliente(cliente);
    }

    public boolean addClientePremium(String nombre, String domicilio, String nif, String email) {
        ClientePremium cliente = new ClientePremium(nombre, domicilio, nif, email);
        return datos.addCliente(cliente);
    }

    public List<Cliente> getClientes() {
        return datos.getClientes().stream().toList();
    }

    public List<Cliente> getClientesEstandar() {
        return datos.getClientesEstandar().stream().toList();
    }

    public List<Cliente> getClientesPremium() {
        return datos.getClientesPremium().stream().toList();
    }



    public boolean existeCliente(String nif){
        try {
            return datos.getCliente(nif) != null;
        }
        catch (Exception e) {
            return false;
        }
    }
}
