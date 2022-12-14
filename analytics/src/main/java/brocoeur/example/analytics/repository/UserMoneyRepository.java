package brocoeur.example.analytics.repository;

import brocoeur.example.analytics.model.UserMoney;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMoneyRepository extends ReactiveCassandraRepository<UserMoney, Integer> {
}
