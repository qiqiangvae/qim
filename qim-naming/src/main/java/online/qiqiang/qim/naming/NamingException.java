package online.qiqiang.qim.naming;

/**
 * @author qiqiang
 */
public class NamingException extends RuntimeException {
    public NamingException() {
    }

    public NamingException(String message) {
        super(message);
    }

    public NamingException(String message, Throwable cause) {
        super(message, cause);
    }

    public NamingException(Throwable cause) {
        super(cause);
    }

    public NamingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}