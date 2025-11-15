package MEDT.MEDT.DAO;

import MEDT.MEDT.modelo.Articulo;

import java.sql.SQLException;
import java.util.Collection;

public interface IArticuloDAO {

    /// Insert a new item
    public void insert(Articulo articulo) throws SQLException;
    /// Update a given item
    public void update(Articulo articulo) throws SQLException;
    /// Delete a given item by code
    public void delete(String codigo) throws SQLException;
    /// Find an item by code
    public Articulo findByCodigo(String codigo) throws SQLException;
    /// Returns a list of all items
    public Collection<Articulo> findAll() throws SQLException;
}
