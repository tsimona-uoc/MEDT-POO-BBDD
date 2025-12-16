package MEDT.vista.fx;

import MEDT.MEDT.controlador.ControladorClientes;
import MEDT.MEDT.controlador.ControladorPedidos;
import MEDT.MEDT.modelo.Pedido;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PedidosView {

    private final Stage stage;
    private final ControladorPedidos controladorPedidos;
    private final ControladorClientes controladorClientes;
    private final MenuPrincipalView menuPrincipal;

    private final Scene scene;
    private final TableView<Pedido> table = new TableView<>();

    // Filtro
    private final TextField txtFiltroNif = new TextField();

    public PedidosView(Stage stage,
                       ControladorPedidos controladorPedidos,
                       ControladorClientes controladorClientes,
                       MenuPrincipalView menuPrincipal) {

        this.stage = stage;
        this.controladorPedidos = controladorPedidos;
        this.controladorClientes = controladorClientes;
        this.menuPrincipal = menuPrincipal;

        BorderPane root = new BorderPane();
        root.setTop(crearZonaAcciones());
        root.setCenter(crearTabla());
        root.setBottom(crearBotonVolver());

        this.scene = new Scene(root, 900, 550);
    }

    public Scene getScene() {
        return scene;
    }

    // =========================
    // UI
    // =========================

    private VBox crearZonaAcciones() {
        VBox contenedor = new VBox(10);
        contenedor.setStyle("-fx-padding: 15;");

        // --- Añadir Pedido ---
        TitledPane paneAdd = new TitledPane();
        paneAdd.setText("Añadir pedido");
        paneAdd.setCollapsible(false);
        paneAdd.setContent(crearFormularioAddPedido());

        // --- Eliminar Pedido ---
        TitledPane paneDelete = new TitledPane();
        paneDelete.setText("Eliminar pedido");
        paneDelete.setCollapsible(false);
        paneDelete.setContent(crearFormularioEliminar());

        // --- Consultas ---
        TitledPane paneQuery = new TitledPane();
        paneQuery.setText("Consultas");
        paneQuery.setCollapsible(false);
        paneQuery.setContent(crearZonaConsultas());

        contenedor.getChildren().addAll(paneAdd, paneDelete, paneQuery);
        return contenedor;
    }

    private GridPane crearFormularioAddPedido() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField txtNumPedido = new TextField();
        TextField txtCantidad = new TextField();
        TextField txtCodigoArticulo = new TextField();
        TextField txtNifCliente = new TextField();

        // Fecha/hora: DatePicker + TextField hora
        DatePicker dpFecha = new DatePicker(LocalDate.now());
        TextField txtHora = new TextField("12:00"); // HH:mm

        Button btnCrear = new Button("Crear pedido");

        int r = 0;
        grid.add(new Label("Número pedido:"), 0, r); grid.add(txtNumPedido, 1, r++);
        grid.add(new Label("Cantidad:"), 0, r);      grid.add(txtCantidad, 1, r++);
        grid.add(new Label("Código artículo:"), 0, r); grid.add(txtCodigoArticulo, 1, r++);
        grid.add(new Label("NIF cliente:"), 0, r);     grid.add(txtNifCliente, 1, r++);

        grid.add(new Label("Fecha:"), 0, r);         grid.add(dpFecha, 1, r++);
        grid.add(new Label("Hora (HH:mm):"), 0, r);  grid.add(txtHora, 1, r++);

        grid.add(btnCrear, 1, r);

        btnCrear.setOnAction(e -> {
            try {
                int numPedido = Integer.parseInt(txtNumPedido.getText().trim());
                int cantidad = Integer.parseInt(txtCantidad.getText().trim());
                String codArt = txtCodigoArticulo.getText().trim();
                String nif = txtNifCliente.getText().trim();

                LocalDate fecha = dpFecha.getValue();
                LocalTime hora = LocalTime.parse(txtHora.getText().trim(), DateTimeFormatter.ofPattern("HH:mm"));
                LocalDateTime fechaHora = LocalDateTime.of(fecha, hora);

                // Si el cliente NO existe -> pedimos datos y llamamos a la transacción atómica que ya tienes
                boolean existe = controladorClientes.existeCliente(nif);
                String msg;

                if (!existe) {
                    ClienteDialog.Result datos = ClienteDialog.pedirDatos();
                    if (datos == null) return; // cancelado

                    boolean ok = controladorPedidos.addPedidoYClienteAtomico(
                            numPedido, cantidad, fechaHora, codArt,
                            nif, datos.nombre, datos.domicilio, datos.email, datos.tipo
                    );

                    msg = ok ? "Pedido y cliente agregados correctamente." : "Error al agregar pedido/cliente.";
                } else {
                    msg = controladorPedidos.addPedido(numPedido, cantidad, fechaHora, codArt, nif);
                }

                mostrarInfo(msg);
                limpiarCampos(txtNumPedido, txtCantidad, txtCodigoArticulo, txtNifCliente);

            } catch (Exception ex) {
                mostrarError("Datos inválidos. Revisa número, cantidad, fecha y hora.");
            }
        });

        return grid;
    }

    private HBox crearFormularioEliminar() {
        HBox box = new HBox(10);
        TextField txtNum = new TextField();
        txtNum.setPromptText("Número de pedido");
        Button btnEliminar = new Button("Eliminar");

        btnEliminar.setOnAction(e -> {
            try {
                int num = Integer.parseInt(txtNum.getText().trim());
                boolean ok = controladorPedidos.eliminarPedido(num);
                if (ok) mostrarInfo("Pedido eliminado correctamente.");
                else mostrarError("No se pudo eliminar (puede que no exista o no sea cancelable).");
            } catch (PedidoNoCancelableException ex) {
                mostrarError("No se puede eliminar: " + ex.getMessage());
            } catch (Exception ex) {
                mostrarError("Número inválido.");
            }
        });

        box.getChildren().addAll(new Label("Pedido:"), txtNum, btnEliminar);
        return box;
    }

    private HBox crearZonaConsultas() {
        HBox box = new HBox(10);
        txtFiltroNif.setPromptText("Filtro NIF (opcional)");
        Button btnPendientes = new Button("Ver pendientes");
        Button btnEnviados = new Button("Ver enviados");
        Button btnTodos = new Button("Ver todos");

        btnPendientes.setOnAction(e -> cargarPendientes(txtFiltroNif.getText()));
        btnEnviados.setOnAction(e -> cargarEnviados(txtFiltroNif.getText()));
        btnTodos.setOnAction(e -> cargarTodos());

        box.getChildren().addAll(new Label("NIF:"), txtFiltroNif, btnPendientes, btnEnviados, btnTodos);
        return box;
    }

    private TableView<Pedido> crearTabla() {
        TableColumn<Pedido, Number> colNum = new TableColumn<>("Nº Pedido");
        colNum.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getNumPedido()));

        TableColumn<Pedido, String> colFecha = new TableColumn<>("Fecha/Hora");
        colFecha.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(
                        d.getValue().getFechaHora() != null ? d.getValue().getFechaHora().toString() : ""
                )
        );

        TableColumn<Pedido, Number> colCant = new TableColumn<>("Cantidad");
        colCant.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getCantidad()));

        TableColumn<Pedido, String> colArt = new TableColumn<>("Artículo");
        colArt.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(
                        d.getValue().getArticulo() != null ? d.getValue().getArticulo().getCodigo() : ""
                )
        );

        TableColumn<Pedido, String> colNif = new TableColumn<>("Cliente (NIF)");
        colNif.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(
                        d.getValue().getCliente() != null ? d.getValue().getCliente().getNif() : ""
                )
        );

        TableColumn<Pedido, Number> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(d ->
                new javafx.beans.property.SimpleDoubleProperty(d.getValue().calcularPrecioTotal())
        );

        table.getColumns().addAll(colNum, colFecha, colCant, colArt, colNif, colTotal);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return table;
    }

    private HBox crearBotonVolver() {
        Button btnVolver = new Button("Volver");
        btnVolver.setOnAction(e -> stage.setScene(menuPrincipal.getScene()));

        HBox box = new HBox(btnVolver);
        box.setStyle("-fx-padding: 10; -fx-alignment: center;");
        return box;
    }

    // =========================
    // Data loaders
    // =========================

    private void cargarPendientes(String nif) {
        List<Pedido> pedidos = controladorPedidos.getPedidosPendientes(nif == null ? "" : nif.trim());
        setTabla(pedidos);
    }

    private void cargarEnviados(String nif) {
        List<Pedido> pedidos = controladorPedidos.getPedidosEnviados(nif == null ? "" : nif.trim());
        setTabla(pedidos);
    }

    private void cargarTodos() {
        // Si quieres “todos”, lo más fácil es pedir pendientes+enviados sin filtro y concatenar
        List<Pedido> pend = controladorPedidos.getPedidosPendientes("");
        List<Pedido> env = controladorPedidos.getPedidosEnviados("");
        pend.addAll(env);
        setTabla(pend);
    }

    private void setTabla(List<Pedido> pedidos) {
        if (pedidos == null) pedidos = List.of();
        ObservableList<Pedido> datos = FXCollections.observableArrayList(pedidos);
        table.setItems(datos);

        if (pedidos.isEmpty()) {
            mostrarInfo("No hay resultados para esa consulta.");
        }
    }

    // =========================
    // Helpers
    // =========================

    private void mostrarError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void mostrarInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Info");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void limpiarCampos(TextField... campos) {
        for (TextField c : campos) c.clear();
    }

    // =========================
    // Dialog para crear cliente si no existe
    // =========================

    private static class ClienteDialog {

        static class Result {
            final String nombre, domicilio, email;
            final int tipo; // 1 estandar, 2 premium
            Result(String n, String d, String e, int t) { nombre=n; domicilio=d; email=e; tipo=t; }
        }

        static Result pedirDatos() {
            Dialog<Result> dialog = new Dialog<>();
            dialog.setTitle("Crear cliente");
            dialog.setHeaderText("El cliente no existe. Introduce sus datos:");

            ButtonType okBtn = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(okBtn, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField txtNombre = new TextField();
            TextField txtDom = new TextField();
            TextField txtEmail = new TextField();
            ComboBox<String> cbTipo = new ComboBox<>();
            cbTipo.getItems().addAll("Estandar", "Premium");
            cbTipo.getSelectionModel().select(0);

            int r=0;
            grid.add(new Label("Nombre:"), 0, r); grid.add(txtNombre, 1, r++);
            grid.add(new Label("Domicilio:"), 0, r); grid.add(txtDom, 1, r++);
            grid.add(new Label("Email:"), 0, r); grid.add(txtEmail, 1, r++);
            grid.add(new Label("Tipo:"), 0, r); grid.add(cbTipo, 1, r++);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(bt -> {
                if (bt == okBtn) {
                    int tipo = cbTipo.getSelectionModel().getSelectedIndex() == 0 ? 1 : 2;
                    return new Result(
                            txtNombre.getText().trim(),
                            txtDom.getText().trim(),
                            txtEmail.getText().trim(),
                            tipo
                    );
                }
                return null;
            });

            return dialog.showAndWait().orElse(null);
        }
    }
}
