package brocoeur.example.analytics.repository;

import brocoeur.example.analytics.model.UserWinLossByGame;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * This interface enables the support for reactive types.
 */
@Repository
public interface UserWinLossByGameRepository extends ReactiveCassandraRepository<UserWinLossByGame, Integer> {

    Flux<UserWinLossByGame> findByGameIdAndUserId(final int gameId, final int userId);
}
