package MEDT;

import MEDT.MEDT.DAO.*;
import MEDT.MEDT.Factory.MEDTFactory;
import MEDT.MEDT.controlador.ControladorArticulos;
import MEDT.MEDT.controlador.ControladorClientes;
import MEDT.MEDT.controlador.ControladorPedidos;
import MEDT.MEDT.vista.Vista;

public class Main {
    public static void main(String[] args) {

        // 1. REGISTRO DE TIPOS
        // Usamos los nombres de clase que TIENES en tu carpeta DAO (los que terminan en jdbc)
        // aunque por dentro tengan código JPA.

        MEDTFactory.registerType(IArticuloDAO.class, new ArticuloDAOjpa());
        MEDTFactory.registerType(IClienteDAO.class, new ClienteDAOjpa());

        // PedidoDAOjdbc ya no necesita argumentos en el constructor porque usa EntityManager internamente
        MEDTFactory.registerType(IPedidoDAO.class, new PedidoDAOJpa());

        // 2. CREACIÓN DE CONTROLADORES (ESTO NO CAMBIA)
        ControladorArticulos ca = new ControladorArticulos(MEDTFactory.resolve(IArticuloDAO.class));
        ControladorPedidos cp = new ControladorPedidos(MEDTFactory.resolve(IArticuloDAO.class), MEDTFactory.resolve(IPedidoDAO.class), MEDTFactory.resolve(IClienteDAO.class));
        ControladorClientes cc = new ControladorClientes(MEDTFactory.resolve(IClienteDAO.class));

        // 3. ARRANQUE DE LA VISTA
        new Vista(cp, ca, cc).menu();
    }
}