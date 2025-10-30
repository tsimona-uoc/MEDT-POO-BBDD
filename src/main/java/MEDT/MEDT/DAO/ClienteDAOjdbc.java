package MEDT.MEDT.DAO;

import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.ClientePremium;
import MEDT.MEDT.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClienteDAOjdbc implements IClienteDAO {
    @Override
    public void insert(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nif, nombre, domicilio, email, tipo, cuota, descuento) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cliente.getNif());
            ps.setString(2, cliente.getNif());
            ps.setString(3, cliente.getDomicilio());
            ps.setString(4, cliente.getEmail());
            if (cliente instanceof ClientePremium premium){
                ps.setString(5, "Premium");
                ps.setDouble(6, premium.getCuota());
                ps.setDouble(7, premium.getDescuento());
            }else if (cliente instanceof ClienteEstandar){
                ps.setString(5, "Estandar");
                ps.setNull(6, java.sql.Types.DOUBLE);
                ps.setNull(7, java.sql.Types.DOUBLE);
            }
            ps.executeUpdate();
        }
    }
}
