package MEDT.MEDT.vista;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

import java.io.IOException;


public class App extends javafx.application.Application {

    @FXML
    Tab articulosTab;

    @FXML
    Tab clientesTab;

    @FXML
    private void initialize() {
        try {
            // Cargar FXML de Tab Articulos
            FXMLLoader articulosTabLoader = new FXMLLoader(getClass().getResource("/views/Articulos/Articulos.fxml"));
            articulosTab.setContent(articulosTabLoader.load()); // inyecta el contenido en la pestaña

            // Cargar FXML de Tab Clientes
            FXMLLoader clientesTabLoader = new FXMLLoader(getClass().getResource("/views/Clientes/Clientes.fxml"));
            clientesTab.setContent(clientesTabLoader.load()); // inyecta el contenido en la pestaña
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
    }
}