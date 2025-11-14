package MEDT.MEDT.DAO;

import MEDT.MEDT.modelo.Articulo;

import java.sql.SQLException;
import java.util.List;

public interface IArticuloDAO {
    void insert(Articulo articulo) throws SQLException;
    List<Articulo> findAll() throws SQLException;
}
