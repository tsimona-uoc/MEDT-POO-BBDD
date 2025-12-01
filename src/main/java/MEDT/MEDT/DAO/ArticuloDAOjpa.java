package MEDT.MEDT.DAO;

import MEDT.MEDT.modelo.Articulo;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.sql.SQLException;
import java.util.Collection;

public class ArticuloDAOjpa implements IArticuloDAO {

    @Override
    public void insert(Articulo articulo) throws SQLException {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(articulo);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new SQLException("Error al insertar artículo JPA", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Articulo articulo) throws SQLException {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(articulo);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new SQLException("Error al actualizar artículo JPA", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(String codigo) throws SQLException {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Articulo a = em.find(Articulo.class, codigo);
            if (a != null) {
                em.remove(a);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new SQLException("Error al eliminar artículo JPA", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Articulo findByCodigo(String codigo) throws SQLException {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Articulo.class, codigo);
        } finally {
            em.close();
        }
    }

    @Override
    public Collection<Articulo> findAll() throws SQLException {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Articulo> query = em.createQuery("SELECT a FROM Articulo a", Articulo.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}