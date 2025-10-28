package MEDT.MEDT.controlador;

import MEDT.MEDT.modelo.*;

import java.util.List;

public class ControladorArticulos {

    /// Datos
    private Datos datos;

    public ControladorArticulos(Datos datos) {
        this.datos = datos;
    }

    // =======================
    //  ARTÍCULOS
    // =======================
    public boolean addArticulo(String codigo, String descripcion, double precio, double gastosEnvio, int tiempoPrep) {
        try {
            Articulo articulo = new Articulo(codigo, descripcion, precio, gastosEnvio, tiempoPrep);
            return datos.addArticulo(articulo);
        } catch (Exception e) {
            return false;
        }
    }

    public List<Articulo> getArticulos() {
        return datos.getArticulos().stream().toList();
    }
}
