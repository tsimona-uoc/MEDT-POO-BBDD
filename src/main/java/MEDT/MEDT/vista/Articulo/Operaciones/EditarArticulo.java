package MEDT.MEDT.vista.Articulo.Operaciones;

import MEDT.MEDT.Factory.MEDTFactory;
import MEDT.MEDT.controlador.IControladorArticulos;
import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.vista.Eventos.TabCloseEvent;
import MEDT.MEDT.vista.TextFormatters.DecimalFormatter;
import MEDT.MEDT.vista.TextFormatters.IntegerFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;

import java.util.function.UnaryOperator;


public class EditarArticulo {

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
    private Button btnActualizar;

    private Articulo articulo;

    @FXML
    private void initialize(){
        this.controladorArticulos = MEDTFactory.resolve(IControladorArticulos.class);

        /// Allow only decimal
        tfPrecio.setTextFormatter(new TextFormatter<>(new DecimalFormatter()));
        tfGastosEnvio.setTextFormatter(new TextFormatter<>(new DecimalFormatter()));
        /// Allow only integers
        tfTiempoPrep.setTextFormatter(new TextFormatter<>(new IntegerFormatter()));
    }

    public void setData(Articulo articulo){
        this.articulo = articulo;
        this.updateTextFields();

        this.btnActualizar.disableProperty().unbind();
        this.btnActualizar.disableProperty().bind(
            /// Any property is empty
            tfDescripcion.textProperty().isEmpty()
            .or(tfPrecio.textProperty().isEmpty())
            .or(tfGastosEnvio.textProperty().isEmpty())
            .or(tfTiempoPrep.textProperty().isEmpty())
            /// or all the values are the same
            .or(
                    tfDescripcion.textProperty().isEqualTo(articulo.getDescripcion())
                    .and(tfPrecio.textProperty().isEqualTo(String.valueOf(articulo.getPrecio())))
                    .and(tfGastosEnvio.textProperty().isEqualTo(String.valueOf(articulo.getGastosEnvio())))
                    .and(tfTiempoPrep.textProperty().isEqualTo(String.valueOf(articulo.getTiempoPrep())))
            )
        );
    }

    private void updateTextFields(){
        tfCodigo.setText(articulo.getCodigo());
        tfDescripcion.setText(articulo.getDescripcion());
        tfPrecio.setText(String.valueOf(articulo.getPrecio()));
        tfGastosEnvio.setText(String.valueOf(articulo.getGastosEnvio()));
        tfTiempoPrep.setText(String.valueOf(articulo.getTiempoPrep()));
    }

    @FXML
    private void OnUpdate(){
        if (this.articulo == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Algo ha salido mal");
            alert.setContentText("Se ha producido un error inesperado, el articulo no puede ser nulo.");
            alert.showAndWait();
            root.fireEvent(new TabCloseEvent());
            return;
        }

        this.articulo.setDescripcion(tfDescripcion.getText());
        this.articulo.setPrecio(Double.parseDouble(tfPrecio.getText()));
        this.articulo.setGastosEnvio(Double.parseDouble(tfGastosEnvio.getText()));
        this.articulo.setTiempoPrep(Integer.parseInt(tfTiempoPrep.getText()));

        if (this.controladorArticulos.updateArticulo(this.articulo)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Articulo actualizado");
            alert.setContentText("Se ha actualizar el articulo correctamente.");
            alert.showAndWait();

            root.fireEvent(new TabCloseEvent());
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error al actualizar el articulo");
            alert.setContentText("No se ha podido actualizar el articulo.");
            alert.showAndWait();
        }
    }

    @FXML
    private void OnCerrar(){
        root.fireEvent(new TabCloseEvent());
    }
}
