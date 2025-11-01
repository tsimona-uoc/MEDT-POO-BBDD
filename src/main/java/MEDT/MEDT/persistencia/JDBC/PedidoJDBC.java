package MEDT.MEDT.persistencia.JDBC;

import MEDT.MEDT.modelo.Pedido;
import MEDT.MEDT.persistencia.DAO.PedidoDAO;
import MEDT.MEDT.persistencia.connection.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import  java.util.List;

public class PedidoJDBC implements PedidoDAO {
    @Override
    public boolean addPedido(Pedido pedido) {

        String sql = "INSERT INTO pedido (numPedido, cantidad, fechaHora, articulo, cliente) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConnectionUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);) {

            ps.setInt(1, pedido.getNumPedido());
            ps.setInt(2, pedido.getCantidad());
            ps.setObject(3, pedido.getFechaHora());
            ps.setString(4, pedido.getArticulo().getCodigo());
            ps.setString(5, pedido.getCliente().getNif());

            ps.executeUpdate();
            return true;
        }
        catch (SQLException e) {
            System.out.println("Error al insertar pedido: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarPedido(int numPedido) {
        return false;
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

    @Override
    public void cancelarPedido(Pedido pedido) {

    }
}
