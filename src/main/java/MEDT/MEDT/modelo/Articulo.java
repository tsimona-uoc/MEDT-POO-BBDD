package MEDT.MEDT.modelo;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "articulo", schema = "MEDT_POO_DDBB")
public class Articulo {
    @Id
    @Column(name = "codigo", nullable = false, length = 200)
    private String codigo;

    @Column(name = "descripcion", nullable = false, length = 200)
    private String descripcion;

    @Column(name = "precio", nullable = false, precision = 10)
    private BigDecimal precio;

    @Column(name = "gastosEnvio", nullable = false, precision = 10)
    private BigDecimal gastosEnvio;

    @Column(name = "tiempoPreparacion", nullable = false)
    private Integer tiempoPreparacion;

    @OneToMany(mappedBy = "articulo")
    private Set<Pedido> pedidos = new LinkedHashSet<>();

    public Articulo() {}

    public Articulo(String codigo, String descripcion, BigDecimal precio, BigDecimal gastosEnvio, Integer tiempoPreparacion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.gastosEnvio = gastosEnvio;
        this.tiempoPreparacion = tiempoPreparacion;
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

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getGastosEnvio() {
        return gastosEnvio;
    }

    public void setGastosEnvio(BigDecimal gastosEnvio) {
        this.gastosEnvio = gastosEnvio;
    }

    public Integer getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    public void setTiempoPreparacion(Integer tiempoPreparacion) {
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public Set<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(Set<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    @Override
    public String toString() {
        return "Articulo{" +
                "código='" + codigo + '\'' +
                ", descripción='" + descripcion + '\'' +
                ", precio='" + precio + '\'' +
                ", gastos de envío=" + gastosEnvio + '\'' +
                ", tiempo de preparación=" + tiempoPreparacion +
                '}';
    }
}