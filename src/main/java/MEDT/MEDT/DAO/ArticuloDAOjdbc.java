package MEDT.MEDT.DAO;

import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticuloDAOjdbc implements IArticuloDAO{
    @Override
    public void insert(Articulo articulo) throws SQLException{
        String sql = "INSERT INTO articulos (codigo, descripcion, precio, gastosEnvio, tiempoPrep) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setString(1, articulo.getCodigo());
                ps.setString(2, articulo.getDescripcion());
                ps.setDouble(3, articulo.getPrecio());
                ps.setDouble(4, articulo.getGastosEnvio());
                ps.setInt(5, articulo.getTiempoPrep());

                ps.executeUpdate();
        }
    }
    @Override
    public List<Articulo> findAll() throws SQLException{
        List<Articulo> articulos = new ArrayList<>();
        String sql = "SELECT * FROM articulos";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            while (rs.next()){
                String codigo = rs.getString("codigo");
                String descripcion = rs.getString("descripcion");
                Double precio = rs.getDouble("precio");
                Double gastosEnvio = rs.getDouble("gastosEnvio");
                Integer tiempoPrep = rs.getInt("TiempoPrep");

                Articulo articulo = new Articulo(codigo, descripcion, precio, gastosEnvio, tiempoPrep);
                articulos.add(articulo);
            }
        }
        return articulos;
    }
}
