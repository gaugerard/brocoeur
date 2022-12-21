package brocoeur.example.analytics.repository;

import brocoeur.example.analytics.model.User;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface enables the support for reactive types.
 */
@Repository
public interface UserRepository extends ReactiveCassandraRepository<User, Integer> {
}
