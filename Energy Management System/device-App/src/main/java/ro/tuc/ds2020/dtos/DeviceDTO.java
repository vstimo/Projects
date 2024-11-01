package ro.tuc.ds2020.dtos;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceDTO {
    private UUID id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private String location;
    @NotNull
    private int max;
    @NotNull
    private UUID idPerson;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceDTO deviceDTO = (DeviceDTO) o;
        return Objects.equals(name, deviceDTO.name) && Objects.equals(location, deviceDTO.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location);
    }
}
