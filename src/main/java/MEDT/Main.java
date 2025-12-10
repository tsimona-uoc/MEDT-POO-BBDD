package MEDT;

import MEDT.MEDT.DAO.IArticuloDAO;
import MEDT.MEDT.DAO.IClienteDAO;
import MEDT.MEDT.DAO.IPedidoDAO;
import MEDT.MEDT.DAO.ArticuloDAOJPA;
import MEDT.MEDT.DAO.ClienteDAOJPA;
import MEDT.MEDT.DAO.PedidoDAOJPA;
import MEDT.MEDT.Factory.MEDTFactory;
import MEDT.MEDT.vistaFX.RootLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class Main extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void init() throws Exception {
        // PASO 1: INICIALIZAR LA LÓGICA DE NEGOCIO AQUÍ
        System.out.println("Inicializando la lógica de negocio...");
        MEDTFactory.registerType(IArticuloDAO.class, new ArticuloDAOJPA());
        MEDTFactory.registerType(IClienteDAO.class, new ClienteDAOJPA());
        MEDTFactory.registerType(IPedidoDAO.class, new PedidoDAOJPA());
        System.out.println("Lógica de negocio inicializada.");
    }

    @Override
    public void start(Stage primaryStage) {
        // PASO 2: CREAR LA INTERFAZ GRÁFICA
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Gestión de Tienda MEDT");
        initRootLayout();
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/MEDT/view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            RootLayoutController controller = loader.getController(); // Obtener el controlador
            controller.setMain(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BorderPane getRootLayout() {
        return rootLayout;
    }

    public static void main(String[] args) {
        // PASO 3: ESTE METODO SOLO DEBE LANZAR LA APLICACIÓN
        launch(args);
    }
}