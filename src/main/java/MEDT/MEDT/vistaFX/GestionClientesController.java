package MEDT.MEDT.vistaFX;

import MEDT.MEDT.DAO.IClienteDAO;
import MEDT.MEDT.Factory.MEDTFactory;
import MEDT.MEDT.controlador.ControladorClientes;
import MEDT.MEDT.modelo.Cliente;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class GestionClientesController {

    // --- Componentes FXML ---
    @FXML
    private TextField nifField;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField domicilioField;
    @FXML
    private TextField emailField;
    @FXML
    private ChoiceBox<String> tipoClienteChoiceBox;
    @FXML
    private TableView<Cliente> clientesTable;
    @FXML
    private TableColumn<Cliente, String> nifColumn;
    @FXML
    private TableColumn<Cliente, String> nombreColumn;
    @FXML
    private TableColumn<Cliente, String> domicilioColumn;
    @FXML
    private TableColumn<Cliente, String> emailColumn;
    @FXML
    private TableColumn<Cliente, String> tipoColumn;

    // Nuevo ComboBox para el filtro
    @FXML
    private ComboBox<String> filterComboBox;

    // --- Lógica de Negocio ---
    private ControladorClientes controladorClientes;
    private final ObservableList<Cliente> clientesData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        controladorClientes = new ControladorClientes(MEDTFactory.resolve(IClienteDAO.class));

        // Configurar ChoiceBox para añadir cliente
        tipoClienteChoiceBox.getItems().addAll("Estándar", "Premium");
        tipoClienteChoiceBox.setValue("Estándar");

        // Configurar ComboBox para el filtro
        filterComboBox.getItems().addAll("Todos", "Estándar", "Premium");
        filterComboBox.setValue("Todos");

        // Añadir un listener para reaccionar a los cambios de selección en el filtro
        filterComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> filterClientes(newValue)
        );

        // Configurar las columnas de la tabla
        setupTableColumns();

        // Cargar los datos iniciales (todos los clientes)
        filterClientes("Todos");
    }

    private void setupTableColumns() {
        nifColumn.setCellValueFactory(new PropertyValueFactory<>("nif"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        domicilioColumn.setCellValueFactory(new PropertyValueFactory<>("domicilio"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        tipoColumn.setCellValueFactory(cellData -> {
            String tipo = cellData.getValue().getTipo();
            if (tipo != null) {
                return new SimpleStringProperty(tipo.substring(0, 1).toUpperCase() + tipo.substring(1));
            } else {
                return new SimpleStringProperty("");
            }
        });
    }

    private void filterClientes(String filter) {
        List<Cliente> filteredList = new ArrayList<>();
        if (filter == null) {
            filter = "Todos";
        }

        switch (filter) {
            case "Estándar":
                filteredList = controladorClientes.getClientesEstandar();
                break;
            case "Premium":
                filteredList = controladorClientes.getClientesPremium();
                break;
            default: // "Todos"
                filteredList = controladorClientes.getClientes();
                break;
        }

        clientesData.clear();
        if (filteredList != null) {
            clientesData.addAll(filteredList);
        }
        clientesTable.setItems(clientesData);
    }

    @FXML
    private void handleAddCliente() {
        if (isInputValid()) {
            String nif = nifField.getText();
            String nombre = nombreField.getText();
            String domicilio = domicilioField.getText();
            String email = emailField.getText();
            String tipo = tipoClienteChoiceBox.getValue();

            boolean success;
            if ("Estándar".equals(tipo)) {
                success = controladorClientes.addClienteEstandar(nombre, domicilio, nif, email);
            } else {
                success = controladorClientes.addClientePremium(nombre, domicilio, nif, email);
            }

            if (success) {
                filterClientes(filterComboBox.getValue()); // Recargar la tabla respetando el filtro actual
                clearFields();
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Cliente añadido correctamente.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error de Base de Datos", "No se pudo añadir el cliente. Es posible que el NIF o el email ya existan.");
            }
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";
        if (nifField.getText() == null || nifField.getText().isEmpty()) {
            errorMessage += "NIF no válido.\n";
        }
        if (nombreField.getText() == null || nombreField.getText().isEmpty()) {
            errorMessage += "Nombre no válido.\n";
        }
        if (domicilioField.getText() == null || domicilioField.getText().isEmpty()) {
            errorMessage += "Domicilio no válido.\n";
        }
        if (emailField.getText() == null || emailField.getText().isEmpty() || !emailField.getText().contains("@")) {
            errorMessage += "Email no válido.\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert(Alert.AlertType.ERROR, "Campos no válidos", "Por favor, corrige los campos no válidos:", errorMessage);
            return false;
        }
    }

    private void clearFields() {
        nifField.clear();
        nombreField.clear();
        domicilioField.clear();
        emailField.clear();
        tipoClienteChoiceBox.setValue("Estándar");
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String... content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        if (content.length > 0) {
            alert.setContentText(content[0]);
        }
        alert.showAndWait();
    }
}