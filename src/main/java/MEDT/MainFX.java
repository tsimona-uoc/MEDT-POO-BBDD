package MEDT;

import MEDT.MEDT.DAO.*;
import MEDT.MEDT.Factory.MEDTFactory;
import MEDT.MEDT.controlador.ControladorArticulos;
import MEDT.MEDT.controlador.ControladorClientes;
import MEDT.MEDT.controlador.ControladorPedidos;
import MEDT.MEDT.vista.VistaArticulos;
import MEDT.MEDT.vista.VistaClientes;
import MEDT.MEDT.vista.VistaPedidos;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainFX extends Application {

    private ControladorArticulos ca;
    private ControladorClientes cc;
    private ControladorPedidos cp;

    @Override
    public void init() throws Exception {
        // Inicializamos la capa de persistencia (ORM/JDBC) y Controladores
        MEDTFactory.registerType(IArticuloDAO.class, new ArticuloDAOjdbc());
        MEDTFactory.registerType(IClienteDAO.class, new ClienteDAOjdbc());
        MEDTFactory.registerType(IPedidoDAO.class, new PedidoDAOjdbc(MEDTFactory.resolve(IArticuloDAO.class), MEDTFactory.resolve(IClienteDAO.class)));

        ca = new ControladorArticulos(MEDTFactory.resolve(IArticuloDAO.class));
        cc = new ControladorClientes(MEDTFactory.resolve(IClienteDAO.class));
        cp = new ControladorPedidos(MEDTFactory.resolve(IArticuloDAO.class), MEDTFactory.resolve(IPedidoDAO.class), MEDTFactory.resolve(IClienteDAO.class));
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tienda Online - JavaFX MVC");

        TabPane tabPane = new TabPane();

        // Creamos las pestañas para cada vista
        Tab tabArticulos = new Tab("Artículos", new VistaArticulos(ca).getView());
        tabArticulos.setClosable(false);

        Tab tabClientes = new Tab("Clientes", new VistaClientes(cc).getView());
        tabClientes.setClosable(false);

        Tab tabPedidos = new Tab("Pedidos", new VistaPedidos(cp, ca, cc).getView());
        tabPedidos.setClosable(false);

        tabPane.getTabs().addAll(tabArticulos, tabClientes, tabPedidos);

        Scene scene = new Scene(tabPane, 950, 650);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}