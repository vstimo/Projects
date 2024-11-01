package ro.tuc.ds2020.services;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonDTO {
    private UUID id;
    @NotNull
    private UUID idPerson;
}
