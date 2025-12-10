package MEDT.MEDT.vistaFX;

import MEDT.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class RootLayoutController {

    private Main main;

    private GestionClientesController gestionClientesController;

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private Tab articulosTab;
    @FXML
    private Tab clientesTab;
    @FXML
    private Tab pedidosTab;


    @FXML
    private void handleGestionArticulos() {
        if (articulosTab.isSelected() && articulosTab.getContent() == null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("/MEDT/view/GestionArticulos.fxml"));
                AnchorPane gestionArticulos = (AnchorPane) loader.load();
                articulosTab.setContent(gestionArticulos);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    private void handleGestionClientes() {
        if (clientesTab.isSelected() && clientesTab.getContent() == null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("/MEDT/view/GestionClientes.fxml"));
                AnchorPane gestionClientes = (AnchorPane) loader.load();
                clientesTab.setContent(gestionClientes);
                this.gestionClientesController = loader.getController();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleGestionPedidos() {
        if (pedidosTab.isSelected() && pedidosTab.getContent() == null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("/MEDT/view/GestionPedidos.fxml"));
                AnchorPane gestionPedidos = (AnchorPane) loader.load();

                pedidosTab.setContent(gestionPedidos);
                GestionPedidosController controller = loader.getController();
                controller.setRootLayoutController(this);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void switchToClientesTabAndSetNif(String nif) {
        TabPane tabPane = clientesTab.getTabPane();
        tabPane.getSelectionModel().select(clientesTab);

        if (clientesTab.getContent() == null) {
            handleGestionClientes();
        }

        if (this.gestionClientesController != null) {
            this.gestionClientesController.setNifParaNuevoCliente(nif);
        }
    }
}