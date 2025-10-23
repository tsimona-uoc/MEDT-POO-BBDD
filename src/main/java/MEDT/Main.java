package MEDT;

import MEDT.MEDT.controlador.Controlador;
import MEDT.MEDT.modelo.Datos;
import MEDT.MEDT.vista.Vista;

public class Main {
    public static void main(String[] args) {
        Controlador controlador = new Controlador(new Datos());
        new Vista(controlador).menu();
    }
}