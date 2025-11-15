package MEDT.MEDT.controlador;

import MEDT.MEDT.DAO.IArticuloDAO;
import MEDT.MEDT.DAO.IClienteDAO;
import MEDT.MEDT.DAO.IPedidoDAO;
import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.Pedido;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// 1. Configuración de Mockito
@ExtendWith(MockitoExtension.class)
class ControladorPedidosTest {

    // 2. Mocks de las dependencias (Interfaces DAO)
    @Mock
    private IArticuloDAO articuloDAO;

    @Mock
    private IPedidoDAO pedidoDAO;

    @Mock
    private IClienteDAO clienteDAO;

    // 3. Inyección del Mock en el controlador que vamos a probar
    @InjectMocks
    private ControladorPedidos controlador;

    // --- Variables de Prueba Comunes ---
    private Articulo articuloValido;
    private Cliente clienteValido;
    private Pedido pedidoCancelable;
    private Pedido pedidoNoCancelable;
    private LocalDateTime ahora;
    private LocalDateTime haceMenosDeUnaHora;
    private LocalDateTime haceMasDeUnaHora;

    @BeforeEach
    void setUp() {
        // Inicializar datos simulados
        articuloValido = new Articulo("A100", "Portátil", 1000.0, 10.0, 60); // TiempoPrep = 60 min (1 hora)
        clienteValido = new ClienteEstandar("Juan Pérez", "NIF123", "Dir1", "juan@test.com");

        ahora = LocalDateTime.now();

        // El pedido será CANCELABLE si el tiempo transcurrido es <= TiempoPrep (60 min)
        haceMenosDeUnaHora = ahora.minusMinutes(30);
        pedidoCancelable = new Pedido(1, 2, haceMenosDeUnaHora, articuloValido, clienteValido);

        // El pedido NO será CANCELABLE si el tiempo transcurrido es > TiempoPrep (60 min)
        haceMasDeUnaHora = ahora.minusMinutes(61);
        pedidoNoCancelable = new Pedido(2, 1, haceMasDeUnaHora, articuloValido, clienteValido);
    }

    // ====================================================================
    //              TESTS para el método addPedido
    // ====================================================================

    @Test
    @DisplayName("addPedido - Pedido añadido exitosamente")
    void testAddPedido_Exito() throws Exception {
        // Configurar los Mocks para que devuelvan datos válidos
        when(articuloDAO.findByCodigo(articuloValido.getCodigo())).thenReturn(articuloValido);
        when(clienteDAO.findByNIF(clienteValido.getNif())).thenReturn(clienteValido);

        // Configurar el DAO de Pedido para que no lance excepción al insertar
        doNothing().when(pedidoDAO).insert(any(Pedido.class));

        // Ejecutar el método
        String resultado = controlador.addPedido(3, 5, ahora, articuloValido.getCodigo(), clienteValido.getNif());

        // Verificar el resultado y el comportamiento del Mock
        assertEquals("Pedido añadido correctamente.", resultado);
        // Verificar que el método insert fue llamado exactamente una vez
        verify(pedidoDAO, times(1)).insert(any(Pedido.class));
    }

    @Test
    @DisplayName("addPedido - Error si el artículo no existe")
    void testAddPedido_ArticuloNoExiste() throws Exception {
        // Configurar el Mock para que devuelva null para el artículo
        when(articuloDAO.findByCodigo(anyString())).thenReturn(null);
        when(clienteDAO.findByNIF(clienteValido.getNif())).thenReturn(clienteValido);

        // Ejecutar el método
        String resultado = controlador.addPedido(3, 5, ahora, "A999", clienteValido.getNif());

        // Verificar el resultado
        assertEquals("Error: el artículo no existe.", resultado);
        // Verificar que NUNCA se intentó insertar el pedido
        verify(pedidoDAO, never()).insert(any(Pedido.class));
    }

    @Test
    @DisplayName("addPedido - Error si el cliente no existe")
    void testAddPedido_ClienteNoExiste() throws Exception {
        // Configurar el Mock para que devuelva el artículo pero null para el cliente
        when(articuloDAO.findByCodigo(articuloValido.getCodigo())).thenReturn(articuloValido);
        when(clienteDAO.findByNIF(anyString())).thenReturn(null);

        // Ejecutar el método
        String resultado = controlador.addPedido(3, 5, ahora, articuloValido.getCodigo(), "NIF999");

        // Verificar el resultado
        assertEquals("Error: el cliente no existe. Debe crearlo antes de continuar.", resultado);
        // Verificar que NUNCA se intentó insertar el pedido
        verify(pedidoDAO, never()).insert(any(Pedido.class));
    }

    @Test
    @DisplayName("addPedido - Error de duplicado en la base de datos (SQLException)")
    void testAddPedido_Duplicado() throws Exception {
        // Configurar los Mocks para datos válidos
        when(articuloDAO.findByCodigo(articuloValido.getCodigo())).thenReturn(articuloValido);
        when(clienteDAO.findByNIF(clienteValido.getNif())).thenReturn(clienteValido);

        // Configurar el DAO de Pedido para que lance una SQLException al intentar insertar (simulando, por ejemplo, una violación de clave única)
        doThrow(new SQLException("Duplicado de pedido")).when(pedidoDAO).insert(any(Pedido.class));

        // Ejecutar el método
        String resultado = controlador.addPedido(3, 5, ahora, articuloValido.getCodigo(), clienteValido.getNif());

        // Verificar el resultado
        assertEquals("Error: el pedido no se pudo añadir (posible duplicado).", resultado);
    }

    // ====================================================================
    //              TESTS para el método eliminarPedido
    // ====================================================================

    @Test
    @DisplayName("eliminarPedido - Eliminación exitosa de pedido cancelable")
    void testEliminarPedido_Exito() throws Exception {
        // Configurar el Mock para que devuelva un pedido cancelable
        when(pedidoDAO.findByCode(1)).thenReturn(pedidoCancelable);

        // Configurar el DAO de Pedido para que la eliminación no lance excepción
        doNothing().when(pedidoDAO).delete(1);

        // Ejecutar y verificar
        assertTrue(controlador.eliminarPedido(1));

        // Verificar que el método delete fue llamado
        verify(pedidoDAO, times(1)).delete(1);
    }

    @Test
    @DisplayName("eliminarPedido - Lanza excepción si el pedido no existe")
    void testEliminarPedido_NoExiste() throws Exception {
        // Configurar el Mock para que devuelva null (no encontrado)
        when(pedidoDAO.findByCode(999)).thenReturn(null);

        // Ejecutar y verificar que lanza la excepción esperada
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            controlador.eliminarPedido(999);
        });

        assertEquals("No existe ningún pedido con ese número.", exception.getMessage());
        // Verificar que NUNCA se intentó eliminar
        verify(pedidoDAO, never()).delete(anyInt());
    }

    @Test
    @DisplayName("eliminarPedido - Lanza excepción si el pedido no es cancelable")
    void testEliminarPedido_NoCancelable() throws Exception {
        // Configurar el Mock para que devuelva un pedido NO cancelable
        when(pedidoDAO.findByCode(2)).thenReturn(pedidoNoCancelable);

        // Ejecutar y verificar que lanza la excepción esperada
        Exception exception = assertThrows(PedidoNoCancelableException.class, () -> {
            controlador.eliminarPedido(2);
        });

        assertEquals("No se puede eliminar el pedido.", exception.getMessage());
        // Verificar que NUNCA se intentó eliminar
        verify(pedidoDAO, never()).delete(anyInt());
    }

    @Test
    @DisplayName("eliminarPedido - Devuelve false si ocurre una SQLException al eliminar")
    void testEliminarPedido_ErrorSQL() throws Exception {
        // Configurar el Mock para que devuelva un pedido cancelable
        when(pedidoDAO.findByCode(1)).thenReturn(pedidoCancelable);

        // Configurar el DAO de Pedido para que lance una SQLException al eliminar
        doThrow(new SQLException("Error de DB al borrar")).when(pedidoDAO).delete(1);

        // Ejecutar y verificar
        assertFalse(controlador.eliminarPedido(1));
    }


    // ====================================================================
    //              TESTS para el método getPedidosPendientes
    // ====================================================================

    @Test
    @DisplayName("getPedidosPendientes - Devuelve todos los pendientes (NIF vacío)")
    void testGetPedidosPendientes_Todos() throws Exception {
        // Un pedido pendiente (tiempo transcurrido < tiempo de preparación)
        Pedido pendiente = pedidoCancelable;
        // Un pedido enviado (tiempo transcurrido > tiempo de preparación)
        Pedido enviado = pedidoNoCancelable;

        // Configurar el Mock para que findAll devuelva la lista completa
        when(pedidoDAO.findAll()).thenReturn(Arrays.asList(pendiente, enviado));

        // Ejecutar
        List<Pedido> resultado = controlador.getPedidosPendientes("");

        // Verificar: solo debe estar el pedido pendiente
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(pendiente.getNumPedido(), resultado.get(0).getNumPedido());
        verify(pedidoDAO, times(1)).findAll();
        verify(pedidoDAO, never()).findByCliente(any(Cliente.class)); // Asegurar que no se usa la búsqueda por cliente
    }

    @Test
    @DisplayName("getPedidosPendientes - Devuelve pedidos de un cliente específico")
    void testGetPedidosPendientes_ClienteEspecifico() throws Exception {
        // Configurar Mocks
        when(clienteDAO.findByNIF(clienteValido.getNif())).thenReturn(clienteValido);

        // Simular que el cliente tiene pedidos pendientes y enviados
        Pedido pendiente = pedidoCancelable;
        Pedido enviado = pedidoNoCancelable;
        when(pedidoDAO.findByCliente(clienteValido)).thenReturn(Arrays.asList(pendiente, enviado));

        // Ejecutar
        List<Pedido> resultado = controlador.getPedidosPendientes(clienteValido.getNif());

        // Verificar: solo debe estar el pedido pendiente
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(pendiente.getNumPedido(), resultado.get(0).getNumPedido());
    }

    // ====================================================================
    //              TESTS para el método getPedidosEnviados
    // ====================================================================

    @Test
    @DisplayName("getPedidosEnviados - Devuelve todos los enviados (NIF vacío)")
    void testGetPedidosEnviados_Todos() throws Exception {
        // Un pedido pendiente (tiempo transcurrido < tiempo de preparación)
        Pedido pendiente = pedidoCancelable;
        // Un pedido enviado (tiempo transcurrido > tiempo de preparación)
        Pedido enviado = pedidoNoCancelable;

        // Configurar el Mock para que findAll devuelva la lista completa
        when(pedidoDAO.findAll()).thenReturn(Arrays.asList(pendiente, enviado));

        // Ejecutar
        List<Pedido> resultado = controlador.getPedidosEnviados("");

        // Verificar: solo debe estar el pedido enviado
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(enviado.getNumPedido(), resultado.get(0).getNumPedido());
        verify(pedidoDAO, times(1)).findAll();
        verify(pedidoDAO, never()).findByCliente(any(Cliente.class));
    }

    @Test
    @DisplayName("getPedidosEnviados - Devuelve pedidos de un cliente específico")
    void testGetPedidosEnviados_ClienteEspecifico() throws Exception {
        // Configurar Mocks
        when(clienteDAO.findByNIF(clienteValido.getNif())).thenReturn(clienteValido);

        // Simular que el cliente tiene pedidos pendientes y enviados
        Pedido pendiente = pedidoCancelable;
        Pedido enviado = pedidoNoCancelable;
        when(pedidoDAO.findByCliente(clienteValido)).thenReturn(Arrays.asList(pendiente, enviado));

        // Ejecutar
        List<Pedido> resultado = controlador.getPedidosEnviados(clienteValido.getNif());

        // Verificar: solo debe estar el pedido enviado
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(enviado.getNumPedido(), resultado.get(0).getNumPedido());
    }

    @Test
    @DisplayName("getPedidosEnviados - Lanza RuntimeException si el cliente no existe")
    void testGetPedidosEnviados_ClienteNoExiste() throws Exception {
        // Configurar Mocks para que devuelva null para el NIF
        when(clienteDAO.findByNIF("NIF999")).thenReturn(null);

        // Ejecutar y verificar que lanza la excepción esperada
        assertThrows(RuntimeException.class, () -> {
            controlador.getPedidosEnviados("NIF999");
        }, "Cliente no encontrado");
    }

    // Nota: Otros casos como errores de SQLException al buscar en los métodos getPedidos... devuelven 'null' según la implementación actual del controlador,
    // por lo que bastaría con verificar que el resultado es null en esos casos.
}