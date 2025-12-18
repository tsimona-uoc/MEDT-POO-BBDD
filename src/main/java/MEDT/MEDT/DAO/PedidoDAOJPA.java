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


    @Override
    public void insert(Pedido pedido) throws SQLException {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {

            /// Generar un identificador
            if (pedido.getNumPedido() == 0){
                Integer maxId = em.createQuery("SELECT MAX(e.numPedido) FROM Pedido e", Integer.class).getSingleResult();

                // Si maxId es nulo (tabla vacía), empezamos por 1.
                if (maxId == null) {
                    pedido.setNumPedido(1);
                }
                else {
                    pedido.setNumPedido(maxId + 1);
                }
            }

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
            // 1. Empezamos con la consulta base que define un pedido "enviado"
            StringBuilder sql = new StringBuilder(
                    "SELECT p.* FROM pedido p " +
                            "JOIN articulo a ON p.codigoArticulo = a.codigo " +
                            "WHERE NOW() >= DATE_ADD(p.fechaHora, INTERVAL a.tiempoPreparacion MINUTE)"
            );

            // 2. Si se proporciona un NIF válido, añadimos el filtro
            boolean hasNif = nif != null && !nif.trim().isEmpty();
            if (hasNif) {
                sql.append(" AND p.nifCliente = :nif");
            }

            // 3. Creamos la consulta nativa a partir del SQL construido
            jakarta.persistence.Query query = em.createNativeQuery(sql.toString(), Pedido.class);

            // 4. Si añadimos el filtro, ahora establecemos el valor del parámetro
            if (hasNif) {
                query.setParameter("nif", nif);
            }

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Pedido> findPedidosPendientes(String nif) throws SQLException {
        EntityManager em = emf.createEntityManager();
        try {
            // 1. Consulta base para pedidos "pendientes"
            StringBuilder sql = new StringBuilder(
                    "SELECT p.* FROM pedido p " +
                            "JOIN articulo a ON p.codigoArticulo = a.codigo " +
                            "WHERE NOW() < DATE_ADD(p.fechaHora, INTERVAL a.tiempoPreparacion MINUTE)"
            );

            // 2. Añadir filtro de NIF si existe
            boolean hasNif = nif != null && !nif.trim().isEmpty();
            if (hasNif) {
                sql.append(" AND p.nifCliente = :nif");
            }

            // 3. Crear la consulta
            jakarta.persistence.Query query = em.createNativeQuery(sql.toString(), Pedido.class);

            // 4. Setear el parámetro si es necesario
            if (hasNif) {
                query.setParameter("nif", nif);
            }

            return query.getResultList();
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