package ro.tuc.ds2020.dtos.builders;

import lombok.NoArgsConstructor;
import ro.tuc.ds2020.dtos.PersonDTO;
import ro.tuc.ds2020.entities.Person;

@NoArgsConstructor
public class PersonBuilder {

    public static PersonDTO toPersonDTO(Person person) {
        return PersonDTO.builder()
                .id(person.getId())
                .idPerson(person.getIdPerson())
                .build();
    }

    public static Person toEntity(PersonDTO personDTO) {
        return Person.builder()
                .id(personDTO.getId())
                .idPerson(personDTO.getIdPerson())
                .build();
    }
}
