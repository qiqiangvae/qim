package online.qiqiang.qim.common.server;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author qiqiang
 */
@Getter
@Setter
@ToString
public class ImServerInfo {
    private String id;
    private String address;

    public void setAddress(String address) {
        this.address = address;
    }
}