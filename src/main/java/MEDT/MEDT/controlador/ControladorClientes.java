package MEDT.MEDT.controlador;

import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.ClientePremium;
import MEDT.MEDT.modelo.excepciones.TipoClienteInvalidoException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.PersistenceException;

import java.util.List;

public class ControladorClientes {

    private final EntityManager em;

    public ControladorClientes(EntityManager em) {
        this.em = em;
    }

    /** Añade un cliente estándar */
    public boolean addClienteEstandar(String nombre, String domicilio, String nif, String email) {
        ClienteEstandar cliente = new ClienteEstandar(nombre, domicilio, nif, email);
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(cliente);
            tx.commit();
            return true;
        } catch (PersistenceException e) {
            if (tx.isActive()) tx.rollback();
            System.out.println("Error al insertar el cliente: " + e.getMessage());
            return false;
        }
    }

    /** Añade un cliente premium */
    public boolean addClientePremium(String nombre, String domicilio, String nif, String email) {
        ClientePremium cliente = new ClientePremium(nombre, domicilio, nif, email);
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(cliente);
            tx.commit();
            return true;
        } catch (PersistenceException e) {
            if (tx.isActive()) tx.rollback();
            System.out.println("Error al insertar el cliente: " + e.getMessage());
            return false;
        }
    }

    /** Devuelve todos los clientes */
    public List<Cliente> getClientes() {
        try {
            TypedQuery<Cliente> query = em.createQuery("SELECT c FROM Cliente c", Cliente.class);
            return query.getResultList();
        } catch (PersistenceException e) {
            System.out.println("Error al obtener los clientes: " + e.getMessage());
            return null;
        }
    }

    /** Devuelve todos los clientes estándar */
    public List<Cliente> getClientesEstandar() {
        try {
            TypedQuery<Cliente> query = em.createQuery(
                    "SELECT c FROM ClienteEstandar c", Cliente.class
            );
            return query.getResultList();
        } catch (PersistenceException e) {
            System.out.println("Error al obtener los clientes estándar: " + e.getMessage());
            return null;
        }
    }

    /** Devuelve todos los clientes premium */
    public List<Cliente> getClientesPremium() {
        try {
            TypedQuery<Cliente> query = em.createQuery(
                    "SELECT c FROM ClientePremium c", Cliente.class
            );
            return query.getResultList();
        } catch (PersistenceException e) {
            System.out.println("Error al obtener los clientes premium: " + e.getMessage());
            return null;
        }
    }

    /** Comprueba si un cliente existe por su NIF */
    public boolean existeCliente(String nif) {
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(c) FROM Cliente c WHERE c.nif = :nif", Long.class )
                    .setParameter("nif", nif)
                    .getSingleResult();
            return count > 0;
        } catch (PersistenceException e) {
            System.out.println("Error al comprobar existencia del cliente: " + e.getMessage());
            return false;
        }
    }

    /** Buscar un cliente por NIF */
    public Cliente findByNIF(String nif) throws TipoClienteInvalidoException {
        Cliente cliente = em.find(Cliente.class, nif);
        if (cliente == null) throw new TipoClienteInvalidoException("Cliente no encontrado");
        return cliente;
    }
}
