package brocoeur.example.games;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "games")
public class GamesConfigProperties {

    private String serviceRequestQueueName;
    private String analyticInputQueueName;
    private String rpcExchange;
    private String autoStartup;

}