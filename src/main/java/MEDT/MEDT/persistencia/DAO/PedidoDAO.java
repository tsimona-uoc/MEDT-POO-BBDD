package MEDT.MEDT.persistencia.DAO;

import MEDT.MEDT.modelo.Pedido;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;
import MEDT.MEDT.modelo.excepciones.ArticuloNoEncontradoException;

import java.util.List;


public interface PedidoDAO {

    boolean addPedido(Pedido pedido) throws ArticuloNoEncontradoException;
    boolean eliminarPedido(int numPedido) throws PedidoNoCancelableException;
    List<Pedido> getPedido();
    List<Pedido> getPedidosPendientes(String nif);
    List<Pedido> getPedidosEnviados(String nif);
}
