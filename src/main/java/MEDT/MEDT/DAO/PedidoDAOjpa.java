package MEDT.MEDT.DAO;

import MEDT.MEDT.modelo.*;
import MEDT.MEDT.modelo.excepciones.TipoClienteInvalidoException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/// JDBC implementation of DAO articulo
public class PedidoDAOjpa implements IPedidoDAO {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("MEDT_PU");

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public void insert(Pedido pedido) throws SQLException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pedido);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Pedido pedido) throws SQLException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(pedido);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    @Override
    public void delete(int code) throws SQLException{
        EntityManager em = getEntityManager();
        try{
            em.getTransaction().begin();
            Pedido pedido = em.find(Pedido.class, code);
            if(pedido != null){
                em.remove(pedido);
            }
            em.getTransaction().commit();
        }finally{
            em.close();
        }
    }
    @Override
    public Pedido findByCode(int code) throws SQLException {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pedido.class, code);
        } finally {
            em.close();
        }
    }

    @Override
    public Collection<Pedido> findAll() throws SQLException {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Pedido p", Pedido.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Collection<Pedido> findByCliente(Cliente cliente) throws SQLException {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pedido p Where p.cliente = :cli", Pedido.class)
                    .setParameter("cli", cliente)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Pedido> findPedidosPendientes(String nif) throws SQLException {
        EntityManager em = getEntityManager();
        try {
            List<Pedido> pedidos = em.createQuery(
                            "SELECT p FROM Pedido p WHERE p.cliente.nif = :nif", Pedido.class)
                    .setParameter("nif", nif)
                    .getResultList();
            return pedidos.stream()
                    .filter(Pedido::esCancelable)
                    .toList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Pedido> findPedidosEnviados(String nif) throws SQLException {
        EntityManager em = getEntityManager();
        try {
            List<Pedido> pedidos = em.createQuery(
                            "SELECT p FROM Pedido p WHERE p.cliente.nif = :nif", Pedido.class)
                    .setParameter("nif", nif)
                    .getResultList();
            return pedidos.stream()
                    .filter(p -> !p.esCancelable())
                    .toList();

        } finally {
            em.close();
        }
    }
    @Override
    public boolean addPedidoYClienteAtomico (Pedido pedido, Cliente cliente) throws SQLException{
        EntityManager em = getEntityManager();
        try{
            em.getTransaction().begin();

            Cliente existente = em.find(Cliente.class, cliente.getNif());
            if(existente == null){
                em.persist(cliente);
            }else{
                em.merge(cliente);
            }

            em.persist(pedido);

            em.getTransaction().commit();
            return true;

        }catch (Exception ex){
            em.getTransaction().rollback();
            return false;

        }finally{
            em.close();
        }
    }
}
