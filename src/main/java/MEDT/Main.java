package MEDT;

import MEDT.MEDT.DAO.ArticuloDAOjdbc;
import MEDT.MEDT.DAO.ClienteDAOjdbc;
import MEDT.MEDT.DAO.IArticuloDAO;
import MEDT.MEDT.DAO.IClienteDAO;
import MEDT.MEDT.controlador.ControladorArticulos;
import MEDT.MEDT.controlador.ControladorClientes;
import MEDT.MEDT.controlador.ControladorPedidos;
import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.modelo.Datos;
import MEDT.MEDT.util.DBConnection;
import MEDT.MEDT.vista.Vista;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {

        IArticuloDAO articuloDAO = new ArticuloDAOjdbc();
        IClienteDAO clienteDAO = new ClienteDAOjdbc();

        ControladorArticulos ca = new ControladorArticulos(articuloDAO);
        ControladorClientes cc = new ControladorClientes(clienteDAO);

        Datos datos = new Datos();

        ControladorPedidos cp = new ControladorPedidos(datos);
        Vista vista = new Vista(ca, cc, cp);

        vista.menu();
    }
}