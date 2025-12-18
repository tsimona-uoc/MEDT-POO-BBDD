package MEDT.MEDT.vista.Pedidos.Modals;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FilterPedidos {

    @FXML
    private AnchorPane root;

    @FXML
    public TextField tfValue;


    public class Data {
        /// TBD
    }

    /// Filter data
    private Data filterData;

    public void setFilterData(Data data){
        this.filterData = data;

        if (this.filterData != null) {
            /// TODO: Apply filters to form fields
        }
        else{
            /// TODO: Empty form values

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
        /// TODO: Build a filter data and close form
        filterData = new Data();
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    public void OnBorrarFiltros(){
        this.filterData = null;
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }
}
