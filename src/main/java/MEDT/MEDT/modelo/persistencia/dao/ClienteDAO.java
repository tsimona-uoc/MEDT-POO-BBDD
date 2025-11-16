package MEDT.MEDT.modelo.persistencia.dao;

import MEDT.MEDT.modelo.Cliente;
import java.util.List;

public interface ClienteDAO {
    void insertar(Cliente cliente);
    Cliente obtenerPorNif(String nif);
    Cliente obtenerPorEmail(String email); // Método nuevo
    List<Cliente> obtenerTodos();
    List<Cliente> obtenerEstandar();
    List<Cliente> obtenerPremium();
}