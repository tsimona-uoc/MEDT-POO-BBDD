package MEDT.MEDT.persistencia.DAO;

import MEDT.MEDT.modelo.Pedido;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;

import java.util.List;


public interface PedidoDAO {

    boolean addPedido(Pedido pedido);
    boolean eliminarPedido(int numPedido) throws PedidoNoCancelableException;
    Pedido getPedido(int numPedido);
    List<Pedido> getPedidosPendientes(String nif);
    List<Pedido> getPedidosEnviados(String nif);


}
