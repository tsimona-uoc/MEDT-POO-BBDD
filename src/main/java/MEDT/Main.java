package MEDT;

import MEDT.MEDT.DAO.*;
import MEDT.MEDT.Factory.MEDTFactory;
import MEDT.MEDT.controlador.ControladorArticulos;
import MEDT.MEDT.controlador.ControladorClientes;
import MEDT.MEDT.controlador.ControladorPedidos;
import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.vista.Vista;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        /// Register types
        MEDTFactory.registerType(IArticuloDAO.class, new ArticuloDAOJPA());
        MEDTFactory.registerType(IClienteDAO.class, new ClienteDAOJPA());
        MEDTFactory.registerType(IPedidoDAO.class, new PedidoDAOjdbc(MEDTFactory.resolve(IArticuloDAO.class), MEDTFactory.resolve(IClienteDAO.class)));

        /// Create controllers
        ControladorArticulos ca = new ControladorArticulos(MEDTFactory.resolve(IArticuloDAO.class));
        ControladorPedidos cp = new ControladorPedidos(MEDTFactory.resolve(IArticuloDAO.class), MEDTFactory.resolve(IPedidoDAO.class), MEDTFactory.resolve(IClienteDAO.class));
        ControladorClientes cc = new ControladorClientes(MEDTFactory.resolve(IClienteDAO.class));
        new Vista(cp, ca, cc).menu();
    }
}