package MEDT.MEDT.vistaFX;

import MEDT.MEDT.DAO.IArticuloDAO;
import MEDT.MEDT.Factory.MEDTFactory;
import MEDT.MEDT.controlador.ControladorArticulos;
import MEDT.MEDT.modelo.Articulo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.List;

public class GestionArticulosController {

    // Componentes de la interfaz gráfica (inyectados por FXML)
    @FXML
    private TextField codigoField;
    @FXML
    private TextField descripcionField;
    @FXML
    private TextField precioField;
    @FXML
    private TextField gastosEnvioField;
    @FXML
    private TextField tiempoPrepField;
    @FXML
    private TableView<Articulo> articulosTable;
    @FXML
    private TableColumn<Articulo, String> codigoColumn;
    @FXML
    private TableColumn<Articulo, String> descripcionColumn;
    @FXML
    private TableColumn<Articulo, Double> precioColumn;
    @FXML
    private TableColumn<Articulo, Double> gastosEnvioColumn;
    @FXML
    private TableColumn<Articulo, Integer> tiempoPrepColumn;

    // Controlador de lógica de negocio
    private ControladorArticulos controladorArticulos;

    // Lista observable para la tabla
    private ObservableList<Articulo> articulosData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // 1. Inicializar el controlador de lógica de negocio.
        // Usamos la DAOFactory para obtener la implementación correcta del DAO de artículos.
        controladorArticulos = new ControladorArticulos(MEDTFactory.resolve(IArticuloDAO.class));

        // 2. Configurar las columnas de la tabla.
        // Se le dice a cada columna de dónde sacar el valor. El string "codigo", "descripcion", etc.
        // debe coincidir exactamente con el nombre de la propiedad en la clase Artículo (con sus getters: getCodigo(), getDescripcion()).
        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        descripcionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        gastosEnvioColumn.setCellValueFactory(new PropertyValueFactory<>("gastosEnvio"));
        tiempoPrepColumn.setCellValueFactory(new PropertyValueFactory<>("tiempoPrep"));

        // 3. Cargar los datos iniciales en la tabla.
        loadArticulosData();
    }

    /**
     * Carga (o recarga) la lista de artículos desde la base de datos y la muestra en la tabla.
     */
    private void loadArticulosData() {
        articulosData.clear();
        List<Articulo> articulos = controladorArticulos.getArticulos();
        if (articulos != null) {
            articulosData.addAll(articulos);
        }
        articulosTable.setItems(articulosData);
    }

    @FXML
    private void handleAddArticulo() {
        // Validar la entrada antes de procesar
        if (isInputValid()) {
            String codigo = codigoField.getText();
            String descripcion = descripcionField.getText();
            double precio = Double.parseDouble(precioField.getText());
            double gastosEnvio = Double.parseDouble(gastosEnvioField.getText());
            int tiempoPrep = Integer.parseInt(tiempoPrepField.getText());

            // Llamar al controlador de lógica para añadir el artículo
            if (controladorArticulos.addArticulo(codigo, descripcion, precio, gastosEnvio, tiempoPrep)) {
                // Si tiene éxito, limpiar los campos y recargar la tabla
                clearFields();
                loadArticulosData();
                showAlert(AlertType.INFORMATION, "Éxito", "Artículo añadido correctamente.");
            } else {
                // Mostrar un mensaje de error si el controlador devuelve false
                showAlert(AlertType.ERROR, "Error de Base de Datos", "No se pudo añadir el artículo. Es posible que el código ya exista.");
            }
        }
    }

    /**
     * Limpia los campos de texto del formulario.
     */
    private void clearFields() {
        codigoField.clear();
        descripcionField.clear();
        precioField.clear();
        gastosEnvioField.clear();
        tiempoPrepField.clear();
    }

    /**
     * Valida los datos introducidos en los campos de texto.
     * @return true si la entrada es válida, false en caso contrario.
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (codigoField.getText() == null || codigoField.getText().isEmpty()) {
            errorMessage += "Código no válido.\n";
        }
        if (descripcionField.getText() == null || descripcionField.getText().isEmpty()) {
            errorMessage += "Descripción no válida.\n";
        }
        try {
            Double.parseDouble(precioField.getText());
        } catch (NumberFormatException e) {
            errorMessage += "Precio no válido (debe ser un número).\n";
        }
        try {
            Double.parseDouble(gastosEnvioField.getText());
        } catch (NumberFormatException e) {
            errorMessage += "Gastos de envío no válidos (debe ser un número).\n";
        }
        try {
            Integer.parseInt(tiempoPrepField.getText());
        } catch (NumberFormatException e) {
            errorMessage += "Tiempo de preparación no válido (debe ser un número entero).\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            // Muestra un diálogo de alerta con los errores.
            showAlert(AlertType.ERROR, "Campos no válidos", "Por favor, corrige los campos no válidos.", errorMessage);
            return false;
        }
    }

    /**
     * Muestra un diálogo de alerta.
     * @param alertType El tipo de alerta (INFORMATION, ERROR, etc.)
     * @param title El título de la ventana de alerta.
     * @param header El texto de la cabecera.
     * @param content El mensaje principal.
     */
    private void showAlert(AlertType alertType, String title, String header, String... content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        if (content.length > 0) {
            alert.setContentText(content[0]);
        }
        alert.showAndWait();
    }
}
