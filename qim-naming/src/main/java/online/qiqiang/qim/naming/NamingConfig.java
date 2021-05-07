package online.qiqiang.qim.naming;

import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author qiqiang
 */
@ConfigurationProperties(prefix = "naming")
@Setter
public class NamingConfig {
    private String zookeeper;

    @Bean
    @ConditionalOnMissingBean(NamingService.class)
    public NamingService namingService() {
        return new ZkNamingServiceImpl(zookeeper);
    }
}