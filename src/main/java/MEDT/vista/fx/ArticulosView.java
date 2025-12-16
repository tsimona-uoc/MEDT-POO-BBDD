package MEDT.vista.fx;

import MEDT.MEDT.controlador.ControladorArticulos;
import MEDT.MEDT.modelo.Articulo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ArticulosView{
    private final ControladorArticulos controlador;
    private final Stage stage;;
    private final Scene scene;

    private final TableView<Articulo> table = new TableView<>();

    public ArticulosView(Stage stage, ControladorArticulos controlador, MenuPrincipalView menuPrincipal){
        this.stage = stage;
        this.controlador = controlador;
        this.menuPrincipal = menuPrincipal;

        BorderPane root = new BorderPane();
        root.setTop(crearFormulario());
        root.setCenter(crearTabla());
        root.setBottom(crearBotonVolver());

        this.scene = new Scene(root, 700, 500);
        cargarArticulos();
    }
    public Scene getScene(){
        return scene;
    }
    private GridPane crearFormulario(){
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-padding: 15;");

        TextField txtCodigo = new TextField();
        TextField txtDescripcion = new TextField();
        TextField txtPrecio = new TextField();
        TextField txtGastos = new TextField();
        TextField txtTiempo = new TextField();

        Button btnAgregar = new Button("Añadir artículo");

        grid.add(new Label("Código"), 0, 0);
        grid.add(txtCodigo, 1, 0);

        grid.add(new Label("Descripción"), 0, 1);
        grid.add(txtDescripcion, 1, 1);

        grid.add(new Label("Precio:"), 0, 2);
        grid.add(txtPrecio, 1, 2);

        grid.add(new Label("Gastos envío"), 0, 3);
        grid.add(txtGastos, 1, 3);

        grid.add(new Label("Tiempo preparación"),0,4);
        grid.add(txtTiempo, 1, 4);

        grid.add(btnAgregar,1,5);

        btnAgregar.setOnAction(e->{
            try {
                boolean ok = controlador.addArticulo(
                        txtCodigo.getText(),
                        txtDescripcion.getText(),
                        Double.parseDouble(txtPrecio.getText()),
                        Double.parseDouble(txtGastos.getText()),
                        Integer.parseInt(txtTiempo.getText())
                );
                if (ok) {
                    cargarArticulos();
                    txtCodigo.clear();
                    txtDescripcion.clear();
                    txtPrecio.clear();
                    txtGastos.clear();
                    txtTiempo.clear();
                    ;
                } else{
                    mostrarError("No se pudo añadir el artículo");
                }
            }catch (Exception ex){
                mostrarError("Datos incorrectos");
            }
        });
        return grid;
    }
    private TableView<Articulo> crearTabla(){
        TableColumn<Articulo, String> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getCodigo())
        );
        TableColumn<Articulo, String> colDesc = new TableColumn<>("Descripción");
        colDesc.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getDescripcion())
        );
        TableColumn<Articulo, Number> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(data ->
                new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPrecio())
        );
        table.getColumns().addAll(colCodigo, colDesc, colPrecio);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return table;
    }
    private void cargarArticulos(){
        var lista = controlador.getArticulos();
        ObservableList<Articulo> datos = FXCollections.observableArrayList(lista);
        table.setItems(datos);
    }
    private HBox crearBotonVolver(){
        Button btnVolver = new Button("Volver");

        btnVolver.setOnAction(e -> {
            stage.setScene(menuPrincipal.getScene());
        });

        HBox box = new HBox(btnVolver);
        box.setStyle("-fx-padding: 10; -fx-alignment center;");
        return box;
    }
    private void mostrarError(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    private final MenuPrincipalView menuPrincipal;
}

