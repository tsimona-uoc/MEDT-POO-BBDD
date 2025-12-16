package MEDT.MEDT.vista;

import MEDT.MEDT.controlador.ControladorArticulos;
import MEDT.MEDT.modelo.Articulo;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class VistaArticulos {

    private ControladorArticulos controlador;
    private TableView<Articulo> tabla;

    public VistaArticulos(ControladorArticulos controlador) {
        this.controlador = controlador;
    }

    public BorderPane getView() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        // --- IZQUIERDA: FORMULARIO ---
        VBox form = new VBox(10);
        form.setPadding(new Insets(10));
        form.setPrefWidth(300);
        form.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 15;");

        Label lblTitle = new Label("Nuevo Artículo");
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        TextField txtCodigo = new TextField(); txtCodigo.setPromptText("Código (ej: A-100)");
        TextField txtDesc = new TextField(); txtDesc.setPromptText("Descripción");
        TextField txtPrecio = new TextField(); txtPrecio.setPromptText("Precio (€)");
        TextField txtGastos = new TextField(); txtGastos.setPromptText("Gastos Envío (€)");
        TextField txtTiempo = new TextField(); txtTiempo.setPromptText("Tiempo Prep (min)");

        Button btnAdd = new Button("Añadir Artículo");
        btnAdd.setMaxWidth(Double.MAX_VALUE);
        btnAdd.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        // Lógica del botón Añadir
        btnAdd.setOnAction(e -> {
            try {
                String cod = txtCodigo.getText();
                String desc = txtDesc.getText();
                if (cod.isEmpty() || desc.isEmpty() || txtPrecio.getText().isEmpty()) {
                    mostrarAlerta("Error", "Rellene los campos obligatorios.");
                    return;
                }
                double pre = Double.parseDouble(txtPrecio.getText());
                double gas = Double.parseDouble(txtGastos.getText());
                int time = Integer.parseInt(txtTiempo.getText());

                if (controlador.addArticulo(cod, desc, pre, gas, time)) {
                    mostrarAlerta("Éxito", "Artículo añadido correctamente.");
                    actualizarTabla();
                    // Limpiar campos
                    txtCodigo.clear(); txtDesc.clear(); txtPrecio.clear(); txtGastos.clear(); txtTiempo.clear();
                } else {
                    mostrarAlerta("Error", "No se pudo añadir (Código duplicado).");
                }
            } catch (NumberFormatException ex) {
                mostrarAlerta("Error", "Revise los campos numéricos.");
            }
        });

        form.getChildren().addAll(lblTitle, txtCodigo, txtDesc, txtPrecio, txtGastos, txtTiempo, btnAdd);

        // --- CENTRO: TABLA ---
        tabla = new TableView<>();

        TableColumn<Articulo, String> colCod = new TableColumn<>("Código");
        colCod.setCellValueFactory(new PropertyValueFactory<>("codigo"));

        TableColumn<Articulo, String> colDesc = new TableColumn<>("Descripción");
        colDesc.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        TableColumn<Articulo, Double> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        TableColumn<Articulo, Double> colGastos = new TableColumn<>("Envío");
        colGastos.setCellValueFactory(new PropertyValueFactory<>("gastosEnvio"));

        tabla.getColumns().addAll(colCod, colDesc, colPrecio, colGastos);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Ajustar columnas

        actualizarTabla();

        root.setLeft(form);
        root.setCenter(tabla);
        BorderPane.setMargin(tabla, new Insets(0, 0, 0, 15)); // Margen entre form y tabla

        return root;
    }

    private void actualizarTabla() {
        if (controlador.getArticulos() != null) {
            tabla.setItems(FXCollections.observableArrayList(controlador.getArticulos()));
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
