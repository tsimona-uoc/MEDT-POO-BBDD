package MEDT.MEDT.controlador;

import MEDT.MEDT.DAO.IClienteDAO;
import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.ClientePremium;
import MEDT.MEDT.modelo.excepciones.TipoClienteInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Habilita el uso de Mockito en JUnit 5
@ExtendWith(MockitoExtension.class)
class ControladorClientesTest {

    // 1. Mock de la dependencia (la interfaz DAO)
    @Mock
    private IClienteDAO clienteDAO;

    // 2. Inyección del Mock en el controlador a probar
    @InjectMocks
    private ControladorClientes controlador;

    // --- Variables de Prueba ---
    private ClienteEstandar clienteEstandar1;
    private ClientePremium clientePremium1;

    @BeforeEach
    void setUp() {
        // Inicializar clientes de prueba
        clienteEstandar1 = new ClienteEstandar("Ana Estandar", "Calle A", "111A", "ana@estandar.com");
        clientePremium1 = new ClientePremium("Beto Premium", "Avenida B", "222B", "beto@premium.com");
    }

    // ====================================================================
    //              TESTS para la inserción (addClienteEstandar/Premium)
    // ====================================================================

    @Test
    @DisplayName("addClienteEstandar - Inserción exitosa")
    void testAddClienteEstandar_Exito() throws SQLException {
        // Configurar el Mock para que la inserción no lance excepción
        doNothing().when(clienteDAO).insert(any(ClienteEstandar.class));

        // Ejecutar y verificar
        boolean resultado = controlador.addClienteEstandar(
                clienteEstandar1.getNombre(), clienteEstandar1.getDomicilio(),
                clienteEstandar1.getNif(), clienteEstandar1.getEmail()
        );

        assertTrue(resultado, "Debe devolver true si la inserción es exitosa.");
        verify(clienteDAO, times(1)).insert(any(ClienteEstandar.class));
    }

    @Test
    @DisplayName("addClienteEstandar - Devuelve false si ocurre una SQLException")
    void testAddClienteEstandar_ErrorSQL() throws SQLException {
        // Configurar el Mock para que lance una SQLException (simulando, por ejemplo, NIF duplicado)
        doThrow(new SQLException("Error: NIF duplicado")).when(clienteDAO).insert(any(ClienteEstandar.class));

        // Ejecutar y verificar
        boolean resultado = controlador.addClienteEstandar(
                clienteEstandar1.getNombre(), clienteEstandar1.getDomicilio(),
                clienteEstandar1.getNif(), clienteEstandar1.getEmail()
        );

        assertFalse(resultado, "Debe devolver false si ocurre una excepción SQL.");
    }

    @Test
    @DisplayName("addClientePremium - Inserción exitosa")
    void testAddClientePremium_Exito() throws SQLException {
        // Configurar el Mock para que la inserción no lance excepción
        doNothing().when(clienteDAO).insert(any(ClientePremium.class));

        // Ejecutar y verificar
        boolean resultado = controlador.addClientePremium(
                clientePremium1.getNombre(), clientePremium1.getDomicilio(),
                clientePremium1.getNif(), clientePremium1.getEmail()
        );

        assertTrue(resultado, "Debe devolver true si la inserción es exitosa.");
        verify(clienteDAO, times(1)).insert(any(ClientePremium.class));
    }

    // ====================================================================
    //              TESTS para getClientes
    // ====================================================================

    @Test
    @DisplayName("getClientes - Devuelve todos los clientes")
    void testGetClientes_Exito() throws Exception {
        List<Cliente> listaEsperada = Arrays.asList(clienteEstandar1, clientePremium1);

        // Configurar el Mock para devolver la lista completa
        when(clienteDAO.findAll()).thenReturn(listaEsperada);

        // Ejecutar
        List<Cliente> resultado = controlador.getClientes();

        // Verificar
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(clienteDAO, times(1)).findAll();
    }

    @Test
    @DisplayName("getClientes - Lanza RuntimeException si hay error SQL")
    void testGetClientes_ErrorSQL() throws Exception {
        // Configurar el Mock para lanzar SQLException
        when(clienteDAO.findAll()).thenThrow(new SQLException("Error de conexión"));

        // Verificar que se lanza la excepción esperada (RuntimeException, ya que el controlador la envuelve)
        assertThrows(RuntimeException.class, () -> controlador.getClientes());
    }

    @Test
    @DisplayName("getClientes - Devuelve null si hay TipoClienteInvalidoException")
    void testGetClientes_TipoInvalido() throws Exception {
        // Configurar el Mock para lanzar TipoClienteInvalidoException
        when(clienteDAO.findAll()).thenThrow(new TipoClienteInvalidoException("Tipo desconocido"));

        // Ejecutar y verificar
        List<Cliente> resultado = controlador.getClientes();
        assertNull(resultado, "Debe devolver null si hay error de tipo de cliente.");
    }


    // ====================================================================
    //              TESTS para getClientesEstandar / getClientesPremium
    // ====================================================================

    @Test
    @DisplayName("getClientesEstandar - Devuelve solo clientes estándar")
    void testGetClientesEstandar_Exito() throws Exception {
        List<ClienteEstandar> listaEsperada = Collections.singletonList(clienteEstandar1);

        // Configurar el Mock
        when(clienteDAO.findStandardClients()).thenReturn(listaEsperada);

        // Ejecutar
        List<Cliente> resultado = controlador.getClientesEstandar();

        // Verificar
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0) instanceof ClienteEstandar);
        verify(clienteDAO, times(1)).findStandardClients();
    }

    @Test
    @DisplayName("getClientesEstandar - Devuelve null si hay error SQL")
    void testGetClientesEstandar_ErrorSQL() throws Exception {
        // Configurar el Mock
        when(clienteDAO.findStandardClients()).thenThrow(new SQLException("Error de consulta"));

        // Ejecutar y verificar
        List<Cliente> resultado = controlador.getClientesEstandar();
        assertNull(resultado, "Debe devolver null si hay error SQL.");
    }

    @Test
    @DisplayName("getClientesPremium - Devuelve solo clientes premium")
    void testGetClientesPremium_Exito() throws Exception {
        List<ClientePremium> listaEsperada = Collections.singletonList(clientePremium1);

        // Configurar el Mock
        when(clienteDAO.findPremiumClients()).thenReturn(listaEsperada);

        // Ejecutar
        List<Cliente> resultado = controlador.getClientesPremium();

        // Verificar
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0) instanceof ClientePremium);
        verify(clienteDAO, times(1)).findPremiumClients();
    }

    // ====================================================================
    //              TESTS para existeCliente
    // ====================================================================

    @Test
    @DisplayName("existeCliente - Devuelve true si el NIF existe")
    void testExisteCliente_True() throws Exception {
        // Configurar el Mock para devolver true para un NIF específico
        when(clienteDAO.existsClient("111A")).thenReturn(true);

        // Ejecutar y verificar
        assertTrue(controlador.existeCliente("111A"));
        verify(clienteDAO, times(1)).existsClient("111A");
    }

    @Test
    @DisplayName("existeCliente - Devuelve false si el NIF no existe")
    void testExisteCliente_False() throws Exception {
        // Configurar el Mock para devolver false para un NIF no existente
        when(clienteDAO.existsClient("999X")).thenReturn(false);

        // Ejecutar y verificar
        assertFalse(controlador.existeCliente("999X"));
    }

    @Test
    @DisplayName("existeCliente - Devuelve false si ocurre una excepción")
    void testExisteCliente_Excepcion() throws Exception {
        // Configurar el Mock para lanzar una excepción
        when(clienteDAO.existsClient("111A")).thenThrow(new RuntimeException("Error inesperado"));

        // Ejecutar y verificar
        assertFalse(controlador.existeCliente("111A"), "Debe capturar cualquier excepción y devolver false.");
    }
}