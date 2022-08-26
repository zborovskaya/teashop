package by.zborovskaya.final_project.service;

public class QuantityException extends Exception {
    public QuantityException() {
        super();
    }

    public QuantityException(String message) {
        super(message);
    }

    public QuantityException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuantityException(Throwable cause) {
        super(cause);
    }
}
