package MEDT.MEDT.controlador;

import MEDT.MEDT.modelo.*;
import MEDT.MEDT.DAO.IArticuloDAO;

import java.sql.SQLException;
import java.util.List;

public class ControladorArticulos {

    /// Articulos DAO
    private IArticuloDAO articuloDAO;

    /// Constructor de controlador articulo
    public ControladorArticulos(IArticuloDAO articuloDAO) {
        this.articuloDAO = articuloDAO;
    }

    /// Inserta un nuevo articulo en la BBDD
    public boolean addArticulo(String codigo, String descripcion, double precio, double gastosEnvio, int tiempoPrep) {
        try {
            Articulo articulo = new Articulo(codigo, descripcion, precio, gastosEnvio, tiempoPrep);
            this.articuloDAO.insert(articulo);
        } catch (SQLException e) {
            System.out.println("Error al insertar articulo");
            return false;
        }

        return true;
    }

    /// Devuelve todos los articulos de la tabla
    public List<Articulo> getArticulos() {
        try {
            return this.articuloDAO.findAll().stream().toList();
        }
        catch (SQLException ex) {
            System.out.println("Error al consultar articulos");
        }

        return null;
    }
}
