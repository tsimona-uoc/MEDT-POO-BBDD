package MEDT.MEDT.vista.Cliente.Operaciones;

import MEDT.MEDT.Factory.MEDTFactory;
import MEDT.MEDT.controlador.IControladorArticulos;
import MEDT.MEDT.controlador.IControladorClientes;
import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.ClientePremium;
import MEDT.MEDT.vista.Eventos.TabCloseEvent;
import MEDT.MEDT.vista.TextFormatters.DecimalFormatter;
import MEDT.MEDT.vista.TextFormatters.IntegerFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;


public class EditarCliente {

    private IControladorClientes controladorClientes;

    @FXML
    private AnchorPane root;

    @FXML
    private TextField tfNif;

    @FXML
    private TextField tfNombre;

    @FXML
    private TextField tfDomicilio;

    @FXML
    private TextField tfEmail;

    @FXML
    private ChoiceBox<String> tipoCliente;

    @FXML
    private Button btnActualizar;

    private Cliente cliente;

    @FXML
    private void initialize(){
        this.controladorClientes = MEDTFactory.resolve(IControladorClientes.class);

        tipoCliente.getItems().addAll("Cliente estandar","Cliente premium");
    }

    public void setData(Cliente cliente){
        this.cliente = cliente;
        this.updateTextFields();

        String tipoClientStr = cliente instanceof ClientePremium ? "Cliente premium" : "Cliente estandar";
        this.btnActualizar.disableProperty().unbind();
        this.btnActualizar.disableProperty().bind(
            /// Any property is empty
            tfEmail.textProperty().isEmpty()
            .or(tfDomicilio.textProperty().isEmpty())
            /// or all the values are the same
            .or(
                    tfEmail.textProperty().isEqualTo(cliente.getEmail())
                    .and(tfDomicilio.textProperty().isEqualTo(cliente.getDomicilio()))
                    .and(tipoCliente.valueProperty().isEqualTo(tipoClientStr))
            )
        );
    }

    private void updateTextFields(){
        String tipoClientStr = cliente instanceof ClientePremium ? "Cliente premium" : "Cliente estandar";

        tfNif.setText(cliente.getNif());
        tfNombre.setText(cliente.getNombre());
        tfDomicilio.setText(cliente.getDomicilio());
        tfEmail.setText(cliente.getEmail());
        tipoCliente.setValue(tipoClientStr);
    }

    @FXML
    private void OnUpdate(){
        if (this.cliente == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Algo ha salido mal");
            alert.setContentText("Se ha producido un error inesperado, el cliente no puede ser nulo.");
            alert.showAndWait();
            root.fireEvent(new TabCloseEvent());
            return;
        }

        String domicilio = tfDomicilio.getText();
        String email = tfEmail.getText();
        String tipo = tipoCliente.getValue();

        if (tipo.equals("Cliente estandar")){
            this.cliente = new ClienteEstandar(this.cliente.getNombre(), domicilio, this.cliente.getNif(), email);
        }
        else if (tipo.equals("Cliente premium")){
            this.cliente = new ClientePremium(this.cliente.getNombre(), domicilio, this.cliente.getNif(), email);
        }

        if (this.controladorClientes.updateCliente(this.cliente)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Cliente actualizado");
            alert.setContentText("Se ha actualizado el cliente correctamente.");
            alert.showAndWait();
            root.fireEvent(new TabCloseEvent());
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error al actualizar el cliente");
            alert.setContentText("No se ha podido actualizar el cliente.");
            alert.showAndWait();
        }
    }

    @FXML
    private void OnCerrar(){
        root.fireEvent(new TabCloseEvent());
    }
}
