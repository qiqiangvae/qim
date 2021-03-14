package online.qiqiang.qim.common.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author qiqiang
 */
@Getter
@Setter
public class QimResponse<T> implements Serializable {
    private static final long serialVersionUID = 42L;

    private String ok;
    private T data;

    public QimResponse() {
    }

    public QimResponse(String ok) {
        this.ok = ok;
    }

    public QimResponse(String ok, T data) {
        this.ok = ok;
        this.data = data;
    }

    public QimResponse(T data) {
        this.ok = "OK";
        this.data = data;
    }

    public static QimResponse<Void> ok() {
        return new QimResponse<>("OK");
    }

    public static QimResponse<Void> fail() {
        return new QimResponse<>("FAIL");
    }
}