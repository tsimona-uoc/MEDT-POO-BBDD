package MEDT.MEDT.DAO;

import MEDT.MEDT.modelo.Articulo;

import java.sql.SQLException;

public interface IArticuloDAO {
    public void insert(Articulo articulo) throws SQLException;
//    public void delete(Articulo articulo) throws SQLException;
//    public void update(Articulo articulo) throws SQLException;
}
