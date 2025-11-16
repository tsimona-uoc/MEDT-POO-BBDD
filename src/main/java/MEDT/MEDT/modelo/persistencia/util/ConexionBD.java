package MEDT.MEDT.modelo.persistencia.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/tienda_online_mvc";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = ""; // Contraseña vacía para XAMPP

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error al cargar el driver JDBC de MySQL.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }

    public static void close(Connection conn) {
        if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void close(PreparedStatement ps) {
        if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void close(ResultSet rs) {
        if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
    }
}