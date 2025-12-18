package MEDT.MEDT.vista.Pedidos.Modals;

import MEDT.MEDT.Factory.MEDTFactory;
import MEDT.MEDT.controlador.IControladorArticulos;
import MEDT.MEDT.controlador.IControladorClientes;
import MEDT.MEDT.controlador.IControladorPedidos;
import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.modelo.Cliente;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.Optional;

public class FilterPedidos {

    private IControladorArticulos controladorArticulos;
    private IControladorClientes controladorClientes;
    private IControladorPedidos controladorPedidos;

    @FXML
    private AnchorPane root;

    @FXML
    private ChoiceBox<Articulo> articuloChoice;

    @FXML
    private ComboBox<Cliente> clienteComboBox;
    @FXML
    private ToggleGroup pedidoCancelableGroup;
    @FXML
    private RadioButton pedidoCancelableNA;
    @FXML
    private RadioButton pedidoCancelableSI;
    @FXML
    private RadioButton pedidoCancelableNO;

    public class Data {
        public Articulo articulo;
        public Cliente cliente;
        public Optional<Boolean> pedidoCancelable;
    }

    /// Filter data
    private Data filterData;

    public void setFilterData(Data data){
        this.filterData = data;

        if (this.filterData != null) {
            this.articuloChoice.setValue(filterData.articulo);
            this.clienteComboBox.setValue(filterData.cliente);

            if (this.filterData.pedidoCancelable.isEmpty()) {
                this.pedidoCancelableNA.setSelected(true);
            }
            else{
                if (this.filterData.pedidoCancelable.get()){
                    this.pedidoCancelableSI.setSelected(true);
                }
                else{
                    this.pedidoCancelableNO.setSelected(true);
                }
            }
        }
        else{
            this.articuloChoice.setValue(null);
            this.clienteComboBox.setValue(null);
            this.pedidoCancelableNA.setSelected(true);
        }
    }

    /// Get filter data
    public Data getFilterData(){
        return filterData;
    }

    @FXML
    private void initialize(){
        this.controladorPedidos = MEDTFactory.resolve(IControladorPedidos.class);
        this.controladorArticulos = MEDTFactory.resolve(IControladorArticulos.class);
        this.controladorClientes = MEDTFactory.resolve(IControladorClientes.class);

        this.filterData = null;

        /// Add all articles options
        articuloChoice.getItems().add(null);
        articuloChoice.getItems().addAll(this.controladorArticulos.getArticulos().stream().toList());

        /// Create a converter to display code and description
        StringConverter<Articulo> articuloConverter = new StringConverter<Articulo>() {

            @Override
            public String toString(Articulo articulo) {
                if (articulo == null) {
                    return null;
                }
                // Aquí defines la concatenación deseada
                return articulo.getCodigo() + " - " + articulo.getDescripcion();
            }

            @Override
            public Articulo fromString(String string) {
                return null;
            }
        };

        /// Set article converter
        articuloChoice.setConverter(articuloConverter);

        clienteComboBox.setEditable(true);

        /// Add clientes to combo box
        clienteComboBox.getItems().addAll(this.controladorClientes.getClientes().stream().toList());

        /// Create a converter to display code and description
        StringConverter<Cliente> clientesConverter = new StringConverter<>() {

            @Override
            public String toString(Cliente cliente) {
                if (cliente == null) {
                    return "";
                }
                return cliente.getNif() + " - " + cliente.getNombre();
            }

            @Override
            public Cliente fromString(String newValue) {

                if (newValue == null || newValue.trim().isEmpty()) {
                    return null;
                }

                final String NIF_CIF_REGEX_ESTRUCTURA = "^(\\d{8}[A-Z])(\\s*-\\s*.*)?$";
                final String busqueda = newValue.toUpperCase();

                if (!busqueda.matches(NIF_CIF_REGEX_ESTRUCTURA)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Valor invalido");
                    alert.setContentText("El criterio de busqueda debe contener al menos un NIF/CIF.");
                    alert.showAndWait();
                    clienteComboBox.getEditor().setText("");
                    return null;
                }

                String nif = busqueda.substring(0, 9);
                Cliente cliente = controladorClientes.getCliente(nif);

                if (cliente == null){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Error de búsqueda");
                    alert.setContentText("No se ha encontrado ningún cliente con el NIF/CIF " + nif);
                    alert.showAndWait();
                    clienteComboBox.getEditor().setText("");
                    return null;
                }

                return cliente;
            }
        };

        clienteComboBox.setConverter(clientesConverter);
    }

    public void OnAplicarFiltros(){

        if (articuloChoice.getValue() == null && clienteComboBox.getValue() == null && this.pedidoCancelableNA.isSelected()){
            filterData = null;
        }
        else{
            filterData = new Data();
            filterData.articulo = articuloChoice.getValue();
            filterData.cliente = clienteComboBox.getValue();
            filterData.pedidoCancelable = this.pedidoCancelableNA.isSelected() ? Optional.empty() : Optional.of(this.pedidoCancelableSI.isSelected());
        }

        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    public void OnBorrarFiltros(){
        this.filterData = null;
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }
}
