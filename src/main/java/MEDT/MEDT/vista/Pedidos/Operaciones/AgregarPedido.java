package MEDT.MEDT.vista.Pedidos.Operaciones;

import MEDT.MEDT.Factory.MEDTFactory;
import MEDT.MEDT.controlador.IControladorArticulos;
import MEDT.MEDT.controlador.IControladorClientes;
import MEDT.MEDT.controlador.IControladorPedidos;
import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.Pedido;
import MEDT.MEDT.vista.Cliente.Operaciones.AgregarCliente;
import MEDT.MEDT.vista.Eventos.OperationCompletedEvent;
import MEDT.MEDT.vista.Eventos.TabCloseEvent;
import MEDT.MEDT.vista.TextFormatters.IntegerFormatter;
import MEDT.MEDT.vista.TextFormatters.TimeFormatter;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;


public class AgregarPedido {

    private IControladorArticulos  controladorArticulos;
    private IControladorClientes controladorClientes;
    private IControladorPedidos controladorPedidos;

    @FXML
    private AnchorPane root;

    @FXML
    private ChoiceBox<Articulo> articuloChoice;

    @FXML
    private TextField cantidad;

    @FXML
    private DatePicker fecha;

    @FXML
    private TextField hora;

    @FXML
    private ComboBox<Cliente> clienteComboBox;

    @FXML
    private Button btnAceptar;

    @FXML
    private void initialize(){
        this.controladorPedidos = MEDTFactory.resolve(IControladorPedidos.class);
        this.controladorArticulos = MEDTFactory.resolve(IControladorArticulos.class);
        this.controladorClientes = MEDTFactory.resolve(IControladorClientes.class);


        /// Add all articles options
        articuloChoice.getItems().addAll(this.controladorArticulos.getArticulos().stream().toList());

        /// Create a converter to display code and description
        StringConverter<Articulo> articuloConverter = new StringConverter<Articulo>() {

            @Override
            public String toString(Articulo articulo) {
                if (articulo == null) {
                    return null;
                }
                return articulo.getCodigo() + " - " + articulo.getDescripcion();
            }

            @Override
            public Articulo fromString(String string) {
                return null;
            }
        };

        /// Set article converter
        articuloChoice.setConverter(articuloConverter);

        /// Allow only integers
        cantidad.setTextFormatter(new TextFormatter<>(new IntegerFormatter()));

        /// Date input
        fecha.setValue(LocalDate.now());

        /// Time input
        hora.setTextFormatter(new TextFormatter<>(new TimeFormatter()));

        /// Set current time
        hora.setText(
                LocalDateTime.now().getHour() + ":" +
                (LocalDateTime.now().getMinute() < 10 ? "0" + LocalDateTime.now().getMinute() : LocalDateTime.now().getMinute()) + ":" +
                (LocalDateTime.now().getSecond() < 10 ? "0" + LocalDateTime.now().getSecond() : LocalDateTime.now().getSecond()));

        BooleanBinding isHoraValida = Bindings.createBooleanBinding(() -> {
            final String TIME_REGEX_STRICT = "^(?:[01]\\d|2[0-3]):[0-5]\\d(?::[0-5]\\d)?$";
            String text = hora.getText();
            return (text != null && !text.trim().isEmpty() && text.matches(TIME_REGEX_STRICT));

        }, hora.textProperty());

        clienteComboBox.setEditable(true);

        /// Add clientes to combo box
        clienteComboBox.getItems().addAll(this.controladorClientes.getClientes().stream().toList());

        /// Create a converter to display code and description
        StringConverter<Cliente> clientesConverter = new StringConverter<Cliente>() {

            @Override
            public String toString(Cliente cliente) {
                if (cliente == null) {
                    return "";
                }
                return cliente.getNif() + " - " + cliente.getNombre();
            }

            @Override
            public Cliente fromString(String newValue) {

                if (newValue == null || newValue.trim().isEmpty() || newValue.length() < 9) {
                    return null;
                }

                final String busqueda = newValue.toUpperCase();

                String nif = busqueda.substring(0, 9);
                Cliente cliente = controladorClientes.getCliente(nif);

                if (cliente == null){
                    /// Cliente no encontrado

                    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmAlert.setTitle("Cliente no encontrado");
                    confirmAlert.setHeaderText("No se ha encontrado ningún cliente con NIF " + nif + ".");
                    confirmAlert.setContentText("¿Desea realizar la operación ahora?");
                    Optional<ButtonType> result = confirmAlert.showAndWait();

                    if (result.isEmpty() || result.get() != ButtonType.OK) {
                        clienteComboBox.getEditor().setText("");
                        return null;
                    }

                    Cliente clienteAgregado = agregarClienteAdhoc(clienteComboBox.getScene().getWindow(), nif);
                    clienteComboBox.getItems().clear();
                    clienteComboBox.getItems().addAll(controladorClientes.getClientes().stream().toList());
                    return clienteAgregado;
                }

                return cliente;
            }
        };

        clienteComboBox.setConverter(clientesConverter);

        /// Handle empty fields
        BooleanBinding disableAceptarBinding = articuloChoice.valueProperty().isNull()
                .or(cantidad.textProperty().isEmpty())
                .or(fecha.valueProperty().isNull())
                .or(isHoraValida.not())
                .or(clienteComboBox.valueProperty().isNull());

        btnAceptar.disableProperty().bind(disableAceptarBinding);
    }

    @FXML
    private void OnAceptar(){

        Articulo articulo = articuloChoice.getValue();

        int cantidadInt = Integer.parseInt(cantidad.textProperty().get());

        DateTimeFormatter longFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter shortFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        String rawDateTime = fecha.getEditor().getText() + " " +  hora.textProperty().get();
        LocalDateTime fechaHora = null;

        try {
            fechaHora = LocalDateTime.parse(rawDateTime, longFormat);
        }
        catch (Exception ex){
            fechaHora = LocalDateTime.parse(rawDateTime, shortFormat);
        }

        int operationResult = this.controladorPedidos.addPedido(0, cantidadInt, fechaHora, articulo.getCodigo(), clienteComboBox.getValue().getNif());

        if (operationResult == 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Operación completada");
            alert.setContentText("Se ha agregado el pedido correctamente.");
            alert.showAndWait();
            root.fireEvent(new TabCloseEvent());
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error al agregar pedido");
            alert.setContentText("No se ha podido agregar el pedido.");
            alert.showAndWait();
        }
    }

    @FXML
    private void OnCancelar(){
        root.fireEvent(new TabCloseEvent());
    }

    private Cliente agregarClienteAdhoc(Window owner, String nif){
        try {
            // 1. Cargar el FXML de la nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Clientes/Operaciones/AgregarCliente.fxml"));
            Parent root = loader.load();

            // 2. Obtener el controlador de la ventana de alta
            AgregarCliente agregarclienteController = loader.getController();

            // 3. Inicializar el controlador de la ventana de alta
            // Usamos el texto que el usuario introdujo en el ComboBox para precargar el campo NIF/CIF
            agregarclienteController.tfNif.setText(nif);
            agregarclienteController.tfNif.setEditable(false);

            // 4. Configurar el nuevo Stage (Ventana)
            Stage altaStage = new Stage();
            altaStage.setTitle("Agregar Nuevo Cliente");
            altaStage.setScene(new Scene(root));

            // 5. Configurar la Modalidad (IMPORTANTE)
            // Bloquea la ventana principal hasta que se cierre este diálogo.
            altaStage.initOwner(owner);
            altaStage.initModality(Modality.APPLICATION_MODAL);

            /// Handle close tab event
            root.addEventHandler(OperationCompletedEvent.OPERATION_COMPLETED_REQUEST, event -> {
                altaStage.close();
            });

            // 6. Mostrar el diálogo y esperar a que se cierre
            altaStage.showAndWait();

            // 7. Lógica post-cierre (Si el alta fue exitosa)
            // Por ejemplo, obtener el objeto Cliente que fue creado.
            if (agregarclienteController.isOperacionCompletada()) {
                return this.controladorClientes.getCliente(nif);
            }

        } catch (IOException e) {
            System.err.println("Error al cargar la vista AltaCliente.fxml: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
