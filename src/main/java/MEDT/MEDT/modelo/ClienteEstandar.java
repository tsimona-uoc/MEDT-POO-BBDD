package MEDT.MEDT.modelo;

public class ClienteEstandar extends Cliente {

    //Constructor
    public ClienteEstandar(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email, "Estandar");
    }


    @Override
    public double calcularDescuento(){
        return 0.0;
    }
}
