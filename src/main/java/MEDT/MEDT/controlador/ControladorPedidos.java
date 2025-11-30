package MEDT.MEDT.controlador;

import MEDT.MEDT.modelo.*;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class ControladorPedidos {

    private final EntityManager em;

    public ControladorPedidos(EntityManager em) {
        this.em = em;
    }

    /** Añade un pedido */
    public String addPedido(int numPedido, int cantidad, LocalDateTime fechaHora, String codigoArticulo, String nifCliente) {
        EntityTransaction tx = em.getTransaction();
        try {
            Articulo articulo = em.find(Articulo.class, codigoArticulo);
            if (articulo == null) return "Error: el artículo no existe.";

            Cliente cliente = em.find(Cliente.class, nifCliente);
            if (cliente == null) return "Error: el cliente no existe. Debe crearlo antes de continuar.";

            Pedido pedido = new Pedido(numPedido, fechaHora.toInstant(ZoneOffset.UTC), cantidad, articulo, cliente);

            tx.begin();
            em.persist(pedido);
            tx.commit();

            return "Pedido añadido correctamente.";

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            return "Error: el pedido no se pudo añadir (" + e.getMessage() + ")";
        }
    }

    /** Elimina un pedido si es cancelable */
    public boolean eliminarPedido(int numPedido) throws PedidoNoCancelableException {
        EntityTransaction tx = em.getTransaction();
        try {
            Pedido pedido = em.find(Pedido.class, numPedido);
            if (pedido == null) {
                throw new IllegalArgumentException("No existe ningún pedido con ese número.");
            }
            if (!pedido.esCancelable()) {
                throw new PedidoNoCancelableException("No se puede eliminar el pedido.");
            }

            tx.begin();
            em.remove(pedido);
            tx.commit();

            return true;

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.out.println("Error al eliminar el pedido: " + e.getMessage());
            return false;
        }
    }

    /** Devuelve los pedidos pendientes de un cliente */
    public List<Pedido> getPedidosPendientes(String nif) {
        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("obtenerPedidosPendientes", Pedido.class);

            // Registrar el parámetro de entrada
            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);

            // Pasar el parámetro
            query.setParameter(1, (nif != null ? nif : ""));

            // Ejecutar
            return query.getResultList();

        } catch (PersistenceException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** Devuelve los pedidos enviados de un cliente */
    public List<Pedido> getPedidosEnviados(String nif) {
        try {
            StoredProcedureQuery query = em.createStoredProcedureQuery("obtenerPedidosEnviados", Pedido.class);

            // Registrar el parámetro de entrada
            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);

            // Pasar el parámetro
            query.setParameter(1, (nif != null ? nif : ""));

            // Ejecutar
            return query.getResultList();

        } catch (PersistenceException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** Añade un cliente y un pedido en la misma transacción */
    public boolean addPedidoYClienteAtomico(int numeroPedido, int cantidad, LocalDateTime fechaHora,
                                            String codigoArticulo, String nif, String nombre,
                                            String domicilio, String email, int tipo) {
        EntityTransaction tx = em.getTransaction();
        try {
            Articulo articulo = em.find(Articulo.class, codigoArticulo);
            if (articulo == null) return false;

            Cliente cliente;
            if (tipo == 1) {
                cliente = new ClienteEstandar(nif, nombre, domicilio, email);
            } else {
                cliente = new ClientePremium(nif, nombre, domicilio, email);
            }

            Pedido pedido = new Pedido(numeroPedido, fechaHora.toInstant(ZoneOffset.UTC), cantidad, articulo, cliente);

            tx.begin();
            em.persist(cliente);
            em.persist(pedido);
            tx.commit();

            return true;

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }
}