package MEDT.vista.fx;

import MEDT.MEDT.DAO.*;
import MEDT.MEDT.Factory.MEDTFactory;
import MEDT.MEDT.controlador.ControladorArticulos;
import MEDT.MEDT.controlador.ControladorClientes;
import MEDT.MEDT.controlador.ControladorPedidos;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) {

        // Inicialización del modelo
        MEDTFactory.registerType(IArticuloDAO.class, new ArticuloDAOjpa());
        MEDTFactory.registerType(IClienteDAO.class, new ClienteDAOjpa());
        MEDTFactory.registerType(IPedidoDAO.class, new PedidoDAOjpa());


        //Controladores
        ControladorArticulos ca = new ControladorArticulos(MEDTFactory.resolve(IArticuloDAO.class));
        ControladorClientes cc = new ControladorClientes(MEDTFactory.resolve(IClienteDAO.class));
        ControladorPedidos cp = new ControladorPedidos(
                MEDTFactory.resolve(IArticuloDAO.class),
                MEDTFactory.resolve(IPedidoDAO.class),
                MEDTFactory.resolve(IClienteDAO.class)
                );

        //Menu principal
        MenuPrincipalView menu = new MenuPrincipalView(stage, ca, cc, cp);

        menu.getBtnArticulos().setOnAction(e -> {
            ArticulosView view = new ArticulosView(stage, ca, menu);
            stage.setScene(view.getScene());
        });

        menu.getBtnClientes().setOnAction(e -> {
            ClientesView view = new ClientesView(stage, cc, menu);
            stage.setScene(view.getScene());
        });

        menu.getBtnPedidos().setOnAction(e -> {
            PedidosView view = new PedidosView(stage, cp, cc, menu);
            stage.setScene(view.getScene());
        });

        stage.setTitle("MEDT - Gestión");
        stage.setScene(menu.getScene());
        stage.show();

    }
    public static void main(String[] args){
        launch();
    }
}
