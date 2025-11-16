package MEDT.MEDT.vista.Articulo.Operaciones;

import MEDT.MEDT.Factory.MEDTFactory;
import MEDT.MEDT.controlador.IControladorArticulos;
import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.vista.Eventos.TabCloseEvent;
import MEDT.MEDT.vista.TextFormatters.DecimalFormatter;
import MEDT.MEDT.vista.TextFormatters.IntegerFormatter;
import MEDT.MEDT.vista.TextFormatters.ItemCodeFormatter;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;


public class AgregarArticulo {

    private IControladorArticulos controladorArticulos;

    @FXML
    private AnchorPane root;

    @FXML
    private TextField tfCodigo;

    @FXML
    private TextField tfDescripcion;

    @FXML
    private TextField tfPrecio;

    @FXML
    private TextField tfGastosEnvio;

    @FXML
    private TextField tfTiempoPrep;

    @FXML
    private Button btnAceptar;

    @FXML
    private void initialize(){
        this.controladorArticulos = MEDTFactory.resolve(IControladorArticulos.class);

        /// Allow only item code format
        tfCodigo.setTextFormatter(new TextFormatter<>(new ItemCodeFormatter()));
        /// Allow only decimal
        tfPrecio.setTextFormatter(new TextFormatter<>(new DecimalFormatter()));
        tfGastosEnvio.setTextFormatter(new TextFormatter<>(new DecimalFormatter()));
        /// Allow only integers
        tfTiempoPrep.setTextFormatter(new TextFormatter<>(new IntegerFormatter()));

        /// Handle empty fields
        BooleanBinding disableAceptarBinding = tfCodigo.textProperty().isEmpty()
                .or(tfDescripcion.textProperty().isEmpty())
                .or(tfPrecio.textProperty().isEmpty())
                .or(tfGastosEnvio.textProperty().isEmpty())
                .or(tfTiempoPrep.textProperty().isEmpty());

        btnAceptar.disableProperty().bind(disableAceptarBinding);
    }

    @FXML
    private void OnAceptar(){
        String codigo = this.tfCodigo.getText();
        String descripcion = this.tfDescripcion.getText();
        double precio = Double.parseDouble(this.tfPrecio.getText());
        double gastosEnvio = Double.parseDouble(this.tfGastosEnvio.getText());
        int tiempoPrep = Integer.parseInt(this.tfTiempoPrep.getText());

        if (this.controladorArticulos.addArticulo(codigo, descripcion, precio, gastosEnvio, tiempoPrep)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Operación completada");
            alert.setContentText("Se ha agregado el articulo correctamente.");
            alert.showAndWait();
            root.fireEvent(new TabCloseEvent());
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error al agregar articulo");
            alert.setContentText("No se ha podido agregar el articulo.");
            alert.showAndWait();
        }
    }

    @FXML
    private void OnCancelar(){
        root.fireEvent(new TabCloseEvent());
    }
}
