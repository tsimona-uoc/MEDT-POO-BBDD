package MEDT.MEDT.modelo.persistencia;

import MEDT.MEDT.modelo.persistencia.dao.*;
import MEDT.MEDT.modelo.persistencia.mysql.*;

public class DAOFactory {

    public static ArticuloDAO getArticuloDAO() {
        return new ArticuloDAOImpl();
    }

    public static ClienteDAO getClienteDAO() {
        return new ClienteDAOImpl();
    }

    public static PedidoDAO getPedidoDAO() {
        return new PedidoDAOImpl();
    }
}