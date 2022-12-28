package brocoeur.example.analytics;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "analytics")
public class AnalyticsConfigProperties {

    private String blockedRequestRate;
    private String pokerFetchingRate;

    private String analyticInputQueueName;
    private String nerimaToAnalyticsQueueName;

    public String getBlockedRequestRate(){
        return (blockedRequestRate != null ) ? blockedRequestRate : "30000";
    }

    public String getPokerFetchingRate(){
        return (pokerFetchingRate != null ) ? pokerFetchingRate : "30000";
    }

}
