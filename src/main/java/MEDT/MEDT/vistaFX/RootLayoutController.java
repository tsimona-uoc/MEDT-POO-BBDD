package MEDT.MEDT.vistaFX;

import MEDT.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class RootLayoutController {

    private Main main;

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private void handleGestionArticulos() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/MEDT/view/GestionArticulos.fxml"));
            AnchorPane gestionArticulos = (AnchorPane) loader.load();

            ((BorderPane) main.getRootLayout()).setCenter(gestionArticulos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGestionClientes() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/MEDT/view/GestionClientes.fxml"));
            AnchorPane gestionClientes = (AnchorPane) loader.load();

            ((BorderPane) main.getRootLayout()).setCenter(gestionClientes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGestionPedidos() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/MEDT/view/GestionPedidos.fxml"));
            AnchorPane gestionPedidos = (AnchorPane) loader.load();

            ((BorderPane) main.getRootLayout()).setCenter(gestionPedidos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
