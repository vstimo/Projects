package ro.tuc.ds2020.services;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.PersonDTO;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.dtos.builders.PersonBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.PersonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PersonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository personRepository;
    private final DeviceRepository deviceRepository;

    public List<PersonDTO> findPersons() {
        List<Person> personList = personRepository.findAll();
        return personList.stream()
                .map(PersonBuilder::toPersonDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO findPersonById(UUID id) { //inainte era PersonDTO
        Optional<Person> prosumerOptional = personRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Person with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        return PersonBuilder.toPersonDTO((prosumerOptional.get()));
    }

    public PersonDTO findPersonByIdPerson(UUID id) {
        Person person = personRepository.findByIdPerson(id);
        if (person == null) {
            LOGGER.error("Person with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        return PersonBuilder.toPersonDTO(person);
    }

    public UUID insert(PersonDTO personDTO) {
        System.out.println("Aicia ajunge");
        Person person = PersonBuilder.toEntity(personDTO);

        person = personRepository.save(person);
        LOGGER.debug("Person with id {} was inserted in db", person.getId());
        System.out.println("Inserted Person: " + person);
        return person.getId();
    }

    /*public PersonDTO updatePerson(UUID id, PersonDTO personDTO) {
        Optional<Person> personOptional = personRepository.findById(id);
        if (!personOptional.isPresent()) {
            LOGGER.error("Person with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        Person person = personOptional.get();
        person.setIdPerson(personDTO.getIdPerson());

        Person updatePerson = personRepository.save(person);
        LOGGER.debug("Person with id {} was updated in db", updatePerson.getId());
        return PersonBuilder.toPersonDTO(updatePerson);
    }*/

    public void deletePerson(UUID id) {
        Optional<Person> personOptional = personRepository.findById(id);
        if (!personOptional.isPresent()) {
            LOGGER.error("Person with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        personRepository.deleteById(id);
        LOGGER.debug("Person with id {} was deleted from db", id);
    }

    public List<DeviceDTO> getPersonDevices(UUID id){
        try {
            Person person = personRepository.findByIdPerson(id);

            List<Device> listDevices = deviceRepository.findByPerson(person);
            List<DeviceDTO> deviceDTOList = new ArrayList<>();
            for (Device device : listDevices){
                DeviceDTO d = DeviceBuilder.toDeviceDTO(device);
                deviceDTOList.add(d);
            }
            return deviceDTOList;
        } catch (Exception e) {
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
    }

}
