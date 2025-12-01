package MEDT.MEDT.DAO;

import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.ClientePremium;
import MEDT.MEDT.modelo.excepciones.TipoClienteInvalidoException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.sql.SQLException;
import java.util.Collection;

public class ClienteDAOjpa implements IClienteDAO {

    @Override
    public void insert(Cliente cliente) throws SQLException {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(cliente);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new SQLException("Error insertando cliente JPA", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Cliente cliente) throws SQLException {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(cliente);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new SQLException("Error actualizando cliente JPA", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(String nif) throws SQLException {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Cliente c = em.find(Cliente.class, nif);
            if (c != null) {
                em.remove(c);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new SQLException("Error borrando cliente JPA", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Cliente findByNIF(String nif) throws SQLException, TipoClienteInvalidoException {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Cliente.class, nif);
        } finally {
            em.close();
        }
    }

    @Override
    public Collection<Cliente> findAll() throws SQLException, TipoClienteInvalidoException {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Collection<ClienteEstandar> findStandardClients() throws SQLException {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM ClienteEstandar c", ClienteEstandar.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Collection<ClientePremium> findPremiumClients() throws SQLException {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM ClientePremium c", ClientePremium.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existsClient(String nif) throws SQLException {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Cliente c = em.find(Cliente.class, nif);
            return (c != null);
        } finally {
            em.close();
        }
    }
}