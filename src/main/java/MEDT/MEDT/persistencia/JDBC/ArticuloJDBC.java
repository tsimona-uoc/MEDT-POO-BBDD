package MEDT.MEDT.persistencia.JDBC;

import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.persistencia.connection.ConnectionUtil;
import MEDT.MEDT.persistencia.DAO.ArticuloDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import  java.util.List;

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
            System.out.println("Error al insertar artículo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Articulo buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM articulo WHERE codigo = ?";

        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigo);

            var rs = ps.executeQuery();

            if (rs.next()) {
                // Crear el objeto Articulo con los datos del registro
                String descripcion = rs.getString("descripcion");
                double precio = rs.getDouble("precio");
                double gastosEnvio = rs.getDouble("gastosEnvio");
                int tiempoPrep = rs.getInt("tiempoPrep");

                return new Articulo(codigo, descripcion, precio, gastosEnvio, tiempoPrep);
            } else {
                return null; // no se encontró ningún artículo
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar artículo: " + e.getMessage());
            return null;
        }
    }


    @Override
    public List<Articulo> listarTodos() {
        String sql = "SELECT * FROM articulo";
        List<Articulo> articulos = new java.util.ArrayList<>();

        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             var rs = ps.executeQuery()) {

            while (rs.next()) {
                String codigo = rs.getString("codigo");
                String descripcion = rs.getString("descripcion");
                double precio = rs.getDouble("precio");
                double gastosEnvio = rs.getDouble("gastosEnvio");
                int tiempoPrep = rs.getInt("tiempoPrep");

                Articulo articulo = new Articulo(codigo, descripcion, precio, gastosEnvio, tiempoPrep);
                articulos.add(articulo);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar artículos: " + e.getMessage());
            return null;
        }

        return articulos;
    }

}
