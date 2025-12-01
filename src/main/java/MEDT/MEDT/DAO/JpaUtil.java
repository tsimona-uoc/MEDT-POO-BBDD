package MEDT.MEDT.DAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtil {
    private static final EntityManagerFactory emf;

    static {
        try {
            // "TiendaOnlinePU" debe ser el mismo nombre que pusiste en persistence.xml
            emf = Persistence.createEntityManagerFactory("TiendaOnlinePU");
        } catch (Throwable ex) {
            System.err.println("Fallo al inicializar EntityManagerFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}