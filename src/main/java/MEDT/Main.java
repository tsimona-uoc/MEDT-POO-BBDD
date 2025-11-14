package MEDT;

import MEDT.MEDT.DAO.*;
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
        IPedidoDAO pedidoDAO = new PedidoDAOjdbc();

        ControladorArticulos ca = new ControladorArticulos(articuloDAO);
        ControladorClientes cc = new ControladorClientes(clienteDAO);
        ControladorPedidos cp = new ControladorPedidos(pedidoDAO, clienteDAO, articuloDAO);
        Vista vista = new Vista(ca, cc, cp);

        vista.menu();
    }
}