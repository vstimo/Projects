package ro.tuc.ds2020.services;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.dtos.builders.PersonBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.repositories.DeviceRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);

    private final DeviceRepository deviceRepository;
    private final PersonService personService;

    @Transactional(rollbackOn = {IOException.class, InterruptedException.class})
    public UUID insert(DeviceDTO deviceDTO) {
        try{
            Device device = DeviceBuilder.toEntity(deviceDTO, PersonBuilder.toEntity(personService.findPersonByIdPerson(deviceDTO.getIdPerson())));
            device = deviceRepository.save(device);
            LOGGER.debug("Device with id {} was inserted in db", device.getId());
            return device.getId();
        }catch (Exception e){
            LOGGER.error("Error inserting device", e);
            throw new RuntimeException(e);
        }
    }

    public List<DeviceDTO> findDevices() {
        List<Device> deviceList = deviceRepository.findAll();
        return deviceList.stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public DeviceDTO findDeviceById(UUID id) {
        Optional<Device> prosumerOptional = deviceRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        return DeviceBuilder.toDeviceDTO(prosumerOptional.get());
    }

    public DeviceDTO updateDevice(UUID id, DeviceDTO deviceDTO) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (!deviceOptional.isPresent()) {
            LOGGER.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }

        Device device = deviceOptional.get();
        device.setName(deviceDTO.getName());
        device.setDescription(deviceDTO.getDescription());
        device.setLocation(deviceDTO.getLocation());
        device.setMax(deviceDTO.getMax());
        device.setPerson(PersonBuilder.toEntity(personService.findPersonByIdPerson(deviceDTO.getIdPerson())));

        Device updatedDevice = deviceRepository.save(device);
        LOGGER.debug("Device with id {} was updated in db", updatedDevice.getId());
        return DeviceBuilder.toDeviceDTO(updatedDevice);
    }

    public void deleteDevice(UUID id) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (!deviceOptional.isPresent()) {
            LOGGER.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }

        deviceRepository.deleteById(id);
        LOGGER.debug("Device with id {} was deleted from db", id);
    }

}
