package MEDT.MEDT.modelo.excepciones;

public class PedidoNoCancelableException extends Exception {
    public PedidoNoCancelableException(String message) {
        super(message);
    }
}