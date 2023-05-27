package es.blaster.projectum.exception;

public class JWTException extends RuntimeException {

    public JWTException(String msg) {
        super("ERROR: JWTException: " + msg);
    }

}
