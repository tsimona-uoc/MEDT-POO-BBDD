package MEDT.MEDT.modelo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ESTANDAR")
public class ClienteEstandar extends Cliente {

    public ClienteEstandar(){
        super();
    }
    //Constructor
    public ClienteEstandar(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
    }


    @Override
    public double calcularDescuento(){
        return 0.0;
    }
}
