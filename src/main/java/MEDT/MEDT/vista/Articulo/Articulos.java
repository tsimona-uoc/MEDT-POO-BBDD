package MEDT.MEDT.vista.Articulo;

import MEDT.MEDT.Factory.MEDTFactory;
import MEDT.MEDT.controlador.IControladorArticulos;
import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.vista.App;
import MEDT.MEDT.vista.Articulo.Operaciones.EditarArticulo;
import MEDT.MEDT.vista.Eventos.TabCloseEvent;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class Articulos {

    /// Componentes
    @FXML
    private TableView<Articulo> tablaArticulos;

    @FXML
    private Button btnReload;

    @FXML
    private TableColumn<Articulo, String> colCode;

    @FXML
    private TableColumn<Articulo, String> colDesc;

    @FXML
    private Button btnDelete;

    @FXML
    private Tab tabDatosArticulos;

    @FXML
    private TabPane operacionesArticulos;

    /// Controlador articulos
    private IControladorArticulos controladorArticulos;

    /// Store the operation counter to assign tab name
    private int addOperationCounter;

    @FXML
    private void initialize(){

        /// Get controlador articulos controller
        this.controladorArticulos = MEDTFactory.resolve(IControladorArticulos.class);

        /// Set column resolver
        colCode.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        /// Bind enable state of the "delete" button to be only active is selection is not empty
        btnDelete.disableProperty().bind(tablaArticulos.getSelectionModel().selectedItemProperty().isNull());
        btnReload.disableProperty().bind(operacionesArticulos.getSelectionModel().selectedItemProperty().isNotEqualTo(tabDatosArticulos));

        /// Initialize operation counter
        this.addOperationCounter = 0;

        /// Handle close tab event
        this.operacionesArticulos.addEventHandler(TabCloseEvent.CLOSE_REQUEST, event -> {
            Tab tab = this.operacionesArticulos.getSelectionModel().getSelectedItem();
            this.operacionesArticulos.getTabs().remove(tab);
            this.OnCargarArticulos();
        });

        /// Handle double click on article entry
        this.tablaArticulos.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY  && !this.tablaArticulos.getSelectionModel().isEmpty()) {
                Articulo articulo = this.tablaArticulos.getSelectionModel().getSelectedItem();
                this.openArticleTab(articulo.getCodigo());
            }
        });

        this.OnCargarArticulos();
    }


    @FXML
    public void OnCargarArticulos(){
        List<Articulo> articulos = this.controladorArticulos.getArticulos();
        ObservableList<Articulo> items = tablaArticulos.getItems();
        /// Limpiar articulos
        items.clear();
        /// Recargar articulos
        items.addAll(articulos);
    }

    @FXML
    public void OnAgregarArticulo(){
        try {
            /// Create a new tab
            Tab agregarArticuloTab = new Tab("Agregar Articulo " + (++this.addOperationCounter));
            agregarArticuloTab.setClosable(true);

            /// Load the add item content fxml and sets as content
            FXMLLoader agregarArticuloContent = new FXMLLoader(getClass().getResource("/views/Articulos/Operaciones/AgregarArticulo.fxml"));
            agregarArticuloTab.setContent(agregarArticuloContent.load()); // inyecta el contenido en la pestaña

            /// Add the last tab
            operacionesArticulos.getTabs().add(agregarArticuloTab);
            operacionesArticulos.getSelectionModel().select(agregarArticuloTab);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void OnEliminarArticulo(){
        if (this.tablaArticulos.getSelectionModel().isEmpty()){
            return;
        }

        Articulo selectedArticle = this.tablaArticulos.getSelectionModel().getSelectedItem();

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmación");
        confirmAlert.setHeaderText("¿Desea eliminar el articulo " +  selectedArticle.getCodigo() + "?");
        confirmAlert.setContentText("Esto eliminará todos los registros relacionados y no se puede deshacer");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        /// Remove the article
        if (this.controladorArticulos.eliminarArticulo(selectedArticle.getCodigo())){
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setHeaderText(null);
            infoAlert.setTitle("Operacion completada");
            infoAlert.setContentText("Se ha eliminado el articulo " + selectedArticle.getDescripcion() + " correctamente.");
            infoAlert.showAndWait();
        }
        else{
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Error al eliminar articulo");
            errorAlert.setContentText("No se ha podido eliminar el articulo " + selectedArticle.getDescripcion() + ".");
            errorAlert.showAndWait();
        }

        /// Refresh articles table
        this.OnCargarArticulos();
    }

    public void openArticleTab(String codigoArticulo){
        try {
            Articulo articulo = this.controladorArticulos.getArticulo(codigoArticulo);

            if (articulo == null){
                /// TODO: Mostrar mensaje de error
                return;
            }

            /// Create a new tab
            Tab visualizarArticuloTab = new Tab("Editar articulo " + codigoArticulo);
            visualizarArticuloTab.setClosable(true);

            /// Load the add item content fxml and sets as content
            FXMLLoader visualizeArticuloContent = new FXMLLoader(getClass().getResource("/views/Articulos/Operaciones/EditarArticulo.fxml"));
            visualizarArticuloTab.setContent(visualizeArticuloContent.load()); // inyecta el contenido en la pestaña

            /// Get controller
            EditarArticulo controller = visualizeArticuloContent.getController();

            /// Set visualization data
            controller.setData(articulo);

            /// Add the last tab
            operacionesArticulos.getTabs().add(visualizarArticuloTab);
            operacionesArticulos.getSelectionModel().select(visualizarArticuloTab);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
