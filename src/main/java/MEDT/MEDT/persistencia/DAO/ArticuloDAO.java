package MEDT.MEDT.persistencia.DAO;

import MEDT.MEDT.modelo.Articulo;
import java.util.List;

public interface ArticuloDAO {

    boolean addArticulo(Articulo articulo);
    Articulo getArticulo(String codigo);
    List<Articulo> getArticulos();
}
