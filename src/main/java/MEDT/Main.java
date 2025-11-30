package MEDT;

import MEDT.MEDT.Factory.MEDTFactory;
import MEDT.MEDT.controlador.*;
import MEDT.MEDT.modelo.*;
import MEDT.MEDT.vista.*;

import jakarta.persistence.*;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {

        /// Set a higher level of logging level
        Logger.getLogger("org.hibernate").setLevel(Level.WARNING);

        /// Create entity manager factory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        /// Create an entity manager
        EntityManager em = emf.createEntityManager();
        /// Register the entity manager in MEDT Factory
        MEDTFactory.registerType(EntityManager.class, em);

        /// Create controllers
        ControladorArticulos ca = new ControladorArticulos(MEDTFactory.resolve(EntityManager.class));
        ControladorPedidos cp = new ControladorPedidos(MEDTFactory.resolve(EntityManager.class));
        ControladorClientes cc = new ControladorClientes(MEDTFactory.resolve(EntityManager.class));
        new Vista(cp, ca, cc).menu();
    }
}