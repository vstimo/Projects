package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.tuc.ds2020.entities.Person;

import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {
    Person findByIdPerson(UUID id);
}
