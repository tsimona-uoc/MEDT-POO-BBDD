package MEDT;

import MEDT.MEDT.controlador.Controlador;
import MEDT.MEDT.vista.Vista;

public class Main {
    public static void main(String[] args) {

        // 1. Creamos el Controlador.
        //    (Él internamente llamará a la DAOFactory para conectarse a la BD)
        Controlador controlador = new Controlador();

        // 2. Creamos la Vista y le "inyectamos" el controlador
        Vista vista = new Vista(controlador);

        // 3. Iniciamos el menú de la aplicación
        vista.menu();
    }
}