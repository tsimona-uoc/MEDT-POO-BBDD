package MEDT.MEDT.vista.Cliente;

import MEDT.MEDT.Factory.MEDTFactory;
import MEDT.MEDT.controlador.IControladorArticulos;
import MEDT.MEDT.controlador.IControladorClientes;
import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.ClientePremium;
import MEDT.MEDT.vista.Articulo.Operaciones.EditarArticulo;
import MEDT.MEDT.vista.Cliente.Operaciones.EditarCliente;
import MEDT.MEDT.vista.Eventos.TabCloseEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class Clientes {

    /// Componentes
    @FXML
    private TableView<Cliente> tablaClientes;

    @FXML
    private Button btnFilter;

    @FXML
    private Button btnReload;

    @FXML
    private TableColumn<Cliente, String> colNif;

    @FXML
    private TableColumn<Cliente, String> colName;

    @FXML
    private TableColumn<Cliente, String> colTipoCliente;

    @FXML
    private Button btnDelete;

    @FXML
    private Tab tabDatosClientes;

    @FXML
    private TabPane operacionesClientes;

    /// Controlador clientes
    private IControladorClientes controladorClientes;

    /// Store the operation counter to assign tab name
    private int addOperationCounter;

    @FXML
    private void initialize(){

        /// Get controlador articulos controller
        this.controladorClientes = MEDTFactory.resolve(IControladorClientes.class);

        /// Set column resolver
        colNif.setCellValueFactory(new PropertyValueFactory<>("nif"));
        colName.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTipoCliente.setCellValueFactory(cellData -> {
            Cliente cliente = cellData.getValue();
            String tipo;
            if (cliente instanceof ClientePremium) {
                tipo = "Premium";
            } else if (cliente instanceof ClienteEstandar) {
                tipo = "Estandar";
            } else {
                tipo = "N/A";
            }
            return new SimpleStringProperty(tipo);
        });

        /// Bind enable state of the "delete" button to be only active is selection is not empty
        btnDelete.disableProperty().bind(tablaClientes.getSelectionModel().selectedItemProperty().isNull());

        btnReload.disableProperty().bind(operacionesClientes.getSelectionModel().selectedItemProperty().isNotEqualTo(tabDatosClientes));
        btnFilter.disableProperty().bind(operacionesClientes.getSelectionModel().selectedItemProperty().isNotEqualTo(tabDatosClientes));

        /// Initialize operation counter
        this.addOperationCounter = 0;

        /// Handle close tab event
        this.operacionesClientes.addEventHandler(TabCloseEvent.CLOSE_REQUEST, event -> {
            Tab tab = this.operacionesClientes.getSelectionModel().getSelectedItem();
            this.operacionesClientes.getTabs().remove(tab);
            this.OnCargarClientes();
        });

        /// Handle double click on article entry
        this.tablaClientes.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY  && !this.tablaClientes.getSelectionModel().isEmpty()) {
                Cliente cliente = this.tablaClientes.getSelectionModel().getSelectedItem();
                this.openClientTab(cliente.getNif());
            }
        });

        this.OnCargarClientes();
    }


    @FXML
    public void OnCargarClientes(){
        List<Cliente> clientes = this.controladorClientes.getClientes();
        ObservableList<Cliente> items = tablaClientes.getItems();
        /// Limpiar articulos
        items.clear();
        /// Recargar articulos
        items.addAll(clientes);
    }

    @FXML
    public void OnAgregarCliente(){
        try {
            /// Create a new tab
            Tab agregarClienteTab = new Tab("Agregar cliente " + (++this.addOperationCounter));
            agregarClienteTab.setClosable(true);

            /// Load the add item content fxml and sets as content
            FXMLLoader agregarClienteContent = new FXMLLoader(getClass().getResource("/views/Clientes/Operaciones/AgregarCliente.fxml"));
            agregarClienteTab.setContent(agregarClienteContent.load()); // inyecta el contenido en la pestaña

            /// Add the last tab
            operacionesClientes.getTabs().add(agregarClienteTab);
            operacionesClientes.getSelectionModel().select(agregarClienteTab);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void OnEliminarCliente(){
        if (this.tablaClientes.getSelectionModel().isEmpty()){
            return;
        }

        Cliente selectedClient = this.tablaClientes.getSelectionModel().getSelectedItem();

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmación");
        confirmAlert.setHeaderText("¿Desea eliminar el cliente " +  selectedClient.getNif() + " - " + selectedClient.getNombre() + "?");
        confirmAlert.setContentText("Esto eliminará todos los registros relacionados y no se puede deshacer");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        /// Remove the article
        if (this.controladorClientes.eliminarCliente(selectedClient.getNif())){
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setHeaderText(null);
            infoAlert.setTitle("Operacion completada");
            infoAlert.setContentText("Se ha eliminado el cliente " + selectedClient.getNif() + " - " + selectedClient.getNombre() + " correctamente.");
            infoAlert.showAndWait();
        }
        else{
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Error al eliminar articulo");
            errorAlert.setContentText("No se ha podido eliminar el cliente " + selectedClient.getNif() + " - " + selectedClient.getNombre() + ".");
            errorAlert.showAndWait();
        }

        /// Refresh articles table
        this.OnCargarClientes();
    }

    private void openClientTab(String nif){
        try {
            Cliente cliente = this.controladorClientes.getCliente(nif);

            if (cliente == null){
                /// TODO: Mostrar mensaje de error
                return;
            }

            /// Create a new tab
            Tab editarClienteTab = new Tab("Editar cliente " + cliente.getNombre() + " - " +  cliente.getNombre());
            editarClienteTab.setClosable(true);

            /// Load the add item content fxml and sets as content
            FXMLLoader editarClienteContent = new FXMLLoader(getClass().getResource("/views/Clientes/Operaciones/EditarCliente.fxml"));
            editarClienteTab.setContent(editarClienteContent.load()); // inyecta el contenido en la pestaña

            /// Get controller
            EditarCliente controller = editarClienteContent.getController();

            /// Set visualization data
            controller.setData(cliente);

            /// Add the last tab
            operacionesClientes.getTabs().add(editarClienteTab);
            operacionesClientes.getSelectionModel().select(editarClienteTab);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void OnFiltrarClientes(){
        try {
            // 1. Cargar el FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Clientes/Modals/FilterClients.fxml"));
            Parent root = loader.load();

            // 2. Crear el Stage
            Stage modalStage = new Stage();
            modalStage.setTitle("Mi Modal");

            // 3. Configurar como modal (bloquea la ventana padre)
            modalStage.initModality(Modality.APPLICATION_MODAL);

            // 4. Asignar la escena
            Scene scene = new Scene(root);
            modalStage.setScene(scene);

            // 5. Mostrar el modal y esperar a que se cierre
            modalStage.showAndWait();

            // Opcional: obtener datos del controlador del modal
            // ModalController controller = loader.getController();
            // controller.getResultado();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
