package MEDT.MEDT.modelo;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("estandar") // Este valor se guardará en la columna 'tipo'
public class ClienteEstandar extends Cliente {

    public ClienteEstandar() {}

    public ClienteEstandar(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
    }

    @Override
    public double calcularDescuento(){
        return 0.0;
    }
}