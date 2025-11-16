package MEDT.MEDT.modelo.persistencia.mysql;

import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.modelo.excepciones.ArticuloNoEncontradoException;
import MEDT.MEDT.modelo.persistencia.dao.ArticuloDAO;
import MEDT.MEDT.modelo.persistencia.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticuloDAOImpl implements ArticuloDAO {

    @Override
    public void insertar(Articulo articulo) {
        String sql = "INSERT INTO articulo (codigo, descripcion, precio, gastos_envio, tiempo_prep) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, articulo.getCodigo());
            ps.setString(2, articulo.getDescripcion());
            ps.setDouble(3, articulo.getPrecio());
            ps.setDouble(4, articulo.getGastosEnvio());
            ps.setInt(5, articulo.getTiempoPrep());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Articulo obtenerPorCodigo(String codigo) throws ArticuloNoEncontradoException {
        String sql = "SELECT * FROM articulo WHERE codigo = ?";
        Articulo articulo = null;
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    articulo = new Articulo(
                            rs.getString("codigo"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio"),
                            rs.getDouble("gastos_envio"),
                            rs.getInt("tiempo_prep")
                    );
                } else {
                    throw new ArticuloNoEncontradoException("El artículo con código " + codigo + " no existe.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articulo;
    }

    @Override
    public List<Articulo> obtenerTodos() {
        String sql = "SELECT * FROM articulo";
        List<Articulo> articulos = new ArrayList<>();
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Articulo articulo = new Articulo(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getDouble("gastos_envio"),
                        rs.getInt("tiempo_prep")
                );
                articulos.add(articulo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articulos;
    }
}