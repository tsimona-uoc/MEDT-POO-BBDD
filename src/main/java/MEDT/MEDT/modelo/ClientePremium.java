package MEDT.MEDT.modelo;

public class ClientePremium extends Cliente {
    private double cuota = 30.0;
    private double descuento = 0.2;

    public ClientePremium(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
    }

    public double getCuota() {
        return cuota;
    }

    public void setCuota(float cuota) {
        this.cuota = cuota;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }

    @Override
    public double calcularDescuento(){
        return descuento;
    }
}
