package MEDT;

import MEDT.MEDT.DAO.*;
import MEDT.MEDT.Factory.MEDTFactory;
import MEDT.MEDT.controlador.ControladorArticulos;
import MEDT.MEDT.controlador.ControladorClientes;
import MEDT.MEDT.controlador.IControladorArticulos;
import MEDT.MEDT.controlador.IControladorClientes;
import MEDT.MEDT.vista.App;
import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        /// Register types
        MEDTFactory.registerType(IArticuloDAO.class, new ArticuloDAOjdbc());
        MEDTFactory.registerType(IClienteDAO.class, new ClienteDAOjdbc());
        MEDTFactory.registerType(IPedidoDAO.class, new PedidoDAOjdbc(MEDTFactory.resolve(IArticuloDAO.class), MEDTFactory.resolve(IClienteDAO.class)));

        MEDTFactory.registerType(IControladorArticulos.class, new ControladorArticulos(MEDTFactory.resolve(IArticuloDAO.class)));
        MEDTFactory.registerType(IControladorClientes.class, new ControladorClientes(MEDTFactory.resolve(IClienteDAO.class)));

        Application.launch(App.class, args);
    }
}
