package by.zborovskaya.final_project.control.listener;

public class ListenerException extends RuntimeException{

    public ListenerException() {
        super();
    }

    public ListenerException(String message) {
        super(message);
    }

    public ListenerException(Exception e) {
        super(e);
    }

    public ListenerException(String message, Exception e) {
        super(message, e);
    }
}
