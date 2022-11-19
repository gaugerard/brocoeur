package brocoeur.example.analytics.repository;

import brocoeur.example.analytics.model.User;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

/**
 * This interface enables the support for reactive types.
 */
public interface UserRepository extends ReactiveCassandraRepository<User, Integer> {
}
