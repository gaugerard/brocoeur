package brocoeur.example.analytics.repository;

import brocoeur.example.analytics.model.UserWinLossByGame;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * This interface enables the support for reactive types.
 */
@Repository
public interface UserWinLossByGameRepository extends ReactiveCassandraRepository<UserWinLossByGame, Integer> {

    Mono<UserWinLossByGame> findByGameIdAndUserId(final int gameId, final int userId);
}
