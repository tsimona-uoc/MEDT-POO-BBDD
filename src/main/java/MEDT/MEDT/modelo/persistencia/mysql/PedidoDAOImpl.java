package MEDT.MEDT.modelo.persistencia.mysql;

import MEDT.MEDT.modelo.*;
import MEDT.MEDT.modelo.excepciones.ArticuloNoEncontradoException;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;
import MEDT.MEDT.modelo.persistencia.dao.PedidoDAO;
import MEDT.MEDT.modelo.persistencia.util.ConexionBD;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {

    @Override
    public void insertar(Pedido pedido) {
        String sql = "{CALL sp_insertar_pedido(?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionBD.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, pedido.getNumPedido());
            cs.setTimestamp(2, Timestamp.valueOf(pedido.getFechaHora()));
            cs.setInt(3, pedido.getCantidad());
            cs.setString(4, pedido.getCliente().getNif());
            cs.setString(5, pedido.getArticulo().getCodigo());
            cs.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Pedido obtenerPorNumPedido(int numPedido) {
        String sql = "SELECT * FROM pedido p " +
                "JOIN cliente c ON p.id_cliente_fk = c.id_cliente " +
                "JOIN articulo a ON p.id_articulo_fk = a.id_articulo " +
                "WHERE p.num_pedido_usuario = ?";
        Pedido pedido = null;
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, numPedido);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pedido = construirPedido(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedido;
    }

    @Override
    public void eliminar(int numPedido) throws PedidoNoCancelableException, IllegalArgumentException {
        Pedido pedido = obtenerPorNumPedido(numPedido);
        if (pedido == null) {
            throw new IllegalArgumentException("No existe el pedido " + numPedido);
        }
        if (!pedido.esCancelable()) {
            throw new PedidoNoCancelableException(
                    "El pedido " + numPedido + " no puede cancelarse, ya pasó el tiempo de preparación."
            );
        }
        String sql = "DELETE FROM pedido WHERE num_pedido_usuario = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, numPedido);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método actualizado
    @Override
    public List<Pedido> obtenerPendientes(String emailCliente) {
        String sql = "SELECT * FROM pedido p " +
                "JOIN cliente c ON p.id_cliente_fk = c.id_cliente " +
                "JOIN articulo a ON p.id_articulo_fk = a.id_articulo " +
                "WHERE TIMESTAMPDIFF(MINUTE, p.fecha_hora, NOW()) <= a.tiempo_prep";

        if (emailCliente != null && !emailCliente.isEmpty()) {
            sql += " AND c.email = ?";
        }
        return ejecutarQueryListaPedidos(sql, emailCliente);
    }

    // Método actualizado
    @Override
    public List<Pedido> obtenerEnviados(String emailCliente) {
        String sql = "SELECT * FROM pedido p " +
                "JOIN cliente c ON p.id_cliente_fk = c.id_cliente " +
                "JOIN articulo a ON p.id_articulo_fk = a.id_articulo " +
                "WHERE TIMESTAMPDIFF(MINUTE, p.fecha_hora, NOW()) > a.tiempo_prep";

        if (emailCliente != null && !emailCliente.isEmpty()) {
            sql += " AND c.email = ?";
        }
        return ejecutarQueryListaPedidos(sql, emailCliente);
    }

    // Helper actualizado
    private List<Pedido> ejecutarQueryListaPedidos(String sql, String filtro) {
        List<Pedido> pedidos = new ArrayList<>();
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (filtro != null && !filtro.isEmpty()) {
                ps.setString(1, filtro);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(construirPedido(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }

    // Métodos de 'nif' (no usados por la vista, pero funcionan)
    @Override
    public List<Pedido> obtenerPendientesCliente(String nif) {
        String sql = "SELECT * FROM pedido p " +
                "JOIN cliente c ON p.id_cliente_fk = c.id_cliente " +
                "JOIN articulo a ON p.id_articulo_fk = a.id_articulo " +
                "WHERE TIMESTAMPDIFF(MINUTE, p.fecha_hora, NOW()) <= a.tiempo_prep " +
                "AND c.nif = ?";
        return ejecutarQueryListaPedidos(sql, nif);
    }

    @Override
    public List<Pedido> obtenerEnviadosCliente(String nif) {
        String sql = "SELECT * FROM pedido p " +
                "JOIN cliente c ON p.id_cliente_fk = c.id_cliente " +
                "JOIN articulo a ON p.id_articulo_fk = a.id_articulo " +
                "WHERE TIMESTAMPDIFF(MINUTE, p.fecha_hora, NOW()) > a.tiempo_prep " +
                "AND c.nif = ?";
        return ejecutarQueryListaPedidos(sql, nif);
    }

    // Helper para construir el objeto Pedido
    private Pedido construirPedido(ResultSet rs) throws SQLException {
        Articulo articulo = new Articulo(
                rs.getString("codigo"),
                rs.getString("descripcion"),
                rs.getDouble("precio"),
                rs.getDouble("gastos_envio"),
                rs.getInt("tiempo_prep")
        );

        Cliente cliente;
        String tipo = rs.getString("tipo_cliente");
        String nif = rs.getString("nif");
        String nombre = rs.getString("nombre");
        String domicilio = rs.getString("domicilio");
        String email = rs.getString("email");

        if ("PREMIUM".equals(tipo)) {
            cliente = new ClientePremium(nombre, domicilio, nif, email);
        } else {
            cliente = new ClienteEstandar(nombre, domicilio, nif, email);
        }

        int numPedido = rs.getInt("num_pedido_usuario");
        int cantidad = rs.getInt("cantidad");
        LocalDateTime fecha = rs.getTimestamp("fecha_hora").toLocalDateTime();

        // Corrección del constructor
        return new Pedido(numPedido, fecha, cantidad, articulo, cliente);
    }
}