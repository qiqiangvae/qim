package online.qiqiang.qim.protocol;

/**
 * @author qiqiang
 */
public class ImProtocolException extends RuntimeException{
    public ImProtocolException() {
    }

    public ImProtocolException(String message) {
        super(message);
    }

    public ImProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImProtocolException(Throwable cause) {
        super(cause);
    }

    public ImProtocolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}