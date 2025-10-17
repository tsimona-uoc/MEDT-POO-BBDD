package MEDT.MEDT.modelo.excepciones;

public class PedidoNoCancelableException extends Exception {
    public PedidoNoCancelableException(String mensaje) {
        super(mensaje);
    }
}

