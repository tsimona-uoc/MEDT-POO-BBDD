package MEDT.MEDT.modelo;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("premium") // Este valor se guardará en la columna 'tipo'
public class ClientePremium extends Cliente {

    // @Transient significa que estos campos NO se guardan en BBDD (son constantes)
    @Transient
    private static final double CUOTA = 30.0;
    @Transient
    private static final double DESCUENTO = 0.2;

    public ClientePremium() {}

    public ClientePremium(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
    }

    public static double getCuota() { return CUOTA; }
    public static double getDescuento() { return DESCUENTO; }

    @Override
    public double calcularDescuento(){
        return DESCUENTO;
    }
}