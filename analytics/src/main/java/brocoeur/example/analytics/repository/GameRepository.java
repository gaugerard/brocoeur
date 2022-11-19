package brocoeur.example.analytics.repository;

import brocoeur.example.analytics.model.Game;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

/**
 * This interface enables the support for reactive types.
 */
public interface GameRepository extends ReactiveCassandraRepository<Game, Integer> {
}
