package MEDT;

import MEDT.MEDT.DAO.*;
import MEDT.MEDT.Factory.MEDTFactory;
import MEDT.MEDT.controlador.*;
import MEDT.MEDT.vista.App;
import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        /// Register types
        MEDTFactory.registerType(IArticuloDAO.class, new ArticuloDAOJPA());
        MEDTFactory.registerType(IClienteDAO.class, new ClienteDAOJPA());
        MEDTFactory.registerType(IPedidoDAO.class, new PedidoDAOJPA());

        MEDTFactory.registerType(IControladorArticulos.class, new ControladorArticulos(MEDTFactory.resolve(IArticuloDAO.class)));
        MEDTFactory.registerType(IControladorClientes.class, new ControladorClientes(MEDTFactory.resolve(IClienteDAO.class)));
        MEDTFactory.registerType(IControladorPedidos.class, new ControladorPedidos(MEDTFactory.resolve(IArticuloDAO.class), MEDTFactory.resolve(IPedidoDAO.class), MEDTFactory.resolve(IClienteDAO.class)));

        Application.launch(App.class, args);
    }
}
