package MEDT.MEDT.controlador;

import MEDT.MEDT.DAO.IArticuloDAO;
import MEDT.MEDT.modelo.Articulo;

import java.util.List;

public class ControladorArticulos {
    private IArticuloDAO articuloDAO;

    public ControladorArticulos(IArticuloDAO articuloDAO) {
        this.articuloDAO = articuloDAO;
    }

    public boolean addArticulo(String codigo, String descripcion, double precio, double gastosEnvio, int tiempoPrep) {
        try {
            Articulo articulo = new Articulo(codigo, descripcion, precio, gastosEnvio, tiempoPrep);
            articuloDAO.insert(articulo);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

//    public List<Articulo> getArticulos() {
//        return datos.getArticulos().stream().toList();
//    }

