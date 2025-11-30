package MEDT.MEDT.modelo;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "pedido", schema = "MEDT_POO_DDBB")
public class Pedido {
    @Id
    @Column(name = "numeroPedido", nullable = false)
    private Integer id;

    @Column(name = "fechaHora", nullable = false)
    private Instant fechaHora;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "codigoArticulo")
    private Articulo articulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "nifCliente")
    private Cliente cliente;

    /// Required by JPA
    public Pedido() {}

    public Pedido(Integer id, Instant fechaHora, Integer cantidad, Articulo articulo, Cliente cliente) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.cantidad = cantidad;
        this.articulo = articulo;
        this.cliente = cliente;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Instant fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    // Metodo para calcular el precio total del pedido
    public double calcularPrecioTotal(){
        double subtotal = cantidad.doubleValue() * articulo.getPrecio().doubleValue();
        double gastos = articulo.getGastosEnvio().doubleValue();

        //Aplicar descuento si cliente Premium
        double descuento = cliente.calcularDescuento();
        gastos = gastos * (1 - descuento);

        return subtotal + gastos;
    }

    // Un pedido no puede cancelarse si ya ha pasado el tiempo de preparación
    public boolean esCancelable() {
        if (articulo == null || fechaHora == null) return false;
        LocalDateTime limite = LocalDateTime.ofInstant(fechaHora, ZoneOffset.UTC).plusMinutes(articulo.getTiempoPreparacion());
        return LocalDateTime.now().isBefore(limite);
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "numeroPedido='" + id + '\'' +
                ", cantidad='" + cantidad + '\'' +
                ", cliente='" + cliente + '\'' +
                ", articulo=" + articulo +
                ", precioTotal='" + this.calcularPrecioTotal() + '\'' +
                '}';
    }
}