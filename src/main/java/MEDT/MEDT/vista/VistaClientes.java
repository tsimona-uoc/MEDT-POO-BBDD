package MEDT.MEDT.vista;

import MEDT.MEDT.controlador.ControladorClientes;
import MEDT.MEDT.modelo.Cliente;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class VistaClientes {

    private ControladorClientes controlador;
    private TableView<Cliente> tabla;

    public VistaClientes(ControladorClientes controlador) {
        this.controlador = controlador;
    }

    public BorderPane getView() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        // --- FORMULARIO ---
        VBox form = new VBox(10);
        form.setPadding(new Insets(10));
        form.setPrefWidth(300);
        form.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 15;");

        Label lblTitle = new Label("Nuevo Cliente");
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        TextField txtNombre = new TextField(); txtNombre.setPromptText("Nombre");
        TextField txtDom = new TextField(); txtDom.setPromptText("Domicilio");
        TextField txtNif = new TextField(); txtNif.setPromptText("NIF");
        TextField txtEmail = new TextField(); txtEmail.setPromptText("Email");

        // RadioButtons para Tipo
        ToggleGroup grupoTipo = new ToggleGroup();
        RadioButton rbEstandar = new RadioButton("Estándar");
        rbEstandar.setToggleGroup(grupoTipo);
        rbEstandar.setSelected(true);
        RadioButton rbPremium = new RadioButton("Premium");
        rbPremium.setToggleGroup(grupoTipo);

        HBox tipos = new HBox(15, rbEstandar, rbPremium);

        Button btnAdd = new Button("Añadir Cliente");
        btnAdd.setMaxWidth(Double.MAX_VALUE);
        btnAdd.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

        btnAdd.setOnAction(e -> {
            String nom = txtNombre.getText();
            String dom = txtDom.getText();
            String nif = txtNif.getText();
            String em = txtEmail.getText();

            if (nom.isEmpty() || nif.isEmpty()) {
                mostrarAlerta("Error", "Nombre y NIF son obligatorios.");
                return;
            }

            boolean exito;
            if (rbEstandar.isSelected()) {
                exito = controlador.addClienteEstandar(nom, dom, nif, em);
            } else {
                exito = controlador.addClientePremium(nom, dom, nif, em);
            }

            if (exito) {
                mostrarAlerta("Éxito", "Cliente registrado.");
                actualizarTabla();
                txtNombre.clear(); txtDom.clear(); txtNif.clear(); txtEmail.clear();
            } else {
                mostrarAlerta("Error", "No se pudo añadir (¿NIF ya existe?).");
            }
        });

        // Filtros visuales
        Separator sep = new Separator();
        Button btnTodos = new Button("Ver Todos");
        Button btnPremium = new Button("Ver Premium");
        Button btnEstandar = new Button("Ver Estándar");
        HBox filtros = new HBox(5, btnTodos, btnPremium, btnEstandar);

        btnTodos.setOnAction(e -> actualizarTabla());
        btnPremium.setOnAction(e -> {
            if (controlador.getClientesPremium() != null)
                tabla.setItems(FXCollections.observableArrayList(controlador.getClientesPremium()));
        });
        btnEstandar.setOnAction(e -> {
            if (controlador.getClientesEstandar() != null)
                tabla.setItems(FXCollections.observableArrayList(controlador.getClientesEstandar()));
        });

        form.getChildren().addAll(lblTitle, txtNombre, txtDom, txtNif, txtEmail, tipos, btnAdd, sep, new Label("Filtros:"), filtros);

        // --- TABLA ---
        tabla = new TableView<>();

        TableColumn<Cliente, String> colNif = new TableColumn<>("NIF");
        colNif.setCellValueFactory(new PropertyValueFactory<>("nif"));

        TableColumn<Cliente, String> colNom = new TableColumn<>("Nombre");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Cliente, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Usamos una columna calculada para el tipo (opcional, si quisieras distinguirlos visualmente)

        tabla.getColumns().addAll(colNif, colNom, colEmail);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        actualizarTabla();

        root.setLeft(form);
        root.setCenter(tabla);
        BorderPane.setMargin(tabla, new Insets(0, 0, 0, 15));

        return root;
    }

    private void actualizarTabla() {
        if (controlador.getClientes() != null) {
            tabla.setItems(FXCollections.observableArrayList(controlador.getClientes()));
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}