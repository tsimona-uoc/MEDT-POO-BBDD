package MEDT.MEDT.modelo;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    private int numeroPedido; // ID Manual (no Auto-Increment)

    private LocalDateTime fechaHora;
    private int cantidad;

    // Relación Muchos Pedidos -> 1 Articulo
    @ManyToOne
    @JoinColumn(name = "codigoArticulo", nullable = false) // Nombre exacto de tu columna FK en SQL
    private Articulo articulo;

    // Relación Muchos Pedidos -> 1 Cliente
    @ManyToOne
    @JoinColumn(name = "nifCliente", nullable = false) // Nombre exacto de tu columna FK en SQL
    private Cliente cliente;

    public Pedido() {}

    public Pedido (int numPedido, int cantidad, LocalDateTime fechaHora, Articulo articulo, Cliente cliente){
        this.numeroPedido = numPedido;
        this.fechaHora = fechaHora;
        this.cantidad = cantidad;
        this.articulo = articulo;
        this.cliente = cliente;
    }

    // Getters y Setters
    public int getNumPedido() { return numeroPedido; }
    public void setNumPedido(int numPedido) { this.numeroPedido = numPedido; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public Articulo getArticulo() { return articulo; }
    public void setArticulo(Articulo articulo) { this.articulo = articulo; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    // Métodos de lógica
    public double calcularPrecioTotal(){
        double subtotal = cantidad * articulo.getPrecio();
        double gastos = articulo.getGastosEnvio();
        double descuento = cliente.calcularDescuento();
        gastos = gastos * (1 - descuento);
        return subtotal + gastos;
    }

    public boolean esCancelable() {
        if (articulo == null || fechaHora == null) return false;
        LocalDateTime limite = fechaHora.plusMinutes(articulo.getTiempoPrep());
        return LocalDateTime.now().isBefore(limite);
    }

    @Override
    public String toString() {
        return "Pedido{numeroPedido='" + numeroPedido + "', cantidad='" + cantidad + "', cliente='" + cliente + "', articulo=" + articulo + ", precioTotal='" + this.calcularPrecioTotal() + "'}";
    }
}