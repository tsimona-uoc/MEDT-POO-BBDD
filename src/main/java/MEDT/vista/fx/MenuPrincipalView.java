package MEDT.vista.fx;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class MenuPrincipalView {

    private final Button btnArticulos = new Button("Gestión de Artículos");
    private final Button btnClientes = new Button("Gestión de Clientes");
    private final Button btnPedidos = new Button("Gestión de Pedidos");

    public Scene getScene(){
        VBox layout = new VBox(15, btnArticulos, btnClientes, btnPedidos);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        return new Scene(layout, 400, 300);
    }
    public Button getBtnArticulos(){
        return btnArticulos;
    }
    public Button getBtnClientes(){
        return btnClientes;
    }
    public Button getBtnPedidos(){
        return btnPedidos;
    }
}
