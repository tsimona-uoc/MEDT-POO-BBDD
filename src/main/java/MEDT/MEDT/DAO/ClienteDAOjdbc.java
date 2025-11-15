package MEDT.MEDT.DAO;

import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.ClientePremium;
import MEDT.MEDT.modelo.excepciones.TipoClienteInvalidoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/// JDBC implementation of DAO articulo
public class ClienteDAOjdbc implements IClienteDAO {

    /// Get a connection
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/MEDT_POO_DDBB", "root", "password");
    }

    /// Insert a new item
    @Override
    public void insert(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nif, nombre, domicilio, email, tipo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNif());
            stmt.setString(2, cliente.getNombre());
            stmt.setString(3, cliente.getDomicilio());
            stmt.setString(4, cliente.getEmail());
            stmt.setString(5, cliente instanceof ClienteEstandar ? "estandar" : "premium");
            stmt.executeUpdate();
        }
    }

    /// Update a given item
    @Override
    public void update(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET nombre = ?, domicilio = ?, email = ?, tipo = ? WHERE nif = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            /// UPDATE fields
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getDomicilio());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente instanceof ClienteEstandar ? "estandar" : "premium");

            /// WHERE fields
            stmt.setString(5, cliente.getNif());
            stmt.executeUpdate();
        }
    }

    /// Delete a given item by code
    @Override
    public void delete(String nif) throws SQLException {
        String sql = "DELETE FROM cliente WHERE nif = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            /// WHERE parameters
            stmt.setString(1, nif);
            stmt.executeUpdate();
        }
    }

    /// Find an item by code
    @Override
    public Cliente findByNIF(String nif) throws SQLException, TipoClienteInvalidoException {
        Cliente cliente = null;

        String sql = "SELECT * FROM cliente WHERE nif = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nif);
            ResultSet resultados = stmt.executeQuery();

            while(resultados.next()){

                String nombre = resultados.getString("nombre");
                String domicilio = resultados.getString("domicilio");
                String email = resultados.getString("email");
                String tipo = resultados.getString("tipo");

                cliente = switch (tipo) {
                    case "estandar" -> new ClienteEstandar(nombre, domicilio, nif, email);
                    case "premium" -> new ClientePremium(nombre, domicilio, nif, email);
                    default -> throw new TipoClienteInvalidoException("");
                };
            }
        }

        return cliente;
    }

    /// Returns a list of all items
    @Override
    public Collection<Cliente> findAll() throws SQLException, TipoClienteInvalidoException {
        List<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT * FROM cliente";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet resultados = stmt.executeQuery();

            while(resultados.next()){

                String nif = resultados.getString("nif");
                String nombre = resultados.getString("nombre");
                String domicilio = resultados.getString("domicilio");
                String email = resultados.getString("email");
                String tipo = resultados.getString("tipo");

                Cliente cliente = switch (tipo) {
                    case "estandar" -> new ClienteEstandar(nombre, domicilio, nif, email);
                    case "premium" -> new ClientePremium(nombre, domicilio, nif, email);
                    default -> throw new TipoClienteInvalidoException("");
                };

                clientes.add(cliente);
            }
        }

        return clientes;
    }

    @Override
    public Collection<ClienteEstandar> findStandardClients() throws SQLException {
        List<ClienteEstandar> clientes = new ArrayList<>();

        String sql = "SELECT * FROM cliente WHERE tipo = 'estandar'";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet resultados = stmt.executeQuery();

            while(resultados.next()){

                String nif = resultados.getString("nif");
                String nombre = resultados.getString("nombre");
                String domicilio = resultados.getString("domicilio");
                String email = resultados.getString("email");
                String tipo = resultados.getString("tipo");

                clientes.add(new ClienteEstandar(nif, nombre, domicilio, email));
            }
        }

        return clientes;
    }

    @Override
    public Collection<ClientePremium> findPremiumClients() throws SQLException {
        List<ClientePremium> clientes = new ArrayList<>();

        String sql = "SELECT * FROM cliente WHERE tipo = 'premium'";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet resultados = stmt.executeQuery();

            while(resultados.next()){

                String nif = resultados.getString("nif");
                String nombre = resultados.getString("nombre");
                String domicilio = resultados.getString("domicilio");
                String email = resultados.getString("email");
                String tipo = resultados.getString("tipo");

                clientes.add(new ClientePremium(nif, nombre, domicilio, email));
            }
        }

        return clientes;
    }

    @Override
    public boolean existsClient(String nif) throws SQLException {
        int count = 0;

        String sql = "SELECT count(*) FROM cliente WHERE nif = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nif);
            ResultSet resultados = stmt.executeQuery();

            while(resultados.next()){
                count = resultados.getInt(1);
            }
        }

        return count > 0;
    }
}
