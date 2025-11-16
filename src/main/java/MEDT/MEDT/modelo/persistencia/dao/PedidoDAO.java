package MEDT.MEDT.modelo.persistencia.dao;

import MEDT.MEDT.modelo.Pedido;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;
import java.util.List;

public interface PedidoDAO {
    void insertar(Pedido pedido);
    Pedido obtenerPorNumPedido(int numPedido);
    void eliminar(int numPedido) throws PedidoNoCancelableException, IllegalArgumentException;

    // Métodos actualizados para filtrar por email
    List<Pedido> obtenerPendientes(String emailCliente);
    List<Pedido> obtenerEnviados(String emailCliente);

    // Mantenemos estos por si se usan en el futuro
    List<Pedido> obtenerPendientesCliente(String nif);
    List<Pedido> obtenerEnviadosCliente(String nif);
}