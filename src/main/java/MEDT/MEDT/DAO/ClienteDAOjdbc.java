package MEDT.MEDT.DAO;

import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.ClientePremium;
import MEDT.MEDT.util.DBConnection;
import com.mysql.cj.protocol.Resultset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOjdbc implements IClienteDAO {
    @Override
    public void insert(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nif, nombre, domicilio, email, tipo, cuota, descuento) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cliente.getNif());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getDomicilio());
            ps.setString(4, cliente.getEmail());
            if (cliente instanceof ClientePremium premium){
                ps.setString(5, "PREMIUM");
                ps.setDouble(6, premium.getCuota());
                ps.setDouble(7, premium.getDescuento());
            }else if (cliente instanceof ClienteEstandar){
                ps.setString(5, "ESTANDAR");
                ps.setNull(6, java.sql.Types.DOUBLE);
                ps.setNull(7, java.sql.Types.DOUBLE);
            }
            ps.executeUpdate();
        }
    }
    @Override
    public Cliente findByNif(String nif) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE nif = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setString(1, nif);
                try (ResultSet rs = ps.executeQuery()){
                    if(!rs.next()) return null;

                    return construirCliente(rs);
                }
        }
    }
    @Override
    public List<Cliente> findAll() throws SQLException{
        String sql = "SELECT * FROM cliente";
        List<Cliente> lista = new ArrayList<>();

        try(Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()){
            while (rs.next()){
                lista.add(construirCliente(rs));
            }
        }
        return lista;
    }
    @Override
    public List<Cliente> findEstandar() throws SQLException {
        String sql = "SELECT * FROM cliente WHERE tipo = 'ESTANDAR'";
        return obtenerListaClientes(sql);
    }
    @Override
    public List<Cliente> findPremium() throws SQLException{
        String sql = "SELECT * FROM cliente WHERE tipo = 'PREMIUM'";
        return obtenerListaClientes(sql);
    }
    private List<Cliente> obtenerListaClientes(String sql) throws SQLException{
        List<Cliente> lista = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()){
            while (rs.next()){
                lista.add(construirCliente(rs));
            }
        }
        return lista;
    }
    private Cliente construirCliente(ResultSet rs) throws SQLException{
        String nif = rs.getString("nif");
        String nombre = rs.getString("nombre");
        String domicilio = rs.getString("domicilio");
        String email = rs.getString("email");
        String tipo = rs.getString("tipo");

        if(tipo.equals("ESTANDAR")){
            return new ClienteEstandar(nombre, domicilio, nif, email);
        }else{
            return new ClientePremium(nombre, domicilio, nif, email);
        }
    }
}
