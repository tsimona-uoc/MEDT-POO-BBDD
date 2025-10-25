package MEDT.MEDT.modelo;

import java.time.LocalDateTime;

public class Pedido {
    private int numPedido;
    private LocalDateTime fechaHora;
    private int cantidad;

    // Asociación con Articulo
    private Articulo articulo;

    //Asosiación con Cliente
    private Cliente cliente;

    public Pedido (int numPedido, int cantidad, LocalDateTime fechaHora, Articulo articulo, Cliente cliente){
        this.numPedido = numPedido;
        this.fechaHora = fechaHora;
        this.cantidad = cantidad;
        this.articulo = articulo;
        this.cliente = cliente;
    }

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

    public Cliente getCliente() {return cliente;}

    public void setCliente(Cliente cliente) {this.cliente = cliente;}


    // Metodo para calcular el precio total del pedido
    public double calcularPrecioTotal(){
        double subtotal = cantidad * articulo.getPrecio();
        double gastos = articulo.getGastosEnvio();

        //Aplicar descuento si cliente Premium
        double descuento = cliente.calcularDescuento();
        gastos = gastos * (1 - descuento);

        return subtotal + gastos;
    }

    // Un pedido no puede cancelarse si ya ha pasado el tiempo de preparación
    public boolean esCancelable() {
        if (articulo == null || fechaHora == null) return false;
        LocalDateTime limite = fechaHora.plusMinutes(articulo.getTiempoPrep());
        return LocalDateTime.now().isBefore(limite);
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "numeroPedido='" + numPedido + '\'' +
                ", cantidad='" + cantidad + '\'' +
                ", cliente='" + cliente + '\'' +
                ", articulo=" + articulo +
                ", precioTotal='" + this.calcularPrecioTotal() + '\'' +
                '}';
    }
}
