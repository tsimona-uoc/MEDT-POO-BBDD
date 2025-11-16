package MEDT.MEDT.controlador;

import MEDT.MEDT.modelo.Articulo;

import java.util.List;

public interface IControladorArticulos {
    /// Add articulo
    boolean addArticulo(String codigo, String descripcion, double precio, double gastosEnvio, int tiempoPrep);

    /// Delete articulo
    boolean eliminarArticulo(String codigo);

    /// Get articulos
    List<Articulo> getArticulos();

    /// Get articulo by code
    Articulo getArticulo(String codigo);

    /// Update articulo
    boolean updateArticulo(Articulo articulo);
}