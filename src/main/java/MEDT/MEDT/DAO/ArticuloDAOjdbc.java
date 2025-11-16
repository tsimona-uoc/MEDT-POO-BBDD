package MEDT.MEDT.DAO;

import MEDT.MEDT.modelo.Articulo;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/// JDBC implementation of DAO articulo
public class ArticuloDAOjdbc implements IArticuloDAO {

    /// Get a connection
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/MEDT_POO_DDBB", "root", "password");
    }

    /// Insert a new item
    @Override
    public void insert(Articulo articulo) throws SQLException {
        String sql = "INSERT INTO articulo (codigo, descripcion, precio, gastosEnvio, tiempoPreparacion) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, articulo.getCodigo());
            stmt.setString(2, articulo.getDescripcion());
            stmt.setDouble(3, articulo.getPrecio());
            stmt.setDouble(4, articulo.getGastosEnvio());
            stmt.setInt(5, articulo.getTiempoPrep());
            stmt.executeUpdate();
        }
    }

    /// Update a given item
    @Override
    public void update(Articulo articulo) throws SQLException {
        String sql = "UPDATE articulo SET descripcion = ?, precio = ?,  gastosEnvio = ?, tiempoPreparacion = ? WHERE codigo = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            /// UPDATE parameters
            stmt.setString(1, articulo.getDescripcion());
            stmt.setDouble(2, articulo.getPrecio());
            stmt.setDouble(3, articulo.getGastosEnvio());
            stmt.setInt(4, articulo.getTiempoPrep());

            /// WHERE parameters
            stmt.setString(5, articulo.getCodigo());
            stmt.executeUpdate();
        }
    }

    /// Delete a given item by code
    @Override
    public void delete(String codigo) throws SQLException {
        String sql = "DELETE FROM articulo WHERE codigo = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            /// WHERE parameters
            stmt.setString(1, codigo);
            stmt.executeUpdate();
        }
    }

    /// Find an item by code
    @Override
    public Articulo findByCodigo(String codigo) throws SQLException{
        Articulo articulo = null;

        String sql = "SELECT * FROM articulo WHERE codigo = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);
            ResultSet resultados = stmt.executeQuery();

            while(resultados.next()){

                String codigoArticulo = resultados.getString(1);
                String descripcionArticulo = resultados.getString(2);
                double precioArticulo = resultados.getDouble(3);
                double gastosEnvioArticulo = resultados.getDouble(4);
                int tiempoPreparacionArticulo = resultados.getInt(5);

                articulo = new Articulo(codigoArticulo, descripcionArticulo, precioArticulo, gastosEnvioArticulo, tiempoPreparacionArticulo);
            }
        }

        return articulo;
    }

    /// Returns a list of all items
    @Override
    public Collection<Articulo> findAll() throws SQLException {
        List<Articulo> articulos = new ArrayList<>();

        String sql = "SELECT * FROM articulo";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet resultados = stmt.executeQuery();

            while (resultados.next()){

                String codigoArticulo = resultados.getString(1);
                String descripcionArticulo = resultados.getString(2);
                double precioArticulo = resultados.getDouble(3);
                double gastosEnvioArticulo = resultados.getDouble(4);
                int tiempoPreparacionArticulo = resultados.getInt(5);

                Articulo articulo = new Articulo(codigoArticulo, descripcionArticulo, precioArticulo, gastosEnvioArticulo, tiempoPreparacionArticulo);

                articulos.add(articulo);
            }
        }

        return articulos;
    }
}
