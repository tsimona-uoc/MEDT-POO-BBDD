package MEDT.MEDT.DAO;

import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.ClientePremium;
import MEDT.MEDT.modelo.excepciones.TipoClienteInvalidoException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/// JDBC implementation of DAO articulo
public class ClienteDAOjpa implements IClienteDAO {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("MEDT_PU");

    private EntityManager getEntityManager(){
        return emf.createEntityManager();
    }

    @Override
    public void insert(Cliente cliente) throws SQLException{
        EntityManager em = getEntityManager();
        try{
            em.getTransaction().begin();

            Cliente existente = em.find(Cliente.class, cliente.getNif());

            if(existente == null){
                em.persist(cliente);
            }else{
                em.merge(cliente);
            }
            em.getTransaction().commit();
        }finally {
            em.close();
        }
    }
    @Override
    public void update(Cliente cliente) throws SQLException{
        EntityManager em = getEntityManager();
        try{
            em.getTransaction().begin();
            em.merge(cliente);
            em.getTransaction().commit();
        }finally{
            em.close();
        }
    }
    @Override
    public void delete(String nif) throws SQLException{
        EntityManager em = getEntityManager();
        try{
            em.getTransaction().begin();
            Cliente cliente = em.find(Cliente.class, nif);

            if(cliente != null){
                em.remove(cliente);
            }
            em.getTransaction().commit();
        }finally{
            em.close();
        }
    }
    @Override
    public Cliente findByNIF(String nif) throws SQLException{
        EntityManager em = getEntityManager();
        try{
            return em.find(Cliente.class, nif);
        }finally{
            em.close();
        }
    }
    @Override
    public Collection<Cliente> findAll() throws SQLException{
        EntityManager em = getEntityManager();
        try{
            return em.createQuery("SELECT c FROM Cliente c", Cliente.class)
                    .getResultList();
        }finally{
            em.close();
        }
    }
    @Override
    public Collection<ClienteEstandar> findStandardClients() throws SQLException {
        EntityManager em = getEntityManager();
        try{
            return em.createQuery(
                    "SELECT c FROM ClienteEstandar c", ClienteEstandar.class)
                    .getResultList();
        }finally{
            em.close();
        }
    }
    @Override
    public Collection<ClientePremium> findPremiumClients() throws SQLException{
        EntityManager em = getEntityManager();
        try{
            return em.createQuery(
                    "SELECT c FROM ClientePremium c", ClientePremium.class)
                    .getResultList();
        }finally{
            em.close();
        }
    }
    @Override
    public boolean existsClient(String nif) throws SQLException{
        EntityManager em = getEntityManager();
        try{
            Long count = em.createQuery(
                    "SELECT COUNT(c) FROM Cliente c WHERE c.nif = :nif", Long.class)
                    .setParameter("nif", nif)
                    .getSingleResult();
            return count > 0;
        }finally{
            em.close();
        }
    }
}
