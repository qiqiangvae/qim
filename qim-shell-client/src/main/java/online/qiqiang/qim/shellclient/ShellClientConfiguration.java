package online.qiqiang.qim.shellclient;

import online.qiqiang.qim.client.ImClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiqiang
 */
@Configuration
public class ShellClientConfiguration {
    @Value("${qim-route.address}")
    private String routeAddress;

    @Bean
    public ImClientService imClientService() {
        return new ImClientService(routeAddress);
    }

}