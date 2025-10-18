package MEDT.MEDT.controlador;

import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.ClientePremium;
import MEDT.MEDT.modelo.Datos;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class Controlador {

    private final Datos datos;

    public Controlador() {
        this.datos = new Datos();
    }

    // Gestión Artículos
    public void addArticulo(String codigo, String descripcion, double precio, double gastosEnvio, int tiempoPrep) {
        Articulo articulo = new Articulo(codigo, descripcion, precio, gastosEnvio, tiempoPrep);
        datos.addArticulo(articulo);
    }

    public Collection<Articulo> getArticulos() {
        return datos.getArticulos();
    }

    // Gestión Clientes
    public void addClienteEstandar(String nombre, String domicilio, String nif, String email) {
        datos.addCliente(new ClienteEstandar(nombre, domicilio, nif, email));
    }

    public void addClientePremium(String nombre, String domicilio, String nif, String email) {
        datos.addCliente(new ClientePremium(nombre, domicilio, nif, email));
    }

    public Collection<Cliente> getClientes() {
        return datos.getClientes();
    }

    public Collection<ClienteEstandar> getClientesEstandar() {
        return datos.getClientesEstandar();
    }

    public Collection<ClientePremium> getClientesPremium() {
        return datos.getClientesPremium();
    }

    // Gestión Pedidos

}

