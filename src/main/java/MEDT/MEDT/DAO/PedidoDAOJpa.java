package MEDT.MEDT.DAO;

import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.Pedido;
// IMPORTANTE: Usamos javax para Hibernate 5
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoDAOJpa implements IPedidoDAO {

    // Constructores vacíos para compatibilidad
    public PedidoDAOJpa() {}
    public PedidoDAOJpa(IArticuloDAO adao, IClienteDAO cdao) {}

    @Override
    public void insert(Pedido pedido) throws SQLException {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(pedido);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            // Imprimimos el error para que veas qué pasa
            e.printStackTrace();
            throw new SQLException("Error JPA insertando pedido", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Pedido pedido) throws SQLException {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(pedido);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new SQLException("Error JPA actualizando pedido", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(int code) throws SQLException {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // Buscamos el objeto por su ID (code)
            Pedido p = em.find(Pedido.class, code);
            if (p != null) {
                em.remove(p);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new SQLException("Error JPA eliminando pedido", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Pedido findByCode(int code) throws SQLException {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Pedido.class, code);
        } finally {
            em.close();
        }
    }

    @Override
    public Collection<Pedido> findAll() throws SQLException {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Pedido p", Pedido.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Collection<Pedido> findByCliente(Cliente cliente) throws SQLException {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Pedido> query = em.createQuery("SELECT p FROM Pedido p WHERE p.cliente.nif = :nif", Pedido.class);
            query.setParameter("nif", cliente.getNif());
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // LÓGICA DE NEGOCIO EN JAVA (Sustituye a los Stored Procedures)
    @Override
    public List<Pedido> findPedidosEnviados(String nif) throws SQLException {
        // 1. Obtenemos los pedidos (todos o filtrados por NIF)
        List<Pedido> todos = buscarPedidosBase(nif);

        // 2. Filtramos en memoria usando Java
        LocalDateTime ahora = LocalDateTime.now();
        return todos.stream()
                .filter(p -> {
                    // Calculamos la fecha de entrega prevista
                    LocalDateTime fechaEntrega = p.getFechaHora().plusMinutes(p.getArticulo().getTiempoPrep());
                    // Si la fecha de entrega es ANTES de ahora, ya está enviado
                    return fechaEntrega.isBefore(ahora);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Pedido> findPedidosPendientes(String nif) throws SQLException {
        // 1. Obtenemos los pedidos
        List<Pedido> todos = buscarPedidosBase(nif);

        // 2. Filtramos en memoria
        LocalDateTime ahora = LocalDateTime.now();
        return todos.stream()
                .filter(p -> {
                    LocalDateTime fechaEntrega = p.getFechaHora().plusMinutes(p.getArticulo().getTiempoPrep());
                    // Si la fecha de entrega es DESPUÉS o IGUAL a ahora, está pendiente
                    return fechaEntrega.isAfter(ahora) || fechaEntrega.isEqual(ahora);
                })
                .collect(Collectors.toList());
    }

    // Método auxiliar para reutilizar código de búsqueda
    private List<Pedido> buscarPedidosBase(String nif) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            if (nif != null && !nif.isEmpty()) {
                return em.createQuery("SELECT p FROM Pedido p WHERE p.cliente.nif = :nif", Pedido.class)
                        .setParameter("nif", nif)
                        .getResultList();
            } else {
                return em.createQuery("SELECT p FROM Pedido p", Pedido.class)
                        .getResultList();
            }
        } finally {
            em.close();
        }
    }

    @Override
    public boolean addPedidoYClienteAtomico(Pedido pedido, Cliente cliente) throws SQLException {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // JPA es inteligente: persistimos el cliente y el pedido en la misma transacción
            em.persist(cliente);
            em.persist(pedido);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
}