package MEDT.MEDT.controlador;

import MEDT.MEDT.DAO.IArticuloDAO;
import MEDT.MEDT.modelo.Articulo;
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
class ControladorArticulosTest {

    // 1. Mock de la dependencia (la interfaz DAO)
    @Mock
    private IArticuloDAO articuloDAO;

    // 2. Inyección del Mock en el controlador a probar
    @InjectMocks
    private ControladorArticulos controlador;

    // --- Variables de Prueba ---
    private Articulo articulo1;
    private Articulo articulo2;

    @BeforeEach
    void setUp() {
        // Inicializar artículos de prueba
        articulo1 = new Articulo("A101", "Laptop Pro", 1200.0, 15.0, 45);
        articulo2 = new Articulo("A102", "Monitor 4K", 450.0, 10.0, 30);
    }

    // ====================================================================
    //              TESTS para el método addArticulo
    // ====================================================================

    @Test
    @DisplayName("addArticulo - Inserta un artículo exitosamente")
    void testAddArticulo_Exito() throws SQLException {
        // Configurar el Mock para que la inserción no lance excepción
        // Usamos doNothing() ya que el método insert es void o no devuelve algo que necesitemos
        doNothing().when(articuloDAO).insert(any(Articulo.class));

        // Ejecutar el método
        boolean resultado = controlador.addArticulo(
                articulo1.getCodigo(), articulo1.getDescripcion(), articulo1.getPrecio(),
                articulo1.getGastosEnvio(), articulo1.getTiempoPrep()
        );

        // Verificar el resultado
        assertTrue(resultado, "Debe devolver true si la inserción es exitosa.");

        // Verificar el comportamiento del Mock (que el método insert fue llamado exactamente una vez)
        verify(articuloDAO, times(1)).insert(any(Articulo.class));
    }

    @Test
    @DisplayName("addArticulo - Devuelve false si ocurre una SQLException")
    void testAddArticulo_ErrorSQL() throws SQLException {
        // Configurar el Mock para que lance una SQLException al insertar (simulando un error de DB, por ejemplo, clave duplicada)
        doThrow(new SQLException("Clave duplicada en la base de datos")).when(articuloDAO).insert(any(Articulo.class));

        // Ejecutar el método
        boolean resultado = controlador.addArticulo(
                articulo1.getCodigo(), articulo1.getDescripcion(), articulo1.getPrecio(),
                articulo1.getGastosEnvio(), articulo1.getTiempoPrep()
        );

        // Verificar el resultado
        assertFalse(resultado, "Debe devolver false si ocurre una excepción SQL.");

        // Verificar el comportamiento del Mock
        verify(articuloDAO, times(1)).insert(any(Articulo.class));
    }

    // ====================================================================
    //              TESTS para el método getArticulos
    // ====================================================================

    @Test
    @DisplayName("getArticulos - Devuelve una lista de todos los artículos")
    void testGetArticulos_Exito() throws SQLException {
        List<Articulo> listaEsperada = Arrays.asList(articulo1, articulo2);

        // Configurar el Mock para que findAll devuelva una lista de artículos
        when(articuloDAO.findAll()).thenReturn(listaEsperada);

        // Ejecutar el método
        List<Articulo> resultado = controlador.getArticulos();

        // Verificar el resultado
        assertNotNull(resultado, "La lista no debe ser nula.");
        assertEquals(2, resultado.size(), "Debe devolver dos artículos.");
        assertEquals(articulo1.getCodigo(), resultado.get(0).getCodigo());

        // Verificar que el método findAll fue llamado
        verify(articuloDAO, times(1)).findAll();
    }

    @Test
    @DisplayName("getArticulos - Devuelve una lista vacía si no hay artículos")
    void testGetArticulos_ListaVacia() throws SQLException {
        // Configurar el Mock para que findAll devuelva una lista vacía
        when(articuloDAO.findAll()).thenReturn(Collections.emptyList());

        // Ejecutar el método
        List<Articulo> resultado = controlador.getArticulos();

        // Verificar el resultado
        assertNotNull(resultado, "Debe devolver una lista no nula (vacía).");
        assertTrue(resultado.isEmpty(), "La lista debe estar vacía.");

        // Verificar que el método findAll fue llamado
        verify(articuloDAO, times(1)).findAll();
    }

    @Test
    @DisplayName("getArticulos - Devuelve null si ocurre una SQLException")
    void testGetArticulos_ErrorSQL() throws SQLException {
        // Configurar el Mock para que findAll lance una SQLException (simulando un error de conexión, etc.)
        when(articuloDAO.findAll()).thenThrow(new SQLException("Error de conexión a la base de datos"));

        // Ejecutar el método
        List<Articulo> resultado = controlador.getArticulos();

        // Verificar el resultado
        assertNull(resultado, "Debe devolver null si ocurre una excepción SQL.");

        // Verificar que el método findAll fue llamado
        verify(articuloDAO, times(1)).findAll();
    }
}