package MEDT.MEDT.persistencia.JDBC;

import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.persistencia.connection.ConnectionUtil;
import MEDT.MEDT.persistencia.DAO.ArticuloDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ArticuloJDBC implements ArticuloDAO {

    @Override
    public boolean addArticulo(Articulo articulo) {
        String sql = "INSERT INTO articulo (codigo, descripcion, precio, gastosEnvio, tiempoPrep) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, articulo.getCodigo());
            ps.setString(2, articulo.getDescripcion());
            ps.setDouble(3, articulo.getPrecio());
            ps.setDouble(4, articulo.getGastosEnvio());
            ps.setInt(5, articulo.getTiempoPrep());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("❌ Error al insertar artículo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Articulo buscarPorCodigo(String codigo) {
        // Lo haremos más adelante
        return null;
    }

    @Override
    public java.util.List<Articulo> listarTodos() {
        // Lo haremos más adelante
        return null;
    }
}
