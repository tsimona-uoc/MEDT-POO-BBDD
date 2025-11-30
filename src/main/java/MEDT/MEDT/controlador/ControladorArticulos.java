package MEDT.MEDT.controlador;

import MEDT.MEDT.modelo.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ControladorArticulos {

    /// Articulos DAO
    private final EntityManager entityManager;

    /// Constructor de controlador articulo
    public ControladorArticulos(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /// Inserta un nuevo articulo en la BBDD
    public boolean addArticulo(String codigo, String descripcion, double precio, double gastosEnvio, int tiempoPrep) {
        EntityTransaction tx = this.entityManager.getTransaction();
        try {
            tx.begin();
            Articulo articulo = new Articulo(codigo, descripcion, new BigDecimal(precio), new BigDecimal(gastosEnvio), tiempoPrep);
            this.entityManager.persist(articulo);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    /// Devuelve todos los articulos de la tabla
    public List<Articulo> getArticulos() {
        TypedQuery<Articulo> query = this.entityManager.createQuery("SELECT art FROM Articulo art", Articulo.class);
        return query.getResultList();
    }
}
