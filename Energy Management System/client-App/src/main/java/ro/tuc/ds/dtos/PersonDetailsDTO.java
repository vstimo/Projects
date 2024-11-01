package ro.tuc.ds2020.dtos;

import lombok.*;
import ro.tuc.ds2020.dtos.validators.annotation.AgeLimit;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonDetailsDTO {

    private UUID id;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @AgeLimit(limit = 18)
    private int age;
    @NotNull
    private boolean isAdmin;
}
