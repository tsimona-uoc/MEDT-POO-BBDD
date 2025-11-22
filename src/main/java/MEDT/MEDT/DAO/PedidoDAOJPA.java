package MEDT.MEDT.DAO;

import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.Pedido;

import java.sql.SQLException;
import jakarta.persistence.*;
import java.util.Collection;
import java.util.List;

public class PedidoDAOJPA implements IPedidoDAO{

    private final EntityManagerFactory emf;

    public PedidoDAOJPA() {
        this.emf = JPAUtil.getEntityManagerFactory();
    }

    public PedidoDAOJPA(IArticuloDAO articuloDAO, IClienteDAO clienteDAO, EntityManagerFactory emf) {
        this.emf = emf;
    }



    @Override
    public void insert(Pedido pedido) throws SQLException {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(pedido);
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
    public void update(Pedido pedido) throws SQLException {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try{
            em.merge(pedido);
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
    public void delete(int code) throws SQLException {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            Pedido pedido = em.find(Pedido.class, code);
            if (pedido != null) {
                em.remove(pedido);
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
    public Pedido findByCode(int code) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            Pedido pedido = em.find(Pedido.class, code);
            return pedido;
        } finally {
            em.close();
        }
    }

    @Override
    public Collection<Pedido> findAll() throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Pedido p", Pedido.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Collection<Pedido> findByCliente(Cliente cliente) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Pedido p WHERE p.cliente = :cliente", Pedido.class).setParameter("cliente", cliente).getResultList();
        } finally {
            em.close();        }

    }

    @Override
    public List<Pedido> findPedidosEnviados(String nif) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            // Usamos una consulta SQL nativa para aprovechar las funciones de fecha de MySQL.
            // El segundo argumento, Pedido.class, le dice a JPA que mapee los resultados a objetos Pedido.
            String sql = "SELECT p.* FROM pedido p " +
                    "JOIN articulo a ON p.codigoArticulo = a.codigo " +
                    "WHERE p.nifCliente = :nif AND NOW() >= DATE_ADD(p.fechaHora, INTERVAL a.tiempoPreparacion MINUTE)";

            return em.createNativeQuery(sql, Pedido.class)
                    .setParameter("nif", nif)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Pedido> findPedidosPendientes(String nif) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            String sql = "SELECT p.* FROM pedido p " +
                    "JOIN articulo a ON p.codigoArticulo = a.codigo " +
                    "WHERE p.nifCliente = :nif AND NOW() < DATE_ADD(p.fechaHora, INTERVAL a.tiempoPreparacion MINUTE)";

            return em.createNativeQuery(sql, Pedido.class)
                    .setParameter("nif", nif)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean addPedidoYClienteAtomico(Pedido pedido, Cliente cliente) throws SQLException {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(cliente);
            em.persist(pedido);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
}