package MEDT.MEDT.Factory;

import java.util.HashMap;
import java.util.Map;

public class MEDTFactory {

    // 1. Mapa estático para el Registro
    // Almacena la relación: Interfaz.class -> Instancia de Implementación
    private static final Map<Class<?>, Object> registry = new HashMap<Class<?>, Object>();

    /**
     * Registra una instancia concreta para una interfaz dada.
     * API: Factory.RegisterType<T>(T implementation)
     * @param interfaceType La interfaz a registrar (ej: ArticuloDAO.class).
     * @param implementation La instancia concreta de la implementación (ej: new ArticuloDAOJdbc()).
     */
    public static <T> void registerType(Class<T> interfaceType, T implementation) {
        // Almacena la instancia concreta bajo la clave de su interfaz.
        registry.put(interfaceType, implementation);
    }

    /**
     * Resuelve y devuelve la implementación registrada para una interfaz.
     * API: Factory.Resolve<T>()
     * @param interfaceType La interfaz a resolver (ej: ArticuloDAO.class).
     * @return La instancia concreta registrada, casteada al tipo de la interfaz.
     */
    @SuppressWarnings("unchecked")
    public static <T> T resolve(Class<T> interfaceType) {
        Object implementation = registry.get(interfaceType);

        if (implementation == null) {
            throw new RuntimeException("Error: No hay implementación registrada para " + interfaceType.getName());
        }

        // Se hace el casting seguro al tipo de la interfaz solicitada.
        return (T) implementation;
    }
}
