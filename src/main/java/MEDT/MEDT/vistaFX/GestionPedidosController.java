package MEDT.MEDT.vistaFX;

import MEDT.MEDT.DAO.IClienteDAO;
import MEDT.MEDT.DAO.IPedidoDAO;
import MEDT.MEDT.Factory.MEDTFactory;
import MEDT.MEDT.controlador.ControladorPedidos;
import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.Pedido;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class GestionPedidosController {

    // --- Componentes FXML ---
    @FXML
    private TextField numPedidoField;
    @FXML
    private TextField nifClienteField;
    @FXML
    private TextField codigoArticuloField;
    @FXML
    private TextField cantidadField;
    @FXML
    private DatePicker fechaPicker;
    @FXML
    private TextField horaField;


    @FXML
    private ComboBox<String> filterStatusComboBox;
    @FXML
    private TextField filterClienteField;

    @FXML
    private TableView<Pedido> pedidosTable;
    @FXML
    private TableColumn<Pedido, Integer> numPedidoColumn;
    @FXML
    private TableColumn<Pedido, String> clienteColumn;
    @FXML
    private TableColumn<Pedido, String> articuloColumn;
    @FXML
    private TableColumn<Pedido, Integer> cantidadColumn;
    @FXML
    private TableColumn<Pedido, String> fechaHoraColumn;
    @FXML
    private TableColumn<Pedido, String> precioTotalColumn;

    // --- Lógica de Negocio ---
    private ControladorPedidos controladorPedidos;
    private final ObservableList<Pedido> pedidosData = FXCollections.observableArrayList();
    private RootLayoutController rootLayoutController;


    @FXML
    private void initialize() {
        // 1. Inicializar el controlador de negocio
        controladorPedidos = new ControladorPedidos(
                MEDTFactory.resolve(MEDT.MEDT.DAO.IArticuloDAO.class),
                MEDTFactory.resolve(IPedidoDAO.class),
                MEDTFactory.resolve(IClienteDAO.class)
        );

        // 2. Configurar el ComboBox de filtro
        filterStatusComboBox.getItems().addAll("Enviados" ,"Pendientes");
        filterStatusComboBox.setValue("Enviados");

        // 3. Añadir listeners para actualizar la tabla automáticamente
        filterStatusComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> loadPedidosData());
        filterClienteField.textProperty().addListener((obs, oldVal, newVal) -> loadPedidosData());

        // 4. Configurar las columnas de la tabla
        setupTableColumns();

        // 5. Cargar los datos iniciales
        loadPedidosData();
    }

    private void setupTableColumns() {
        numPedidoColumn.setCellValueFactory(new PropertyValueFactory<>("numPedido"));
        cantidadColumn.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        precioTotalColumn.setCellValueFactory(cellData -> {
            double precio = cellData.getValue().calcularPrecioTotal();
            String precioFormateado = String.format("%.2f €", precio);
            return new SimpleStringProperty(precioFormateado);
        });

        clienteColumn.setCellValueFactory(cellData -> {
            Cliente cliente = cellData.getValue().getCliente();
            return new SimpleStringProperty(cliente != null ? cliente.getNif() : "N/A");
        });

        articuloColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getArticulo() != null) {
                return new SimpleStringProperty(cellData.getValue().getArticulo().getDescripcion());
            }
            return new SimpleStringProperty("N/A");
        });

        fechaHoraColumn.setCellValueFactory(cellData -> {
            LocalDateTime fecha = cellData.getValue().getFechaHora();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return new SimpleStringProperty(fecha.format(formatter));
        });
    }

    private void loadPedidosData() {
        List<Pedido> pedidos = new ArrayList<>();
        String statusFilter = filterStatusComboBox.getValue();
        String clienteNif = filterClienteField.getText();
        switch (statusFilter) {
            case "Pendientes":
                pedidos = controladorPedidos.getPedidosPendientes(clienteNif);
                break;

            case "Enviados":
                pedidos = controladorPedidos.getPedidosEnviados(clienteNif);
                break;
        }

        pedidosData.clear();
        if (pedidos != null) {
            pedidosData.addAll(pedidos);
        }

        pedidosTable.setItems(pedidosData);

    }

    @FXML
    private void handleAddPedido() {
        try {
            int numPedido = Integer.parseInt(numPedidoField.getText());
            String nifCliente = nifClienteField.getText();
            String codigoArticulo = codigoArticuloField.getText();
            int cantidad = Integer.parseInt(cantidadField.getText());
            if (fechaPicker.getValue() == null || horaField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Datos incompletos", "Debes seleccionar una fecha y una hora.");
                return;
            }
            LocalDate fechaParte = fechaPicker.getValue();
            LocalTime horaParte = LocalTime.parse(horaField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalDateTime fechaHoraCompleta = LocalDateTime.of(fechaParte, horaParte);

            String resultado = controladorPedidos.addPedido(numPedido, cantidad, fechaHoraCompleta, codigoArticulo, nifCliente);
            // Muestra el resultado en un diálogo de alerta
            showAlert(Alert.AlertType.INFORMATION, "Resultado", resultado);

            if (resultado.contains("cliente no existe")) {
                // En lugar de una alerta, llamamos al mediador
                if (rootLayoutController != null) {
                    rootLayoutController.switchToClientesTabAndSetNif(nifCliente);
                }
            } else
                // Si el pedido se añadió correctamente, recarga la tabla y limpia los campos
            if (resultado.contains("correctamente")) {
                loadPedidosData();
                clearFields();
            }

        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Formato Incorrecto", "La hora debe tener el formato HH:mm (ej: 14:30)");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Formato", "El número de pedido y cantidad deben ser números.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error inesperado: " + e.getMessage());
        }

    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleDeletePedido() {
        Pedido selectedPedido = pedidosTable.getSelectionModel().getSelectedItem();

        if (selectedPedido == null) {
            showAlert(Alert.AlertType.WARNING, "Ningún pedido seleccionado", "Por favor, selecciona un pedido de la tabla para eliminarlo.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmar Eliminación");
        confirmationAlert.setHeaderText("¿Estás seguro de que quieres eliminar el pedido número " + selectedPedido.getNumPedido() + "?");
        confirmationAlert.setContentText("Esta acción no se puede deshacer.");

        // showAndWait() espera a que el usuario haga clic en un botón
        if (confirmationAlert.showAndWait().get() == ButtonType.OK) {
            try {
                boolean success = controladorPedidos.eliminarPedido(selectedPedido.getNumPedido());

                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Éxito", "Pedido eliminado correctamente.");
                    loadPedidosData();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el pedido por un error desconocido.");
                }

            } catch (PedidoNoCancelableException e) {
                showAlert(Alert.AlertType.ERROR, "Operación no permitida", "Este pedido ya ha sido enviado y no se puede eliminar.");
            } catch (Exception e) {
                // Captura para cualquier otro error inesperado
                showAlert(Alert.AlertType.ERROR, "Error Inesperado", "Ha ocurrido un error: " + e.getMessage());
            }
        }
    }

    public void setRootLayoutController(RootLayoutController rootLayoutController) {
        this.rootLayoutController = rootLayoutController;
    }

    private void clearFields() {
        numPedidoField.clear();
        nifClienteField.clear();
        codigoArticuloField.clear();
        cantidadField.clear();
        fechaPicker.setValue(null);
        horaField.clear();
    }
}
    