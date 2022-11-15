package net.ausiasmarch.andamio.exception;

public class JWTException extends RuntimeException {

    public JWTException(String msg) {
        super("ERROR: JWTException: " + msg);
    }

}
