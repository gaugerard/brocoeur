package brocoeur.example.analytics.repository;

import brocoeur.example.analytics.model.ServiceRequestStatus;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ServiceRequestStatusRepository extends ReactiveCassandraRepository<ServiceRequestStatus, Integer> {

    @AllowFiltering
    Flux<ServiceRequestStatus> findAllByStrategyAndStatus(final String strategy, final String status);

    @AllowFiltering
    Flux<ServiceRequestStatus> findAllByStatus(final String status);

}
