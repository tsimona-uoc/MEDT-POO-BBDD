package MEDT.MEDT.persistencia.JDBC;

import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.Pedido;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;
import MEDT.MEDT.persistencia.DAO.ArticuloDAO;
import MEDT.MEDT.persistencia.DAO.ClienteDAO;
import MEDT.MEDT.persistencia.DAO.PedidoDAO;
import MEDT.MEDT.persistencia.connection.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoJDBC implements PedidoDAO {

    private ArticuloDAO articuloDAO;
    private ClienteDAO clienteDAO;

    public PedidoJDBC() {
        this.articuloDAO = new ArticuloJDBC();
        this.clienteDAO = new ClienteJDBC();
    }

    @Override
    public boolean addPedido(Pedido pedido) {

        String sql = "INSERT INTO pedido (numPedido, cantidad, fechaHora, articulo, cliente) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConnectionUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, pedido.getNumPedido());
            ps.setInt(2, pedido.getCantidad());
            ps.setObject(3, pedido.getFechaHora());
            ps.setString(4, pedido.getArticulo().getCodigo());
            ps.setString(5, pedido.getCliente().getNif());

            ps.executeUpdate();
            return true;
        }
        catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean eliminarPedido(int numPedido) throws PedidoNoCancelableException {
        String sqlSelect = """
        SELECT p.fechaHora, a.tiempoPrep
        FROM pedido p
        JOIN articulo a ON p.articulo = a.codigo
        WHERE p.numPedido = ?
        """;

        String sqlDelete = "DELETE FROM pedido WHERE numPedido = ?";

        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement psSelect = con.prepareStatement(sqlSelect);
             PreparedStatement psDelete = con.prepareStatement(sqlDelete)) {

            psSelect.setInt(1, numPedido);
            ResultSet rs = psSelect.executeQuery();

            if (!rs.next()) {
                return false;
            }

            LocalDateTime fechaHora = rs.getObject("fechaHora", LocalDateTime.class);
            int tiempoPrep = rs.getInt("tiempoPrep");

            long minutosTranscurridos = Duration.between(fechaHora, LocalDateTime.now()).toMinutes();


            // Comprobamos si se puede eliminar
            if (minutosTranscurridos > tiempoPrep) {
                throw new PedidoNoCancelableException("El pedido ya fue enviado y no puede eliminarse.");
            }

            psDelete.setInt(1, numPedido);
            psDelete.executeUpdate();
            return true;

        } catch (SQLException e) {
            return false;
        }
    }


    @Override
    public List<Pedido> getPedido() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedido";

        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int numPedido = rs.getInt("numPedido");
                int cantidad = rs.getInt("cantidad");
                LocalDateTime fechaHora = rs.getObject("fechaHora", LocalDateTime.class);
                String codigoArticulo = rs.getString("articulo");
                String nifCliente = rs.getString("cliente");

                // Obtener los datos completos del artículo y cliente
                Articulo articulo = articuloDAO.getArticulo(codigoArticulo);
                Cliente cliente = clienteDAO.getCliente(nifCliente);

                // Crear el pedido completo
                Pedido pedido = new Pedido(numPedido, cantidad, fechaHora, articulo, cliente);
                pedidos.add(pedido);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener pedidos: " + e.getMessage());
        }

        return pedidos;
    }



    @Override
    public List<Pedido> getPedidosPendientes(String nif) {
        List<Pedido> todos = getPedido();
        List<Pedido> pendientes = new ArrayList<>();

        for (Pedido p : todos) {
            if (p.esCancelable()) { // si aún no ha pasado el tiempo de preparación
                if (nif == null || nif.isEmpty() || p.getCliente().getNif().equalsIgnoreCase(nif)) {
                    pendientes.add(p);
                }
            }
        }

        return pendientes;
    }

    @Override
    public List<Pedido> getPedidosEnviados(String nif) {
        List<Pedido> todos = getPedido();
        List<Pedido> enviados = new ArrayList<>();

        for (Pedido p : todos) {
            if (!p.esCancelable()) { // si ya pasó el tiempo de preparación
                if (nif == null || nif.isEmpty() || p.getCliente().getNif().equalsIgnoreCase(nif)) {
                    enviados.add(p);
                }
            }
        }

        return enviados;
    }

}
