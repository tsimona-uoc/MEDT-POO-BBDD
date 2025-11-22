package MEDT.MEDT.modelo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


@Entity
@DiscriminatorValue("estandar")
public class ClienteEstandar extends Cliente {

    //Constructor
    public ClienteEstandar(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
    }

    public ClienteEstandar() {

    }

    @Override
    public double calcularDescuento(){
        return 0.0;
    }
}
