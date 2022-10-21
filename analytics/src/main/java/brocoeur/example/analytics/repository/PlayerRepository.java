package brocoeur.example.analytics.repository;


import brocoeur.example.analytics.model.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends CrudRepository<Player,Long> {




}

