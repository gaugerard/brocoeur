package brocoeur.example.analytics.service.utils;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

@Component
public class RandomService {
    public int getCurrentTimeInSeconds() {
        return (int)new Date().getTime();
    }

    public int getRandomJobId() {
        final Random random = new Random();
        return random.nextInt(100000);  // returns pseudo-random value between 0 and 100000
    }
}
