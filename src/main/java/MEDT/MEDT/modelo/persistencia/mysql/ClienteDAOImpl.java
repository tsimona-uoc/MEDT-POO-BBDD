package MEDT.MEDT.modelo.persistencia.mysql;

import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.ClientePremium;
import MEDT.MEDT.modelo.persistencia.dao.ClienteDAO;
import MEDT.MEDT.modelo.persistencia.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public void insertar(Cliente cliente) {
        String sql = "INSERT INTO cliente (nif, nombre, domicilio, email, tipo_cliente) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cliente.getNif());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getDomicilio());
            ps.setString(4, cliente.getEmail());
            if (cliente instanceof ClientePremium) {
                ps.setString(5, "PREMIUM");
            } else {
                ps.setString(5, "ESTANDAR");
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Cliente obtenerPorNif(String nif) {
        String sql = "SELECT * FROM cliente WHERE nif = ?";
        Cliente cliente = null;
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nif);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cliente = construirCliente(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }

    // Método nuevo
    @Override
    public Cliente obtenerPorEmail(String email) {
        String sql = "SELECT * FROM cliente WHERE email = ?";
        Cliente cliente = null;
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cliente = construirCliente(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }

    @Override
    public List<Cliente> obtenerTodos() {
        return obtenerPorTipo(null);
    }

    @Override
    public List<Cliente> obtenerEstandar() {
        return obtenerPorTipo("ESTANDAR");
    }

    @Override
    public List<Cliente> obtenerPremium() {
        return obtenerPorTipo("PREMIUM");
    }

    private List<Cliente> obtenerPorTipo(String tipo) {
        String sql = "SELECT * FROM cliente";
        if (tipo != null && !tipo.isEmpty()) {
            sql += " WHERE tipo_cliente = ?";
        }
        List<Cliente> clientes = new ArrayList<>();
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (tipo != null && !tipo.isEmpty()) {
                ps.setString(1, tipo);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clientes.add(construirCliente(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    private Cliente construirCliente(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo_cliente");
        String nif = rs.getString("nif");
        String nombre = rs.getString("nombre");
        String domicilio = rs.getString("domicilio");
        String email = rs.getString("email");

        if ("PREMIUM".equals(tipo)) {
            return new ClientePremium(nombre, domicilio, nif, email);
        } else {
            return new ClienteEstandar(nombre, domicilio, nif, email);
        }
    }
}