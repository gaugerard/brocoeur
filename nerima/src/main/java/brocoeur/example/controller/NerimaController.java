package brocoeur.example;

public class NerimaService {

    public String play(final ServiceRequest serviceRequest) {
        final String userId = serviceRequest.userId();
        final GameStrategyTypes gameStrategyTypes = serviceRequest.gameStrategyTypes();
        final GameStrategy gameStrategy = gameStrategyTypes.getGameStrategy();

        final GamePlay gamePlay = gameStrategy.play();
        System.out.println(userId + " used strategy: " + gameStrategy + " and will play: " + gamePlay);
        return userId + " used strategy: " + gameStrategy + " and will play: " + gamePlay;
    }
}
