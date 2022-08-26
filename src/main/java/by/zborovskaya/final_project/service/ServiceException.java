package by.zborovskaya.final_project.service;

public class ServiceException extends Exception{
    private static final long serialVersionUID = -2795804103099775537L;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Exception e) {
        super(e);
    }

    public ServiceException(String message, Exception e) {
        super(message, e);
    }
}
