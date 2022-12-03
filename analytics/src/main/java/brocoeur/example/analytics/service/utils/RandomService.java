package brocoeur.example.analytics.service.utils;

import org.springframework.stereotype.Component;

@Component
public class RandomService {
    public int getCurrentTimeInSeconds() {
        return (int) java.time.Instant.now().getEpochSecond();
    }

    public int getRandomJobId() {
        return (int) (Math.random() * 100000);
    }
}
