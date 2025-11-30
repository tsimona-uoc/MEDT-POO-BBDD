package MEDT.MEDT.modelo;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("estandar")
public class ClienteEstandar extends Cliente {

    /// Required by JPA
    public ClienteEstandar() {}

    //Constructor
    public ClienteEstandar(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
    }

    @Override
    public double calcularDescuento(){
        return 0.0;
    }
}
