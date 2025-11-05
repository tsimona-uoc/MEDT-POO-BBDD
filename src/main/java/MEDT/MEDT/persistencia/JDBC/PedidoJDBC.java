package MEDT.MEDT.persistencia.JDBC;

import MEDT.MEDT.modelo.Pedido;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;
import MEDT.MEDT.persistencia.DAO.PedidoDAO;
import MEDT.MEDT.persistencia.connection.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import  java.util.List;

public class PedidoJDBC implements PedidoDAO {
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
    public Pedido getPedido(int numPedido) {
        return null;
    }

    @Override
    public List<Pedido> getPedidosPendientes(String nif) {
        return List.of();
    }

    @Override
    public List<Pedido> getPedidosEnviados(String nif) {
        return List.of();
    }
}
