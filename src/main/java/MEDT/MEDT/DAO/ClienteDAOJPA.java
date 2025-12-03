package MEDT.MEDT.DAO;

import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.ClientePremium;
import MEDT.MEDT.modelo.excepciones.TipoClienteInvalidoException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.sql.SQLException;
import java.util.Collection;

public class ClienteDAOJPA implements IClienteDAO {

    private final EntityManagerFactory emf;

    public ClienteDAOJPA() {
        this.emf = JPAUtil.getEntityManagerFactory();
    }

    @Override
    public void insert(Cliente cliente) throws SQLException {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(cliente);
            em.getTransaction().commit();
        } catch (Exception e) {
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Cliente cliente) throws SQLException {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.merge(cliente);
            em.getTransaction().commit();
        } catch (Exception e) {
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(String nif) throws SQLException {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            Cliente cliente = em.find(Cliente.class, nif);
            if (cliente != null) {
                em.remove(cliente);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public Cliente findByNIF(String codigo) throws SQLException, TipoClienteInvalidoException {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Cliente.class, codigo);
        } finally {
            em.close();
        }
    }

    @Override
    public Collection<Cliente> findAll() throws SQLException, TipoClienteInvalidoException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Cliente> query = em.createQuery("SELECT c FROM Cliente c", Cliente.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Collection<ClienteEstandar> findStandardClients() throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ClienteEstandar> query = em.createQuery("SELECT c FROM ClienteEstandar c", ClienteEstandar.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Collection<ClientePremium> findPremiumClients() throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ClientePremium> query = em.createQuery("SELECT c FROM ClientePremium c", ClientePremium.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existsClient(String nif) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Cliente.class, nif) != null;
        } finally {
            em.close();
        }
    }
}
