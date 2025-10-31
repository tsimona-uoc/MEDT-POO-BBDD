package MEDT.MEDT.persistencia.JDBC;

import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.ClientePremium;
import MEDT.MEDT.persistencia.connection.ConnectionUtil;
import MEDT.MEDT.persistencia.DAO.ClienteDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import  java.util.List;

public class ClienteJDBC implements ClienteDAO {

    @Override
    public boolean addCliente(Cliente cliente) {
        String sql = "INSERT INTO cliente (nif, nombre, domicilio, email, tipo) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConnectionUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getNif());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getDomicilio());
            ps.setString(4, cliente.getEmail());

            // Determinar tipo de cliente (Estandar o Premium)
            if (cliente instanceof ClienteEstandar) {
                ps.setString(5, "Estandar");
            } else if (cliente instanceof ClientePremium) {
                ps.setString(5, "Premium");
            } else {
                ps.setString(5, "Estandar"); // valor por defecto
            }

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al insertar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Cliente getCliente(String nif) {
        return null;
    }

    @Override
    public Collection<Cliente> getClientes() {
        return List.of();
    }

    @Override
    public List<Cliente> getClientesEstandar() {
        return List.of();
    }

    @Override
    public List<Cliente> getClientesPremium() {
        return List.of();
    }
}
