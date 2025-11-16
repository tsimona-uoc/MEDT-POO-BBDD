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

    @Override
    public void insert(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedido (numeroPedido, fechaHora, cantidad, codigoArticulo, nifCliente) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionUtil.getConnection();
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
        try (Connection conn = ConnectionUtil.getConnection();
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
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, code);
            stmt.executeUpdate();
        }
    }

    @Override
    public Pedido findByCode(int code) throws SQLException {
        Pedido pedido = null;

        String sql = "SELECT * FROM pedido WHERE numeroPedido = ?";
        try (Connection conn = ConnectionUtil.getConnection();
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
        try (Connection conn = ConnectionUtil.getConnection();
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
        try (Connection conn = ConnectionUtil.getConnection();
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

    /// Returns a list of pedidos enviados
    public List<Pedido> findPedidosEnviados(String nif) throws SQLException{
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conn = ConnectionUtil.getConnection()) {
            // 1. Preparar CallableStatement
            CallableStatement cs = conn.prepareCall("{CALL obtenerPedidosEnviados(?)}");

            // 2. Setear el parámetro
            cs.setString(1, nif);  // Ejemplo de categoría

            // 3. Ejecutar
            ResultSet resultados = cs.executeQuery();

            while(resultados.next()){

                /// Datos del pedido
                int numPedido = resultados.getInt("numeroPedido");
                LocalDateTime fechaHora = resultados.getObject("fechaHora", LocalDateTime.class);
                int cantidad = resultados.getInt("cantidad");
                String nifCliente = resultados.getString("nifCliente");
                String codigoArticulo = resultados.getString("codigoArticulo");

                /// Datos del articulo
                String descripcion = resultados.getString("descripcion");
                double precio = resultados.getDouble("precio");
                double gastosEnvio = resultados.getDouble("gastosEnvio");
                int tiempoPreparacion = resultados.getInt("tiempoPreparacion");

                /// Datos del cliente
                String nombre = resultados.getString("nombre");
                String domicilio = resultados.getString("domicilio");
                String email = resultados.getString("email");
                String tipo = resultados.getString("tipo");

                Articulo articulo = new Articulo(codigoArticulo, descripcion, precio, gastosEnvio, tiempoPreparacion);
                Cliente cliente = null;

                if (tipo.equals("estandar")){
                    cliente = new ClienteEstandar(nombre, domicilio, nifCliente, email);
                }
                else{
                    cliente = new ClientePremium(nombre, domicilio, nifCliente, email);
                }


                pedidos.add(new Pedido(numPedido, cantidad, fechaHora, articulo, cliente));
            }
        }

        return pedidos;
    }

    /// Returns a list of pedidos pendientes
    public List<Pedido> findPedidosPendientes(String nif) throws SQLException{
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conn = ConnectionUtil.getConnection()) {
            // 1. Preparar CallableStatement
            CallableStatement cs = conn.prepareCall("{CALL obtenerPedidosPendientes(?)}");

            // 2. Setear el parámetro
            cs.setString(1, nif);  // Ejemplo de categoría

            // 3. Ejecutar
            ResultSet resultados = cs.executeQuery();

            while(resultados.next()){

                /// Datos del pedido
                int numPedido = resultados.getInt("numeroPedido");
                LocalDateTime fechaHora = resultados.getObject("fechaHora", LocalDateTime.class);
                int cantidad = resultados.getInt("cantidad");
                String nifCliente = resultados.getString("nifCliente");
                String codigoArticulo = resultados.getString("codigoArticulo");

                /// Datos del articulo
                String descripcion = resultados.getString("descripcion");
                double precio = resultados.getDouble("precio");
                double gastosEnvio = resultados.getDouble("gastosEnvio");
                int tiempoPreparacion = resultados.getInt("tiempoPreparacion");

                /// Datos del cliente
                String nombre = resultados.getString("nombre");
                String domicilio = resultados.getString("domicilio");
                String email = resultados.getString("email");
                String tipo = resultados.getString("tipo");

                Articulo articulo = new Articulo(codigoArticulo, descripcion, precio, gastosEnvio, tiempoPreparacion);
                Cliente cliente = null;

                if (tipo.equals("estandar")){
                    cliente = new ClienteEstandar(nombre, domicilio, nifCliente, email);
                }
                else{
                    cliente = new ClientePremium(nombre, domicilio, nifCliente, email);
                }

                pedidos.add(new Pedido(numPedido, cantidad, fechaHora, articulo, cliente));
            }
        }

        return pedidos;
    }
    public boolean addPedidoYClienteAtomico(Pedido p, Cliente c) throws SQLException{

        boolean result = false;
        Connection conn = null;

        try {
            conn = ConnectionUtil.getConnection();

            // 1. Desactivar auto-commit (obligatorio para transacciones)
            conn.setAutoCommit(false);

            /// INSERTAR EL CLIENTE PRIMERO
            PreparedStatement insertClientStatement = conn.prepareStatement("INSERT INTO cliente (nif, nombre, domicilio, email, tipo) VALUES (?, ?, ?, ?, ?)");

            /// Fill values
            insertClientStatement.setString(1, c.getNif());
            insertClientStatement.setString(2, c.getNombre());
            insertClientStatement.setString(3, c.getDomicilio());
            insertClientStatement.setString(4, c.getEmail());
            insertClientStatement.setString(5, c instanceof ClienteEstandar ? "estandar" : "premium");
            insertClientStatement.executeUpdate();

            /// INSERTAR PEDIDO
            PreparedStatement insertPedidoStatement = conn.prepareStatement("INSERT INTO pedido (numeroPedido, fechaHora, cantidad, codigoArticulo, nifCliente) VALUES (?, ?, ?, ?, ?)");
            /// Fill values
            insertPedidoStatement.setInt(1, p.getNumPedido());
            insertPedidoStatement.setObject(2, p.getFechaHora());
            insertPedidoStatement.setInt(3, p.getCantidad());
            insertPedidoStatement.setString(4, p.getArticulo().getCodigo());
            insertPedidoStatement.setString(5, p.getCliente().getNif());
            insertPedidoStatement.executeUpdate();

            // 3. Si todo va bien: commit
            conn.commit();
            result = true;

        } catch (SQLException e) {

            // 4. Si algo falla: rollback
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();

        } finally {
            // 5. Restaurar auto-commit y cerrar conexión
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return result;
    }
}
