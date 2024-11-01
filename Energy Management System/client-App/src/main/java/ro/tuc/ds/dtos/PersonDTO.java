package ro.tuc.ds2020.dtos;

import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonDTO {
    private UUID id;
    private String username;
    private String password;
    private int age;
    private boolean isAdmin;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDTO personDTO = (PersonDTO) o;
        return age == personDTO.age &&
                Objects.equals(username, personDTO.username); //adaugat parola?
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, age);
    }
}
