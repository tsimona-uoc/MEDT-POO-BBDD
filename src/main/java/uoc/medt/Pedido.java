package uoc.medt;

import java.time.LocalDateTime;

public class Pedido {
    private int numPedido;
    private LocalDateTime fechaHora;
    private int cantidad;

    // Asociación con Articulo
    private Articulo articulo;

    public int getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(int numPedido) {
        this.numPedido = numPedido;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    // Método para calcular el precio total del pedido
    public double calcularPrecio() {
        if (articulo == null) return 0;
        return (articulo.getPrecio() * cantidad) + articulo.getGastosEnvio();
    }

    // Un pedido no puede cancelarse si ya ha pasado el tiempo de preparación
    public boolean esCancelable() {
        if (articulo == null || fechaHora == null) return false;
        LocalDateTime limite = fechaHora.plusDays(articulo.getTiempoPrep());
        return LocalDateTime.now().isBefore(limite);
    }
}
