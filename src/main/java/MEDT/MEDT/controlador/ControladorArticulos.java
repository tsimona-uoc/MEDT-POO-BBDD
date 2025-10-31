package MEDT.MEDT.controlador;

import MEDT.MEDT.modelo.*;
import MEDT.MEDT.persistencia.DAO.ArticuloDAO;
import MEDT.MEDT.persistencia.JDBC.ArticuloJDBC;

import java.util.List;

public class ControladorArticulos {

    ///Articulo DAO
    private ArticuloDAO articuloDAO;

    public ControladorArticulos() {
        this.articuloDAO = new ArticuloJDBC();
    }

    // =======================
    //  ARTÍCULOS
    // =======================
    public boolean addArticulo(String codigo, String descripcion, double precio, double gastosEnvio, int tiempoPrep) {
        try {
            Articulo articulo = new Articulo(codigo, descripcion, precio, gastosEnvio, tiempoPrep);
            return articuloDAO.addArticulo(articulo);
        } catch (Exception e) {
            return false;
        }
    }

    public Articulo buscarPorCodigo (String codigo) {
        return  articuloDAO.buscarPorCodigo(codigo);
    }

    public List<Articulo> listarTodos() {
        return articuloDAO.listarTodos();
    }
}
