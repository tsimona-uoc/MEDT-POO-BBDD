package controlador;

import MEDT.MEDT.controlador.Controlador;
import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.modelo.Cliente;
import MEDT.MEDT.modelo.Datos;
import MEDT.MEDT.modelo.Pedido;
import MEDT.MEDT.modelo.ClienteEstandar;
import MEDT.MEDT.modelo.excepciones.ArticuloNoEncontradoException;
import MEDT.MEDT.modelo.excepciones.PedidoNoCancelableException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Habilita el uso de anotaciones de Mockito con JUnit Jupiter
@ExtendWith(MockitoExtension.class)
class ControladorPedidosTest {

    // Simula la dependencia de la capa de datos
    @Mock
    private Datos datos;

    // Inyecta el mock 'datos' en la instancia real de Controlador que vamos a probar
    @InjectMocks
    private Controlador controlador;

    // Variables de prueba comunes
    private final int NUM_PEDIDO = 1;
    private final int CANTIDAD = 5;
    private final LocalDateTime FECHA_HORA = LocalDateTime.now();
    private final String CODIGO_ARTICULO = "A101";
    private final String NIF_CLIENTE = "12345678A";
    private Articulo articulo;
    private Cliente cliente;
    private Pedido pedido;


    @BeforeEach
    void setUp() {
        controlador = new Controlador(datos);
        // Objetos de prueba usados para simular las respuestas del mock 'datos'
        articulo = new Articulo(CODIGO_ARTICULO, "Descripción", 10.0, 5.0, 60);
        cliente = new ClienteEstandar("Nombre", "Domicilio", NIF_CLIENTE, "email@test.com");
        pedido = new Pedido(NUM_PEDIDO, CANTIDAD, FECHA_HORA, articulo, cliente);
    }

    // ============================================================
    //  Tests para addPedido
    // ============================================================

    @Test
    void addPedido_Exito() throws ArticuloNoEncontradoException {
        // Configuración del Mockito (Stubbing):
        // 1. Simula la búsqueda del artículo y cliente.
        when(datos.getArticulo(CODIGO_ARTICULO)).thenReturn(articulo);
        when(datos.getCliente(NIF_CLIENTE)).thenReturn(cliente);
        // 2. Simula que el guardado del pedido es exitoso.
        when(datos.addPedido(any(Pedido.class))).thenReturn(true);

        // Ejecución
        String resultado = controlador.addPedido(NUM_PEDIDO, CANTIDAD, FECHA_HORA, CODIGO_ARTICULO, NIF_CLIENTE);

        // Verificación del resultado
        assertEquals("Pedido añadido correctamente.", resultado, "El mensaje de éxito debería ser el esperado.");

        // Verificación de Interacciones (Comportamiento)
        verify(datos, times(1)).addPedido(any(Pedido.class)); // Asegura que se llamó al guardado
    }

    @Test
    void addPedido_ArticuloNoEncontrado_ManejaExcepcion() throws ArticuloNoEncontradoException {
        // Configuración: Simula que la búsqueda de artículo lanza la excepción
        when(datos.getArticulo(CODIGO_ARTICULO))
                .thenThrow(new ArticuloNoEncontradoException("El artículo con código A101 no existe."));

        // Ejecución
        String resultado = controlador.addPedido(NUM_PEDIDO, CANTIDAD, FECHA_HORA, CODIGO_ARTICULO, NIF_CLIENTE);

        // Verificación del resultado
        assertTrue(resultado.contains("Error: Artículo no encontrado."), "Debe capturar y reportar la excepción de artículo.");

        // Verificación de Interacciones
        verify(datos, never()).getCliente(anyString()); // No debe continuar buscando cliente ni guardando
        verify(datos, never()).addPedido(any(Pedido.class));
    }

    @Test
    void addPedido_ClienteNoEncontrado_DevuelveError() throws ArticuloNoEncontradoException {
        // Configuración
        when(datos.getArticulo(CODIGO_ARTICULO)).thenReturn(articulo);
        // Simula que el cliente no existe (devuelve null)
        when(datos.getCliente(NIF_CLIENTE)).thenReturn(null);

        // Ejecución
        String resultado = controlador.addPedido(NUM_PEDIDO, CANTIDAD, FECHA_HORA, CODIGO_ARTICULO, NIF_CLIENTE);

        // Verificación del resultado
        assertEquals("Error: el cliente no existe. Debe crearlo antes de continuar.", resultado, "Debe devolver el error de cliente no encontrado.");

        // Verificación de Interacciones
        verify(datos, times(1)).getCliente(NIF_CLIENTE);
        verify(datos, never()).addPedido(any(Pedido.class)); // No debe intentar guardar
    }

    @Test
    void addPedido_PedidoDuplicado_DevuelveError() throws ArticuloNoEncontradoException {
        // Configuración
        when(datos.getArticulo(CODIGO_ARTICULO)).thenReturn(articulo);
        when(datos.getCliente(NIF_CLIENTE)).thenReturn(cliente);
        // Simula que el guardado del pedido falla (posible duplicado)
        when(datos.addPedido(any(Pedido.class))).thenReturn(false);

        // Ejecución
        String resultado = controlador.addPedido(NUM_PEDIDO, CANTIDAD, FECHA_HORA, CODIGO_ARTICULO, NIF_CLIENTE);

        // Verificación del resultado
        assertEquals("Error: el pedido no se pudo añadir (posible duplicado).", resultado, "Debe devolver el error de pedido duplicado.");

        // Verificación de Interacciones
        verify(datos, times(1)).addPedido(any(Pedido.class));
    }

    // ============================================================
    //  Tests para eliminarPedido
    // ============================================================

    @Test
    void eliminarPedido_Exito() throws PedidoNoCancelableException {
        // 1. Configuración (Stubbing)
        // **IMPORTANTE**: Usamos anyInt() para asegurar que el mock se activa
        // con cualquier valor entero, haciéndolo más robusto.
        when(datos.getPedido(anyInt())).thenReturn(pedido);

        // 2. Ejecución
        boolean resultado = controlador.eliminarPedido(NUM_PEDIDO);

        // 3. Verificación (Assert)
        assertFalse(resultado, "El método debe devolver false según la implementación proporcionada.");

        // 4. Verificación de interacciones
        // Verificamos que se llamó con el número de pedido CORRECTO
        verify(datos, times(1)).getPedido(NUM_PEDIDO);
        verify(datos, times(1)).cancelarPedido(pedido); // Verifica que se llamó a la cancelación
    }

    @Test
    void eliminarPedido_NoExistePedido_LanzaIllegalArgumentException() throws PedidoNoCancelableException {
        // Configuración: Simula que el pedido no existe (devuelve null)
        when(datos.getPedido(NUM_PEDIDO)).thenReturn(null);

        // Ejecución y Verificación de Excepción
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            controlador.eliminarPedido(NUM_PEDIDO);
        });

        // Verificación del mensaje de la excepción
        assertEquals("No existe ningún pedido con ese número.", excepcion.getMessage(), "El mensaje de la excepción debe ser el esperado.");

        // Verificación de Interacciones
        verify(datos, times(1)).getPedido(NUM_PEDIDO);
        verify(datos, never()).cancelarPedido(any(Pedido.class)); // No debe intentar cancelar
    }

    @Test
    void eliminarPedido_NoCancelable_LanzaPedidoNoCancelableException() throws PedidoNoCancelableException {
        // Configuración
        when(datos.getPedido(NUM_PEDIDO)).thenReturn(pedido);
        // Simula que el intento de cancelación lanza la excepción
        doThrow(new PedidoNoCancelableException("El pedido no puede cancelarse, ya pasó el tiempo de preparación."))
                .when(datos).cancelarPedido(pedido);

        // Ejecución y Verificación de Excepción
        PedidoNoCancelableException excepcion = assertThrows(PedidoNoCancelableException.class, () -> {
            controlador.eliminarPedido(NUM_PEDIDO);
        });

        // Verificación del mensaje de la excepción
        assertEquals("El pedido no puede cancelarse, ya pasó el tiempo de preparación.", excepcion.getMessage(), "El mensaje de la excepción debe ser el esperado.");

        // Verificación de Interacciones
        verify(datos, times(1)).getPedido(NUM_PEDIDO);
        verify(datos, times(1)).cancelarPedido(pedido); // Asegura que SÍ se llamó al intento de cancelación
    }
}