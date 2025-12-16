package MEDT.MEDT.vista;

import MEDT.MEDT.controlador.ControladorArticulos;
import MEDT.MEDT.controlador.ControladorClientes;
import MEDT.MEDT.controlador.ControladorPedidos;
import MEDT.MEDT.modelo.Pedido;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class VistaPedidos {

    private ControladorPedidos cp;
    private ControladorArticulos ca;
    private ControladorClientes cc;
    private TableView<Pedido> tabla;

    public VistaPedidos(ControladorPedidos cp, ControladorArticulos ca, ControladorClientes cc) {
        this.cp = cp;
        this.ca = ca;
        this.cc = cc;
    }

    public BorderPane getView() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        // --- FORMULARIO PEDIDOS ---
        VBox form = new VBox(10);
        form.setPadding(new Insets(10));
        form.setPrefWidth(300);
        form.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 15;");

        Label lblTitle = new Label("Gestión de Pedidos");
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        TextField txtNum = new TextField(); txtNum.setPromptText("Num Pedido (ej: 101)");
        TextField txtCant = new TextField(); txtCant.setPromptText("Cantidad");
        TextField txtFecha = new TextField(); txtFecha.setPromptText("Fecha (yyyy-MM-dd HH:mm)");
        // Pre-llenar con fecha actual
        txtFecha.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        TextField txtCodArt = new TextField(); txtCodArt.setPromptText("Cód. Artículo (ej: A-001)");
        TextField txtNifCli = new TextField(); txtNifCli.setPromptText("NIF Cliente");

        Button btnAdd = new Button("Crear Pedido");
        btnAdd.setMaxWidth(Double.MAX_VALUE);
        btnAdd.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");

        Button btnDel = new Button("Eliminar Seleccionado");
        btnDel.setMaxWidth(Double.MAX_VALUE);
        btnDel.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");

        // --- LÓGICA DE AÑADIR ---
        btnAdd.setOnAction(e -> {
            try {
                int num = Integer.parseInt(txtNum.getText());
                int cant = Integer.parseInt(txtCant.getText());
                LocalDateTime fecha = LocalDateTime.parse(txtFecha.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                String codArt = txtCodArt.getText();
                String nif = txtNifCli.getText();

                // Verificar si existe el cliente
                if (!cc.existeCliente(nif)) {
                    // Si no existe, abrimos diálogo (Transacción Atómica)
                    abrirDialogoTransaccionAtomica(num, cant, fecha, codArt, nif);
                } else {
                    // Flujo normal
                    String res = cp.addPedido(num, cant, fecha, codArt, nif);
                    mostrarAlerta("Info", res);
                }

                // IMPORTANTE: Refrescar TODOS para asegurar que se ve el nuevo
                actualizarTabla("TODOS", null);

            } catch (NumberFormatException ex) {
                mostrarAlerta("Error", "Revise los campos numéricos.");
            } catch (DateTimeParseException ex) {
                mostrarAlerta("Error", "Formato de fecha incorrecto. Use: yyyy-MM-dd HH:mm");
            }
        });

        // --- LÓGICA ELIMINAR ---
        btnDel.setOnAction(e -> {
            Pedido selected = tabla.getSelectionModel().getSelectedItem();
            if (selected == null) {
                mostrarAlerta("Error", "Seleccione un pedido de la tabla.");
                return;
            }
            try {
                if (cp.eliminarPedido(selected.getNumPedido())) {
                    mostrarAlerta("Éxito", "Pedido eliminado.");
                    actualizarTabla("TODOS", null); // Refrescar lista completa
                }
            } catch (PedidoNoCancelableException | IllegalArgumentException ex) {
                mostrarAlerta("Error", ex.getMessage());
            }
        });

        // --- FILTROS ---
        Separator sep = new Separator();
        TextField txtFiltroNif = new TextField(); txtFiltroNif.setPromptText("Filtrar NIF...");

        Button btnTodos = new Button("Ver Todos");
        Button btnPendientes = new Button("Ver Pendientes");
        Button btnEnviados = new Button("Ver Enviados");

        btnTodos.setOnAction(e -> actualizarTabla("TODOS", null));
        btnPendientes.setOnAction(e -> actualizarTabla("PENDIENTES", txtFiltroNif.getText()));
        btnEnviados.setOnAction(e -> actualizarTabla("ENVIADOS", txtFiltroNif.getText()));

        form.getChildren().addAll(lblTitle, txtNum, txtCant, txtFecha, txtCodArt, txtNifCli, btnAdd, btnDel, sep, new Label("Filtros:"), txtFiltroNif, btnTodos, btnPendientes, btnEnviados);

        // --- TABLA ---
        tabla = new TableView<>();

        TableColumn<Pedido, Integer> colNum = new TableColumn<>("Num");
        colNum.setCellValueFactory(new PropertyValueFactory<>("numPedido"));

        TableColumn<Pedido, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaHora"));

        TableColumn<Pedido, Integer> colCant = new TableColumn<>("Cant");
        colCant.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        // Columnas calculadas para mostrar Articulo y Cliente
        TableColumn<Pedido, String> colArt = new TableColumn<>("Artículo");
        colArt.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getArticulo().getCodigo()));

        TableColumn<Pedido, String> colCli = new TableColumn<>("Cliente");
        colCli.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCliente().getNif()));

        tabla.getColumns().addAll(colNum, colFecha, colCant, colArt, colCli);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Cargar datos iniciales
        actualizarTabla("TODOS", null);

        root.setLeft(form);
        root.setCenter(tabla);
        BorderPane.setMargin(tabla, new Insets(0, 0, 0, 15));

        return root;
    }

    // Diálogo para crear Cliente + Pedido en una sola transacción
    private void abrirDialogoTransaccionAtomica(int numPed, int cant, LocalDateTime fecha, String codArt, String nif) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Cliente Nuevo Detectado");
        dialog.setHeaderText("El cliente " + nif + " no existe.\nRellene datos para crearlo junto al pedido.");

        ButtonType btnGuardar = new ButtonType("Guardar Todo", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        VBox content = new VBox(10);
        TextField tfNom = new TextField(); tfNom.setPromptText("Nombre Cliente");
        TextField tfDom = new TextField(); tfDom.setPromptText("Domicilio");
        TextField tfEmail = new TextField(); tfEmail.setPromptText("Email");
        CheckBox chkPremium = new CheckBox("Es Premium?");

        content.getChildren().addAll(new Label("Datos del Cliente:"), tfNom, tfDom, tfEmail, chkPremium);
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                int tipo = chkPremium.isSelected() ? 2 : 1;
                return cp.addPedidoYClienteAtomico(numPed, cant, fecha, codArt, nif,
                        tfNom.getText(), tfDom.getText(), tfEmail.getText(), tipo);
            }
            return null;
        });

        Optional<Boolean> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.get()) {
                mostrarAlerta("Éxito", "Transacción completada: Cliente y Pedido creados.");
                // Refrescamos la tabla tras el éxito
                actualizarTabla("TODOS", null);
            } else {
                mostrarAlerta("Error", "Falló la creación (revise datos o conexión).");
            }
        }
    }

    private void actualizarTabla(String tipo, String filtroNif) {
        tabla.getItems().clear();
        if (filtroNif == null) filtroNif = "";

        if ("TODOS".equals(tipo)) {
            // Este método requiere haber editado ControladorPedidos.java
            if (cp.getTodosLosPedidos() != null)
                tabla.setItems(FXCollections.observableArrayList(cp.getTodosLosPedidos()));

        } else if ("PENDIENTES".equals(tipo)) {
            if (cp.getPedidosPendientes(filtroNif) != null)
                tabla.setItems(FXCollections.observableArrayList(cp.getPedidosPendientes(filtroNif)));

        } else if ("ENVIADOS".equals(tipo)) {
            if (cp.getPedidosEnviados(filtroNif) != null)
                tabla.setItems(FXCollections.observableArrayList(cp.getPedidosEnviados(filtroNif)));
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