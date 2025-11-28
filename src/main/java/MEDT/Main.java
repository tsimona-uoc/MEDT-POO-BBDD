package MEDT;

import MEDT.MEDT.DAO.*;
import MEDT.MEDT.Factory.MEDTFactory;
import MEDT.MEDT.controlador.ControladorArticulos;
import MEDT.MEDT.controlador.ControladorClientes;
import MEDT.MEDT.controlador.ControladorPedidos;
import MEDT.MEDT.vista.Vista;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {

    public static void main(String[] args) {

        // ============================================================
        // === TEST JPA / HIBERNATE (antes de arrancar la aplicación) ==
        // ============================================================

        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("MEDT_PU");
            EntityManager em = emf.createEntityManager();

            System.out.println("✔ JPA cargó correctamente");
            System.out.println("✔ Hibernate está funcionando");
            System.out.println("✔ persistence.xml es válido");

            em.close();
            emf.close();
        } catch (Exception e) {
            System.out.println("❌ ERROR: JPA NO se ha podido inicializar");
            e.printStackTrace();
        }

        // ============================================================
        // ========== TU APLICACIÓN ORIGINAL (JPA / DAO) ==============
        // ============================================================

        // Register types
        MEDTFactory.registerType(IArticuloDAO.class, new ArticuloDAOjpa());
        MEDTFactory.registerType(IClienteDAO.class, new ClienteDAOjpa());
        MEDTFactory.registerType(IPedidoDAO.class, new PedidoDAOjpa());

        // Create controllers
        ControladorArticulos ca = new ControladorArticulos(MEDTFactory.resolve(IArticuloDAO.class));
        ControladorPedidos cp = new ControladorPedidos(
                MEDTFactory.resolve(IArticuloDAO.class),
                MEDTFactory.resolve(IPedidoDAO.class),
                MEDTFactory.resolve(IClienteDAO.class)
        );
        ControladorClientes cc = new ControladorClientes(MEDTFactory.resolve(IClienteDAO.class));

        // Run app
        new Vista(cp, ca, cc).menu();
    }
}