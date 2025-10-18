package MEDT.MEDT.modelo;

public class Articulo {
    private String codigo;
    private String descripcion;
    private double precio;
    private double gastosEnvio;
    private int tiempoPrep;

    public Articulo(String codigo, String descripcion, double precio, double gastosEnvio, int tiempoPrep){
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.gastosEnvio = gastosEnvio;
        this.tiempoPrep = tiempoPrep;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getGastosEnvio() {
        return gastosEnvio;
    }

    public void setGastosEnvio(double gastosEnvio) {
        this.gastosEnvio = gastosEnvio;
    }

    public int getTiempoPrep() {
        return tiempoPrep;
    }

    public void setTiempoPrep(int tiempoPrep) {
        this.tiempoPrep = tiempoPrep;
    }


    @Override
    public String toString() {
        return "Articulo{" +
                "código='" + codigo + '\'' +
                ", descripción='" + descripcion + '\'' +
                ", precio='" + precio + '\'' +
                ", gastos de envío=" + gastosEnvio + '\'' +
                ", tiempo de preparación=" + tiempoPrep +
                '}';
    }
}
