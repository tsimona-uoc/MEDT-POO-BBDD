package MEDT;

import MEDT.MEDT.controlador.ControladorArticulos;
import MEDT.MEDT.controlador.ControladorClientes;
import MEDT.MEDT.controlador.ControladorPedidos;
import MEDT.MEDT.modelo.Datos;
import MEDT.MEDT.util.DBConnection;
import MEDT.MEDT.vista.Vista;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Datos datos = new Datos();
//        ControladorArticulos ca = new ControladorArticulos(new Datos());
        ControladorClientes cc = new ControladorClientes(datos);
        ControladorPedidos cp = new ControladorPedidos(datos);
    }
}