package brocoeur.example.nerima;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "nerima")
public class NerimaConfigProperties {

    private String nerimaToAnalyticsQueueName;
    private String serviceRequestQueueName;
    private String routingKey;
    private String rpcExchange;

}