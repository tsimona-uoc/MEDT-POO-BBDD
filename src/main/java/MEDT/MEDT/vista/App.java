package MEDT.MEDT.vista;

import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.Pedido;
import MEDT.MEDT.vista.Articulo.Articulos;
import MEDT.MEDT.vista.Cliente.Clientes;
import MEDT.MEDT.vista.Pedidos.Pedidos;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;


public class App extends javafx.application.Application {

    @FXML
    TabPane tabsContainer;

    @FXML
    Tab articulosTab;

    @FXML
    Tab clientesTab;

    @FXML
    Tab pedidosTab;

    private Articulos articuloController;
    private Pedidos pedidoController;
    private Clientes clienteController;

    /// Open articulos tab
    public void OpenArticulosTab(){
        this.tabsContainer.getSelectionModel().select(articulosTab);
    }

    /// Open clientes tab
    public void OpenClientesTab(){
        this.tabsContainer.getSelectionModel().select(clientesTab);
    }

    /// Open pedidos tab
    public void OpenPedidosTab(){
        this.tabsContainer.getSelectionModel().select(pedidosTab);
    }

    /// Get articulos tab
    public Tab getArticulosTab(){
        return articulosTab;
    }

    /// Get clientes tab
    public Tab getClientesTab(){
        return clientesTab;
    }

    /// Get pedidos tab
    public Tab getPedidosTab(){
        return pedidosTab;
    }

    /// Get articulos controller
    public Articulos getArticuloController() {
        return articuloController;
    }

    /// Get pedidos controller
    public Pedidos getPedidoController() {
        return pedidoController;
    }

    /// Get clientes controller
    public Clientes getClienteController() {
        return clienteController;
    }

    @FXML
    private void initialize() {
        try {
            // Cargar FXML de Tab Articulos
            FXMLLoader articulosTabLoader = new FXMLLoader(getClass().getResource("/views/Articulos/Articulos.fxml"));
            articulosTab.setContent(articulosTabLoader.load()); // inyecta el contenido en la pestaña
            this.articuloController = articulosTabLoader.getController();

            // Cargar FXML de Tab Clientes
            FXMLLoader clientesTabLoader = new FXMLLoader(getClass().getResource("/views/Clientes/Clientes.fxml"));
            clientesTab.setContent(clientesTabLoader.load()); // inyecta el contenido en la pestaña
            this.clienteController = clientesTabLoader.getController();

            // Cargar FXML de Tab Pedidos
            FXMLLoader pedidosTabLoader = new FXMLLoader(getClass().getResource("/views/Pedidos/Pedidos.fxml"));
            pedidosTab.setContent(pedidosTabLoader.load()); // inyecta el contenido en la pestaña
            this.pedidoController = pedidosTabLoader.getController();

            ((Pedidos)pedidosTabLoader.getController()).setParent(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Carga el archivo FXML que contiene el diseño de tu ventana
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600); // Define el tamaño inicial

        stage.setTitle("Mi Aplicación JavaFX");
        stage.setScene(scene);
        stage.show();

        String cssPath = getClass().getResource("/styles/style.css").toExternalForm();
        scene.getStylesheets().add(cssPath);
        System.out.println("Loaded!");
    }
}