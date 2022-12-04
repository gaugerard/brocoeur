package brocoeur.example.analytics.repository;

import brocoeur.example.analytics.model.ServiceRequestStatus;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface ServiceRequestStatusRepository extends ReactiveCassandraRepository<ServiceRequestStatus, Integer> {
}
