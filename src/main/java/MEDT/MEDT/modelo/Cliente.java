package MEDT.MEDT.modelo;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "cliente", schema = "MEDT_POO_DDBB")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
public abstract class Cliente {
    @Id
    @Column(name = "nif", nullable = false, length = 20)
    private String nif;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "domicilio", nullable = false, length = 200)
    private String domicilio;

    @Column(name = "email", nullable = false, length = 200)
    private String email;

    @OneToMany(mappedBy = "cliente")
    private Set<Pedido> pedidos = new LinkedHashSet<>();

    /// Required by JPA
    public Cliente() {}

    public Cliente(String nombre, String domicilio, String nif, String email) {
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.nif = nif;
        this.email = email;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(Set<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public abstract double calcularDescuento();

    @Override
    public String toString() {
        return "Cliente{" +
                "nombre='" + nombre + '\'' +
                ", domicilo='" + domicilio + '\'' +
                ", nif='" + nif + '\'' +
                ", email=" + email +
                ", tipo=" + (this instanceof ClientePremium ? "premium" : "estandar")  +
                '}';
    }
}