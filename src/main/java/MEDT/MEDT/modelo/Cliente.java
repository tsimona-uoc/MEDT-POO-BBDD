package MEDT.MEDT.modelo;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "cliente")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
public abstract class Cliente {
    private String nombre;
    private String domicilio;
    @Id
    private String nif;
    private String email;
    @Column(name = "tipo", insertable = false, updatable = false)
    private String tipo;

    // Asociación con Pedido
    @Transient
    private List<Pedido> pedidos = new ArrayList<>();

    //Constructor
    public Cliente(String nombre, String domicilio, String nif, String email) {
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.nif = nif;
        this.email = email;
    }

    protected Cliente() {
    }

    // Getters y Setters
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

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }


    //Metodo abstracto
    public abstract double calcularDescuento();

    @Override
    public String toString() {
        return "Cliente{" +
                "nombre='" + nombre + '\'' +
                ", domicilo='" + domicilio + '\'' +
                ", nif='" + nif + '\'' +
                ", email=" + email +
                '}';
    }
}
