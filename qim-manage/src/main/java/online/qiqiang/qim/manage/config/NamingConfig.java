package online.qiqiang.qim.manage.config;

import online.qiqiang.qim.naming.NamingService;
import online.qiqiang.qim.naming.ZkNamingServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiqiang
 */
@Configuration
public class NamingConfig {
    @Value("${naming.zookeeper}")
    private String zkAddress;

    @Bean
    public NamingService namingService() {
        return new ZkNamingServiceImpl(zkAddress);
    }
}