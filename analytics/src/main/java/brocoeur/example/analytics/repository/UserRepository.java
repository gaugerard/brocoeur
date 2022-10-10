package brocoeur.example.repository;


import brocoeur.example.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends CrudRepository<User,Long> {




}

