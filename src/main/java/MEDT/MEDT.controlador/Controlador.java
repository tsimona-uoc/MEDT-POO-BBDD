package MEDT.MEDT.controlador;

import MEDT.MEDT.modelo.*;

import java.time.LocalDateTime;
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

    public List<Articulo> getArticulos() {
        return (List<Articulo>) datos.getArticulos();
    }

    // Gestión Clientes
    public void addClienteEstandar(String nombre, String domicilio, String nif, String email) {
        datos.addCliente(new ClienteEstandar(nombre, domicilio, nif, email));
    }

    public void addClientePremium(String nombre, String domicilio, String nif, String email) {
        datos.addCliente(new ClientePremium(nombre, domicilio, nif, email));
    }

    public List<Cliente> getClientes() {
        return (List<Cliente>) datos.getClientes();
    }

    public List<ClienteEstandar> getClientesEstandar() {
        return datos.getClientesEstandar();
    }

    public List<ClientePremium> getClientesPremium() {
        return datos.getClientesPremium();
    }

    // Gestión Pedidos

}

