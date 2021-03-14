package online.qiqiang.qim.server.im;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author qiqiang
 */
@ConfigurationProperties(prefix = "im-server")
@Component
@Getter
@Setter
public class ImServerConfiguration {
    private String id;
    private String host;
    private int port;

    public String getAddress() {
        return host + ":" + port;
    }
}