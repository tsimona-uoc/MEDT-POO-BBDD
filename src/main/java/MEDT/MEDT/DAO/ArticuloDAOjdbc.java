package MEDT.MEDT.DAO;

import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
