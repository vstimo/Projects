package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.PersonDTO;
import ro.tuc.ds2020.dtos.PersonDetailsDTO;
import ro.tuc.ds2020.entities.Person;

public class PersonBuilder {

    private PersonBuilder() {
    }

    public static PersonDTO toPersonDTO(Person person) {
        return new PersonDTO(person.getId_person(), person.getUsername(), person.getPassword(), person.getAge(), person.isAdmin());
    }

    public static Person toEntity(PersonDetailsDTO personDetailsDTO) {
        return new Person(personDetailsDTO.getId(),
                personDetailsDTO.getUsername(),
                personDetailsDTO.getPassword(),
                personDetailsDTO.getAge(),
                personDetailsDTO.isAdmin());
    }
}
