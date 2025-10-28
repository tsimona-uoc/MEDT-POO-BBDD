package MEDT.MEDT.DAO;

import MEDT.MEDT.modelo.*;
import MEDT.MEDT.modelo.excepciones.TipoClienteInvalidoException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/// JDBC implementation of DAO articulo
public class PedidoDAOjdbc implements IPedidoDAO {

    private IArticuloDAO articuloDAO;
    private IClienteDAO clienteDAO;

    public PedidoDAOjdbc(IArticuloDAO articuloDAO, IClienteDAO clienteDAO) {
        this.articuloDAO = articuloDAO;
        this.clienteDAO = clienteDAO;
    }

    /// Get a connection
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/MEDT_POO_DDBB", "root", "password");
    }

    @Override
    public void insert(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedido (numeroPedido, fechaHora, cantidad, codigoArticulo, nifCliente) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pedido.getNumPedido());
            stmt.setObject(2, pedido.getFechaHora());
            stmt.setInt(3, pedido.getCantidad());
            stmt.setString(4, pedido.getArticulo().getCodigo());
            stmt.setString(5, pedido.getCliente().getNif());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(Pedido pedido) throws SQLException {
        String sql = "UPDATE pedido SET fechaHora = ?, cantidad = ?, codigoArticulo = ?, nifCliente = ? WHERE numeroPedido = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            /// SET parameters
            stmt.setObject(1, pedido.getFechaHora());
            stmt.setInt(2, pedido.getCantidad());
            stmt.setString(3, pedido.getArticulo().getCodigo());
            stmt.setString(4, pedido.getCliente().getNif());

            /// WHERE parameters
            stmt.setInt(5, pedido.getNumPedido());

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int code) throws SQLException {
        String sql = "DELETE FROM pedido WHERE numeroPedido = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, code);
            stmt.executeUpdate();
        }
    }

    @Override
    public Pedido findByCode(int code) throws SQLException {
        Pedido pedido = null;

        String sql = "SELECT * FROM pedido WHERE numeroPedido = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, code);
            ResultSet resultados = stmt.executeQuery();

            while(resultados.next()){

                LocalDateTime fechaHora = resultados.getObject("fechaHora", LocalDateTime.class);
                int cantidad = resultados.getInt("cantidad");
                String nifCliente = resultados.getString("nifCliente");
                String codigoArticulo = resultados.getString("codigoArticulo");

                Cliente cliente = null;
                Articulo articulo = null;
                try {
                    cliente = this.clienteDAO.findByNIF(nifCliente);
                    articulo = this.articuloDAO.findByCodigo(codigoArticulo);
                } catch (TipoClienteInvalidoException e) {
                    throw new RuntimeException(e);
                }

                pedido = new Pedido(code, cantidad, fechaHora, articulo, cliente);
            }
        }

        return pedido;
    }

    @Override
    public Collection<Pedido> findAll() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();

        String sql = "SELECT * FROM pedido";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet resultados = stmt.executeQuery();

            while(resultados.next()){

                int numPedido = resultados.getInt("numeroPedido");
                LocalDateTime fechaHora = resultados.getObject("fechaHora", LocalDateTime.class);
                int cantidad = resultados.getInt("cantidad");
                String nifCliente = resultados.getString("nifCliente");
                String codigoArticulo = resultados.getString("codigoArticulo");

                Cliente cliente = null;
                Articulo articulo = null;
                try {
                    cliente = this.clienteDAO.findByNIF(nifCliente);
                    articulo = this.articuloDAO.findByCodigo(codigoArticulo);
                } catch (TipoClienteInvalidoException e) {
                    throw new RuntimeException(e);
                }

                pedidos.add(new Pedido(numPedido, cantidad, fechaHora, articulo, cliente));
            }
        }

        return pedidos;
    }

    @Override
    public Collection<Pedido> findByCliente(Cliente cliente) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();

        String sql = "SELECT * FROM pedido WHERE nifCliente = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNif());
            ResultSet resultados = stmt.executeQuery();

            while(resultados.next()){

                int numPedido = resultados.getInt("numeroPedido");
                LocalDateTime fechaHora = resultados.getObject("fechaHora", LocalDateTime.class);
                int cantidad = resultados.getInt("cantidad");
                String nifCliente = resultados.getString("nifCliente");
                String codigoArticulo = resultados.getString("codigoArticulo");

                Cliente c = null;
                Articulo a = null;
                try {
                    c = this.clienteDAO.findByNIF(nifCliente);
                    a = this.articuloDAO.findByCodigo(codigoArticulo);
                } catch (TipoClienteInvalidoException e) {
                    throw new RuntimeException(e);
                }

                pedidos.add(new Pedido(numPedido, cantidad, fechaHora, a, cliente));
            }
        }

        return pedidos;
    }

}
