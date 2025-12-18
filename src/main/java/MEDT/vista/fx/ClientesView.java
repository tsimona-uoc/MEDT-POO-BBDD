package MEDT.vista.fx;

import MEDT.MEDT.controlador.ControladorClientes;
import MEDT.MEDT.modelo.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ClientesView {
    private final Stage stage;
    private final ControladorClientes controlador;
    private final MenuPrincipalView menuPrincipal;
    private final Scene scene;

    private final TableView<Cliente> table = new TableView<>();

    public ClientesView(Stage stage, ControladorClientes controlador, MenuPrincipalView menuPrincipal){
        this.stage = stage;
        this.controlador = controlador;
        this.menuPrincipal = menuPrincipal;

        BorderPane root = new BorderPane();
        root.setTop(crearFormulario());
        root.setCenter(crearTabla());
        root.setBottom(crearBotonVolver());

        this.scene = new Scene(root, 800,500);
        cargarClientes();
    }
    public Scene getScene(){
        return scene;
    }
    private GridPane crearFormulario() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-padding: 15;");

        TextField txtNombre = new TextField();
        TextField txtDomicilio = new TextField();
        TextField txtNif = new TextField();
        TextField txtEmail = new TextField();

        ComboBox<String> cmbTipo = new ComboBox<>();
        cmbTipo.getItems().addAll("Estandar", "Premium");
        cmbTipo.setValue("Estandar");

        Button btnAgregar = new Button("Añadir cliente");

        grid.add(new Label("Nombre"), 0, 0);
        grid.add(txtNombre, 1, 0);

        grid.add(new Label("Domicilio"), 0, 1);
        grid.add(txtDomicilio, 1, 1);

        grid.add(new Label("NIF"), 0, 2);
        grid.add(txtNif, 1, 2);

        grid.add(new Label("Email"), 0, 3);
        grid.add(txtEmail, 1, 3);

        grid.add(new Label("Tipo"), 0, 4);
        grid.add(cmbTipo, 1, 4);

        grid.add(btnAgregar, 1, 5);

        btnAgregar.setOnAction(e -> {
            try {
                boolean ok;

                if (cmbTipo.getValue().equals("Estandar")) {
                    ok = controlador.addClienteEstandar(
                            txtNombre.getText(),
                            txtDomicilio.getText(),
                            txtNif.getText(),
                            txtEmail.getText()
                    );
                } else {
                    ok = controlador.addClientePremium(
                            txtNombre.getText(),
                            txtDomicilio.getText(),
                            txtNif.getText(),
                            txtEmail.getText()
                    );
                }

                if (ok) {
                    cargarClientes();
                    txtNombre.clear();
                    txtDomicilio.clear();
                    txtNif.clear();
                    txtEmail.clear();
                } else {
                    mostrarError("No se pudo añadir el cliente");
                }

            } catch (Exception ex) {
                mostrarError("Datos incorrectos");
            }
        });

        return grid;
    }
    private TableView<Cliente> crearTabla() {

        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre())
        );

        TableColumn<Cliente, String> colNif = new TableColumn<>("NIF");
        colNif.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getNif())
        );

        TableColumn<Cliente, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail())
        );

        table.getColumns().addAll(colNombre, colNif, colEmail);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return table;
    }
    private void cargarClientes() {
        var lista = controlador.getClientes();
        table.setItems(FXCollections.observableArrayList(lista));
    }
    private HBox crearBotonVolver() {
        Button btnVolver = new Button("Volver");

        btnVolver.setOnAction(e ->
                stage.setScene(menuPrincipal.getScene())
        );

        HBox box = new HBox(btnVolver);
        box.setStyle("-fx-padding: 10; -fx-alignment: center;");
        return box;
    }
    private void mostrarError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
