package MEDT.MEDT.modelo;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("premium")
public class ClientePremium extends Cliente {
    private static final double CUOTA = 30.0;
    private static final double DESCUENTO = 0.2;

    /// Required by JPA
    public ClientePremium() {}

    public ClientePremium(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
    }

    public static double getCuota() {
        return CUOTA;
    }

    public static double getDescuento() {
        return DESCUENTO;
    }

    @Override
    public double calcularDescuento(){
        return DESCUENTO;
    }
}
