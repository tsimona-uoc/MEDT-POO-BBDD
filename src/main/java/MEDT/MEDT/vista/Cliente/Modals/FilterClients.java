package MEDT.MEDT.vista.Cliente.Modals;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FilterClients {

    public class Data {
        public String tipoCliente;
        public String nif;
        public String nombre;
        public String domicilio;
        public String email;
    }

    @FXML
    private AnchorPane root;
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

    /// Filter data
    private Data filterData;

    public void setFilterData(Data data){
        this.filterData = data;

        if (this.filterData != null) {
            if (this.filterData.tipoCliente != null && this.filterData.tipoCliente.equals("estandar")) {
                tipoClienteEstandar.setSelected(true);
            } else if (this.filterData.tipoCliente != null && this.filterData.tipoCliente.equals("premium")) {
                tipoClientePremium.setSelected(true);
            } else {
                tipoClienteNA.setSelected(true);
            }

            tfNif.setText(filterData.nif);
            tfNombre.setText(filterData.nombre);
            tfDomicilio.setText(filterData.domicilio);
            tfEmail.setText(filterData.email);
        }
        else{
            tipoClienteNA.setSelected(true);
            tfNif.setText("");
            tfNombre.setText("");
            tfDomicilio.setText("");
            tfEmail.setText("");
        }
    }

    /// Get filter data
    public Data getFilterData(){
        return filterData;
    }

    private void initialize(){
        filterData = null;
    }

    public void OnAplicarFiltros(){

        if (tipoClienteNA.isSelected() && tfNif.getText().isEmpty() && tfNombre.getText().isEmpty() && tfDomicilio.getText().isEmpty() && tfEmail.getText().isEmpty()){
            this.filterData = null;
        }
        else {
            filterData = new Data();
            filterData.tipoCliente = tipoClienteNA.isSelected() ? "" : tipoClienteEstandar.isSelected() ? "estandar" : tipoClientePremium.isSelected() ? "premium" : "";
            filterData.nif = tfNif.getText();
            filterData.nombre = tfNombre.getText();
            filterData.domicilio = tfDomicilio.getText();
            filterData.email = tfEmail.getText();
        }

        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    public void OnBorrarFiltros(){
        filterData = null;
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }
}
