package MEDT.MEDT.controlador;

import MEDT.MEDT.modelo.*;
import MEDT.MEDT.DAO.IArticuloDAO;

import java.sql.SQLException;
import java.util.List;

public class ControladorArticulos implements IControladorArticulos {

    /// Articulos DAO
    private IArticuloDAO articuloDAO;

    /// Constructor de controlador articulo
    public ControladorArticulos(IArticuloDAO articuloDAO) {
        this.articuloDAO = articuloDAO;
    }

    /// Inserta un nuevo articulo en la BBDD
    @Override
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

    /// Inserta un nuevo articulo en la BBDD
    @Override
    public boolean eliminarArticulo(String codigo) {
        try {
            this.articuloDAO.delete(codigo);
        } catch (SQLException e) {
            System.out.println("Error al eliminar el articulo: " + e.getMessage());
            return false;
        }

        return true;
    }

    /// Devuelve todos los articulos de la tabla
    @Override
    public List<Articulo> getArticulos() {
        try {
            return this.articuloDAO.findAll().stream().toList();
        }
        catch (SQLException ex) {
            System.out.println("Error al consultar articulos");
        }

        return null;
    }

    /// Get articulo by code
    @Override
    public Articulo getArticulo(String codigo){
        try {
            return this.articuloDAO.findByCodigo(codigo);
        }
        catch (SQLException ex) {
            System.out.println("Error al consultar articulos");
        }

        return null;
    }

    /// Update articulo
    @Override
    public boolean updateArticulo(Articulo articulo){
        try {
            this.articuloDAO.update(articulo);
        } catch (SQLException ex) {
            System.out.println("Error al actualizar articulo: " + ex.getMessage());
            return false;
        }
        return true;
    }
}
