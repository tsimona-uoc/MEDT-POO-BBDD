package MEDT.vista.fx;

import MEDT.MEDT.controlador.ControladorArticulos;
import MEDT.MEDT.controlador.ControladorClientes;
import MEDT.MEDT.controlador.ControladorPedidos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class MenuPrincipalView {

    private final Stage stage;
    private final ControladorArticulos ca;
    private final ControladorClientes cc;
    private final ControladorPedidos cp;

    private final Button btnArticulos = new Button("Gestión de Artículos");
    private final Button btnClientes  = new Button("Gestión de Clientes");
    private final Button btnPedidos   = new Button("Gestión de Pedidos");

    private final Scene scene;

    public MenuPrincipalView(Stage stage,
                             ControladorArticulos ca,
                             ControladorClientes cc,
                             ControladorPedidos cp) {

        this.stage = stage;
        this.ca = ca;
        this.cc = cc;
        this.cp = cp;

        VBox layout = new VBox(15, btnArticulos, btnClientes, btnPedidos);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        this.scene = new Scene(layout, 400, 300);
    }

    public Scene getScene() {
        return scene;
    }

    public Button getBtnArticulos() {
        return btnArticulos;
    }

    public Button getBtnClientes() {
        return btnClientes;
    }

    public Button getBtnPedidos() {
        return btnPedidos;
    }

}
