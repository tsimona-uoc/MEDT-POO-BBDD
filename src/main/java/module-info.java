module MEDT {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base; // Es buena práctica incluirlo
    requires java.sql;
    requires java.base;
    requires mysql.connector.j;
    requires java.desktop;

    opens MEDT to javafx.fxml, javafx.base;
    opens MEDT.MEDT.modelo to javafx.fxml, javafx.base;
    opens MEDT.MEDT.vista to javafx.fxml, javafx.base;
    opens MEDT.MEDT.vista.Articulo to javafx.base, javafx.fxml;
    opens MEDT.MEDT.vista.Articulo.Operaciones to javafx.base, javafx.fxml;
    opens MEDT.MEDT.vista.Cliente to javafx.base, javafx.fxml;
    opens MEDT.MEDT.vista.Cliente.Operaciones to javafx.base, javafx.fxml;

    exports MEDT;
    exports MEDT.MEDT.vista;
    exports MEDT.MEDT.vista.Articulo;
    exports MEDT.MEDT.vista.Articulo.Operaciones;

    exports MEDT.MEDT.vista.Cliente;
    exports MEDT.MEDT.vista.Cliente.Operaciones;

    exports MEDT.MEDT.modelo;
}