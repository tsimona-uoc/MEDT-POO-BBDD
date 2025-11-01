package MEDT.MEDT.persistencia.JDBC;

import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.ClientePremium;
import MEDT.MEDT.persistencia.connection.ConnectionUtil;
import MEDT.MEDT.persistencia.DAO.ClienteDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import  java.util.List;

public class ClienteJDBC implements ClienteDAO {

    @Override
    public boolean addCliente(Cliente cliente) {
        String sql = "INSERT INTO cliente (nombre, domicilio, nif, email, tipo) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConnectionUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDomicilio());
            ps.setString(3, cliente.getNif());
            ps.setString(4, cliente.getEmail());
            ps.setString(5, cliente.getTipo());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al insertar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Cliente getCliente(String nif) {
        String sql = "SELECT * FROM cliente WHERE nif = ?";

        try (Connection con = ConnectionUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, nif);
            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                // Crear el objeto Cliente con los datos del registro
                String nombre = rs.getString("nombre");
                String domicilio = rs.getString("domicilio");
                String email = rs.getString("email");
                String tipo = rs.getString("tipo");

                // Crear objeto según el tipo
                if ("Premium".equalsIgnoreCase(tipo)) {
                    return new ClientePremium(nombre, domicilio, nif, email);
                } else {
                    return new ClienteEstandar(nombre, domicilio, nif, email);
                }
            } else {
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar cliente: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Cliente> getClientes() {
        String sql = "SELECT * FROM cliente";
        List<Cliente> clientes = new ArrayList<>();

        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String nif = rs.getString("nif");
                String nombre = rs.getString("nombre");
                String domicilio = rs.getString("domicilio");
                String email = rs.getString("email");
                String tipo = rs.getString("tipo");

                Cliente cliente;
                if ("Premium".equalsIgnoreCase(tipo)) {
                    cliente = new ClientePremium(nombre, domicilio, nif, email);
                } else {
                    cliente = new ClienteEstandar(nombre, domicilio, nif, email);
                }

                clientes.add(cliente);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar clientes: " + e.getMessage());
        }

        return clientes;
    }

    @Override
    public List<Cliente> getClientesEstandar() {
        String sql = "SELECT * FROM cliente WHERE tipo = 'Estandar'";
        List<Cliente> clientes = new ArrayList<>();

        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String nif = rs.getString("nif");
                String nombre = rs.getString("nombre");
                String domicilio = rs.getString("domicilio");
                String email = rs.getString("email");
                String tipo = rs.getString("tipo");

                Cliente cliente = new ClienteEstandar(nombre, domicilio, nif, email);
                clientes.add(cliente);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar clientes: " + e.getMessage());
        }

        return clientes;
    }

    @Override
    public List<Cliente> getClientesPremium() {
        String sql = "SELECT * FROM cliente WHERE tipo = 'Premium'";
        List<Cliente> clientes = new ArrayList<>();

        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String nif = rs.getString("nif");
                String nombre = rs.getString("nombre");
                String domicilio = rs.getString("domicilio");
                String email = rs.getString("email");
                String tipo = rs.getString("tipo");

                Cliente cliente = new ClientePremium(nombre, domicilio, nif, email);
                clientes.add(cliente);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar clientes: " + e.getMessage());
        }

        return clientes;
    }
}
