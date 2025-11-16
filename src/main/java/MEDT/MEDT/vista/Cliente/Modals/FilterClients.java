package MEDT.MEDT.vista.Cliente.Modals;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FilterClients {

    @FXML
    private RadioButton tipoClienteNA;
    @FXML
    private RadioButton tipoClienteEstandar;
    @FXML
    private RadioButton tipoClientePremium;
    @FXML
    private TextField tfNif;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfDomicilio;
    @FXML
    private TextField tfEmail;
    /// Toggle group
    private ToggleGroup toggleGroup;

    private void initialize(){
        toggleGroup = new ToggleGroup();

        tipoClienteNA.setToggleGroup(toggleGroup);
        tipoClienteEstandar.setToggleGroup(toggleGroup);
        tipoClientePremium.setToggleGroup(toggleGroup);
    }

    public void OnAplicarFiltros(){

    }

    public void OnBorrarFiltros(){

    }
}
