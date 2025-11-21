package MEDT.MEDT.DAO;

import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.modelo.excepciones.ArticuloNoEncontradoException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class ArticuloDAOJPA implements IArticuloDAO {

    @Override
    public void insert(Articulo articulo) {
        // 1. Obtener un EntityManager
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        // 2. Iniciar una transacción
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // 3. Usar el metodo persist() para guardar el objeto
            em.persist(articulo);
            // 4. Confirmar la transacción
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            // 5. Cerrar el EntityManager
            em.close();
        }
    }

    @Override
    public void update(Articulo articulo) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(articulo);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void eliminar(String codigo) {
        // Lógica con JPA para eliminar
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Articulo articulo = em.find(Articulo.class, codigo);
            if (articulo != null) {
                em.remove(articulo);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }    }

    @Override
    public Articulo buscar(String codigo) throws ArticuloNoEncontradoException {
        // Lógica con JPA para buscar
        return null;
    }

    @Override
    public List<Articulo> obtenerTodos() {
        // Lógica con JPA para obtener todos
        return null;
    }
}
