package MEDT;

import MEDT.MEDT.controlador.ControladorArticulos;
import MEDT.MEDT.controlador.ControladorClientes;
import MEDT.MEDT.controlador.ControladorPedidos;
import MEDT.MEDT.modelo.Datos;
import MEDT.MEDT.vista.Vista;

public class Main {
    public static void main(String[] args) {
        Datos datos = new Datos();
        ControladorArticulos ca = new ControladorArticulos(datos);
        ControladorPedidos cp = new ControladorPedidos(datos);
        ControladorClientes cc = new ControladorClientes(datos);
        new Vista(cp, ca, cc).menu();
    }
}