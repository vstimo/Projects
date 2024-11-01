package ro.tuc.ds2020.dtos.builders;

import lombok.NoArgsConstructor;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.Person;

@NoArgsConstructor
public class DeviceBuilder {

    public static DeviceDTO toDeviceDTO(Device device) {
        return DeviceDTO.builder()
                .id(device.getId())
                .name(device.getName())
                .description(device.getDescription())
                .location(device.getLocation())
                .max(device.getMax())
                .idPerson(device.getPerson().getIdPerson())
                .build();
    }

    public static Device toEntity(DeviceDTO deviceDTO, Person person) {
        return Device.builder()
                .id(deviceDTO.getId())
                .name(deviceDTO.getName())
                .description(deviceDTO.getDescription())
                .location(deviceDTO.getLocation())
                .max(deviceDTO.getMax())
                .person(person)
                .build();
    }
}
