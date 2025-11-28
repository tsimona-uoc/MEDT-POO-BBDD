package MEDT.MEDT.modelo;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cliente")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
public abstract class Cliente {
    @Id
    @Column(name = "nif", nullable = false)
    private String nif;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "domicilio", nullable = false)
    private String domicilio;

    @Column(name = "email", nullable = false)
    private String email;

    @OneToMany(mappedBy = "cliente")
    // Asociación con Pedido
    private List<Pedido> pedidos = new ArrayList<>();
    public Cliente() {}
    //Constructor
    public Cliente(String nombre, String domicilio, String nif, String email) {
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.nif = nif;
        this.email = email;
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
