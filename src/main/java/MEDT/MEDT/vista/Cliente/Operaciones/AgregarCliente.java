package MEDT.MEDT.vista.Cliente.Operaciones;

import MEDT.MEDT.Factory.MEDTFactory;
import MEDT.MEDT.controlador.IControladorArticulos;
import MEDT.MEDT.controlador.IControladorClientes;
import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.vista.Eventos.OperationCompletedEvent;
import MEDT.MEDT.vista.Eventos.TabCloseEvent;
import MEDT.MEDT.vista.TextFormatters.DecimalFormatter;
import MEDT.MEDT.vista.TextFormatters.IntegerFormatter;
import MEDT.MEDT.vista.TextFormatters.ItemCodeFormatter;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ListBinding;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;


public class AgregarCliente {

    private IControladorClientes controladorClientes;

    @FXML
    private AnchorPane root;

    @FXML
    public TextField tfNif;

    @FXML
    private TextField tfNombre;

    @FXML
    private TextField tfDomicilio;

    @FXML
    private TextField tfEmail;

    @FXML
    private ChoiceBox<String> tipoCliente;

    @FXML
    private Button btnAceptar;

    /// Operation completed
    private boolean operacionCompletada = false;

    @FXML
    private void initialize(){
        this.controladorClientes = MEDTFactory.resolve(IControladorClientes.class);

        tipoCliente.getItems().addAll("Cliente estandar","Cliente premium");
        tipoCliente.setValue("Cliente estandar");

        /// Handle empty fields
        BooleanBinding disableAceptarBinding = tfNif.textProperty().isEmpty()
                .or(tfNombre.textProperty().isEmpty())
                .or(tfDomicilio.textProperty().isEmpty())
                .or(tfEmail.textProperty().isEmpty());

        btnAceptar.disableProperty().bind(disableAceptarBinding);
    }

    @FXML
    private void OnAceptar(){
        String nif = tfNif.getText();
        String nombre = tfNombre.getText();
        String domicilio = tfDomicilio.getText();
        String email = tfEmail.getText();
        String tipo = tipoCliente.getValue();

        boolean operationResult = false;

        if (tipo.equals("Cliente estandar")){
            operationResult = this.controladorClientes.addClienteEstandar(nombre, domicilio, nif, email);
        }
        else if (tipo.equals("Cliente premium")){
            operationResult = this.controladorClientes.addClientePremium(nombre, domicilio, nif, email);
        }

        if (operationResult){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Operación completada");
            alert.setContentText("Se ha agregado el cliente correctamente.");
            alert.showAndWait();
            this.operacionCompletada = true;
            root.fireEvent(new OperationCompletedEvent());
            root.fireEvent(new TabCloseEvent());
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error al agregar cliente");
            alert.setContentText("No se ha podido agregar el cliente.");
            alert.showAndWait();
            this.operacionCompletada = false;
            root.fireEvent(new OperationCompletedEvent());
        }
    }

    @FXML
    private void OnCancelar(){
        root.fireEvent(new TabCloseEvent());
    }

    public boolean isOperacionCompletada() {
        return operacionCompletada;
    }
}
