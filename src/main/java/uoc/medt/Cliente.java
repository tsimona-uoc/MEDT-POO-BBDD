package uoc.medt;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private String nombre;
    private String domicilio;
    private String nif;
    private String email;

    // Asociación con Pedido (composición)
    private List<Pedido> pedidos = new ArrayList<>();

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

    // Método para agregar pedidos (composición)
    public void addPedido(Pedido pedido) {
        pedidos.add(pedido);
    }
}
