package MEDT.MEDT.vista.Pedidos;

import MEDT.MEDT.Factory.MEDTFactory;
import MEDT.MEDT.controlador.IControladorPedidos;
import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.ClientePremium;
import MEDT.MEDT.modelo.Pedido;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;
import MEDT.MEDT.vista.Cliente.Modals.FilterClients;
import MEDT.MEDT.vista.Cliente.Operaciones.EditarCliente;
import MEDT.MEDT.vista.Eventos.TabCloseEvent;
import MEDT.MEDT.vista.Pedidos.Modals.FilterPedidos;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Pedidos {

    /// Componentes

    @FXML
    private Label filterLabel;

    @FXML
    private TableView<Pedido> tablaPedidos;

    @FXML
    private Button btnFilter;

    @FXML
    private Button btnReload;

    @FXML
    private TableColumn<Pedido, String> colCodigo;

    @FXML
    private TableColumn<Pedido, String> colArticulo;

    @FXML
    private TableColumn<Pedido, String> colCantidad;

    @FXML
    private TableColumn<Pedido, String> colFechaHora;

    @FXML
    private TableColumn<Pedido, String> colNIFCliente;

    @FXML
    private Button btnDelete;

    @FXML
    private Tab tabDatosPedidos;

    @FXML
    private TabPane operacionesPedidos;

    /// Controlador pedido
    private IControladorPedidos controladorPedidos;

    /// Store the operation counter to assign tab name
    private int addOperationCounter;

    /// Filter data
    public ObjectProperty<FilterPedidos.Data> filterData;

    @FXML
    private void initialize(){

        /// Get controlador articulos controller
        this.controladorPedidos = MEDTFactory.resolve(IControladorPedidos.class);

        /// Set column resolver
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("numPedido"));
        colArticulo.setCellValueFactory(cellData -> {
            Pedido pedido = cellData.getValue();
            return new SimpleStringProperty(pedido.getArticulo().getCodigo());
        });
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colFechaHora.setCellValueFactory(cellData -> {
            Pedido pedido = cellData.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return new SimpleObjectProperty<>(pedido.getFechaHora().format(formatter));
        });
        colNIFCliente.setCellValueFactory(cellData -> {
            Pedido pedido = cellData.getValue();
            return new SimpleStringProperty(pedido.getCliente().getNif());
        });

        /// Bind enable state of the "delete" button to be only active is selection is not empty
        btnDelete.disableProperty().bind(tablaPedidos.getSelectionModel().selectedItemProperty().isNull());

        btnReload.disableProperty().bind(operacionesPedidos.getSelectionModel().selectedItemProperty().isNotEqualTo(tabDatosPedidos));
        btnFilter.disableProperty().bind(operacionesPedidos.getSelectionModel().selectedItemProperty().isNotEqualTo(tabDatosPedidos));

        /// Initialize operation counter
        this.addOperationCounter = 0;

        /// Handle close tab event
        this.operacionesPedidos.addEventHandler(TabCloseEvent.CLOSE_REQUEST, event -> {
            Tab tab = this.operacionesPedidos.getSelectionModel().getSelectedItem();
            this.operacionesPedidos.getTabs().remove(tab);
            this.OnCargarPedidos();
        });

        /// Handle double click on article entry
        this.tablaPedidos.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY  && !this.tablaPedidos.getSelectionModel().isEmpty()) {
                Pedido pedido = this.tablaPedidos.getSelectionModel().getSelectedItem();
                this.openPedidoTab(pedido.getNumPedido());
            }
        });

        this.filterData = new SimpleObjectProperty<>();

        this.filterLabel.textProperty().bind(
                Bindings.when(this.filterData.isNull())
                        .then("")
                        .otherwise("(filtro activado)")
        );

        this.OnCargarPedidos();
    }


    @FXML
    public void OnCargarPedidos(){
        List<Pedido> pedidos = this.controladorPedidos.getPedidos();

        if (!this.filterData.isNull().get()){
            pedidos = this.aplicarFiltros(pedidos, this.filterData.get());
        }

        ObservableList<Pedido> items = tablaPedidos.getItems();
        /// Limpiar pedidos
        items.clear();
        /// Recargar articulos
        items.addAll(pedidos);
    }

    @FXML
    public void OnAgregarPedido(){
        try {
            /// Create a new tab
            Tab agregarClienteTab = new Tab("Agregar pedido " + (++this.addOperationCounter));
            agregarClienteTab.setClosable(true);

            /// Load the add item content fxml and sets as content
            FXMLLoader agregarPedidocontent = new FXMLLoader(getClass().getResource("/views/Pedidos/Operaciones/AgregarPedido.fxml"));
            agregarClienteTab.setContent(agregarPedidocontent.load()); // inyecta el contenido en la pestaña

            /// Add the last tab
            operacionesPedidos.getTabs().add(agregarClienteTab);
            operacionesPedidos.getSelectionModel().select(agregarClienteTab);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void OnEliminarPedido(){
        if (this.tablaPedidos.getSelectionModel().isEmpty()){
            return;
        }

        Pedido selectedPedido = this.tablaPedidos.getSelectionModel().getSelectedItem();

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmación");
        confirmAlert.setHeaderText("¿Desea eliminar el pedido " +  selectedPedido.getNumPedido() + "?");
        confirmAlert.setContentText("Esto eliminará todos los registros relacionados y no se puede deshacer");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        /// Remove the article
        try {
            int resultado = this.controladorPedidos.eliminarPedido(selectedPedido.getNumPedido());

            if (resultado == 0){
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setHeaderText(null);
                infoAlert.setTitle("Operacion completada");
                infoAlert.setContentText("Se ha eliminado el pedido " + selectedPedido.getNumPedido() + " correctamente.");
                infoAlert.showAndWait();
            }
            else{
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText(null);
                errorAlert.setTitle("Error al eliminar pedido");

                switch (resultado){
                    case 1:
                        errorAlert.setContentText("No se ha encontrado el pedido " + selectedPedido.getNumPedido() + ".");
                        break;
                    case 2:
                        errorAlert.setContentText("El pedido " + selectedPedido.getNumPedido() + " no es cancelable.");
                        break;
                }

                errorAlert.showAndWait();
            }
        } catch (PedidoNoCancelableException e) {
            throw new RuntimeException(e);
        }

        /// Refresh articles table
        this.OnCargarPedidos();
    }

    private void openPedidoTab(int numeroPedido){

        /*
        try {
            Pedido pedido = this.controladorPedidos.getPedido(numeroPedido);

            if (pedido == null){
                /// TODO: Mostrar mensaje de error
                return;
            }

            /// Create a new tab
            Tab editarPedidoTab = new Tab("Editar pedido " + pedido.getNumPedido() + " - " +  pedido.getArticulo().getCodigo());
            editarPedidoTab.setClosable(true);

            /// Load the add item content fxml and sets as content
            FXMLLoader editarPedidoContent = new FXMLLoader(getClass().getResource("/views/Pedidos/Operaciones/EditarPedido.fxml"));
            editarPedidoTab.setContent(editarPedidoContent.load()); // inyecta el contenido en la pestaña

            /// Get controller
            /// TODO: Create EditarPedido controller
            /// EditarPedido controller = editarPedidoContent.getController();

            /// Set visualization data
            /// controller.setData(cliente);

            /// Add the last tab
            TODO: Open edit Pedido tab
            operacionesClientes.getTabs().add(editarClienteTab);
            operacionesClientes.getSelectionModel().select(editarClienteTab);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        */
    }

    public void OnFiltrarPedidos(){
        try {
            // 1. Cargar el FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Pedidos/Modals/FilterPedidos.fxml"));
            Parent root = loader.load();

            // 2. Crear el Stage
            Stage modalStage = new Stage();
            modalStage.setTitle("Filtrar clientes");
            modalStage.setResizable(false);

            // 3. Configurar como modal (bloquea la ventana padre)
            modalStage.initModality(Modality.APPLICATION_MODAL);

            // 4. Asignar la escena
            Scene scene = new Scene(root);
            modalStage.setScene(scene);

            // 5. Asignar filtro existente
            FilterPedidos controller = loader.getController();
            controller.setFilterData(this.filterData.get());

            // 6. Mostrar el modal y esperar a que se cierre
            modalStage.showAndWait();

            // 7. Recuperar datos de filtro
            this.filterData.set(controller.getFilterData());

            // 8. Recargar datos
            this.OnCargarPedidos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Pedido> aplicarFiltros(List<Pedido> pedidos, FilterPedidos.Data filters){
        Stream<Pedido> stream = pedidos.stream();
        /// TODO: Apply filters
        return stream.toList();
    }
}
