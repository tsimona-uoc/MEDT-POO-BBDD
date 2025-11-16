package MEDT.MEDT.controlador;

import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.ClientePremium;
import MEDT.MEDT.modelo.excepciones.TipoClienteInvalidoException;

import java.sql.SQLException;
import java.util.List;

public interface IControladorClientes {

    /// Eliminar cliente
    boolean eliminarCliente(String nif);

    /// Add cliente estandar
    boolean addClienteEstandar(String nombre, String domicilio, String nif, String email);

    /// Add cliente premium
    boolean addClientePremium(String nombre, String domicilio, String nif, String email);

    /// Get clientes
    List<Cliente> getClientes();

    /// Get clientes estandar
    List<Cliente> getClientesEstandar();

    /// Get clientes premium
    List<Cliente> getClientesPremium();

    /// Existe cliente
    boolean existeCliente(String nif);

    /// Actualizar cliente
    boolean updateCliente(Cliente cliente);

    /// Get cliente
    Cliente getCliente(String nif);
}