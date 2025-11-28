package MEDT.MEDT.DAO;

import MEDT.MEDT.modelo.Articulo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.sql.*;
import java.util.Collection;

/// JPA implementation of DAO articulo
public class ArticuloDAOjpa implements IArticuloDAO {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("MEDT_PU");

    private EntityManager getEntityManager(){
        return emf.createEntityManager();
    }
    @Override
    public void insert(Articulo articulo) throws SQLException{
        EntityManager em = getEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(articulo);
            em.getTransaction().commit();
        }finally{
            em.close();
        }
    }
    @Override
    public void update(Articulo articulo) throws SQLException{
        EntityManager em = getEntityManager();
        try{
            em.getTransaction().begin();
            em.merge(articulo);
            em.getTransaction().commit();
        }finally{
            em.close();
        }
    }
    @Override
    public void delete(String codigo) throws SQLException{
        EntityManager em = getEntityManager();
        try{
            em.getTransaction().begin();
            Articulo articulo = em.find(Articulo.class, codigo);
            if(articulo != null){
                em.remove(articulo);
            }
            em.getTransaction().commit();
        }finally{
            em.close();
        }
    }
    @Override
    public Articulo findByCodigo(String codigo) throws SQLException{
        EntityManager em = getEntityManager();
        try{
            return em.find(Articulo.class, codigo);
        }finally{
            em.close();
        }
    }
    @Override
    public Collection<Articulo> findAll() throws SQLException{
        EntityManager em = getEntityManager();
        try{
            return em
                    .createQuery("Select a FROM Articulo a", Articulo.class)
                    .getResultList();
        }finally{
            em.close();
        }
    }
}

