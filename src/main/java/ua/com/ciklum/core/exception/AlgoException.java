package ua.com.ciklum.core.exception;

public class AlgoException extends RuntimeException {

    public AlgoException(final String message, final Throwable th) {
        super(message, th);
    }

}
