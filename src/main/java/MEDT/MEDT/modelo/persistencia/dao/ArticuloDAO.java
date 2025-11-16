package MEDT.MEDT.modelo.persistencia.dao;

import MEDT.MEDT.modelo.Articulo;
import MEDT.MEDT.modelo.excepciones.ArticuloNoEncontradoException;
import java.util.List;

public interface ArticuloDAO {
    void insertar(Articulo articulo);
    Articulo obtenerPorCodigo(String codigo) throws ArticuloNoEncontradoException;
    List<Articulo> obtenerTodos();
}