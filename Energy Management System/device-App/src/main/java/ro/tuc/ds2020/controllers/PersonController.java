package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.PersonDTO;
import ro.tuc.ds2020.services.PersonService;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/person")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
        System.out.println("Conectat cu success");
    }

    @PostMapping("/{id}")
    public ResponseEntity<UUID> insertPerson(@PathVariable UUID id) {
        System.out.println("Received ID via Path Variable: " + id);

        PersonDTO personDTO = new PersonDTO();
        personDTO.setIdPerson(id);

        UUID personID = personService.insert(personDTO);
        return new ResponseEntity<>(personID, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<PersonDTO>> getPersons() {
        try {
            List<PersonDTO> dtos = personService.findPersons();
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PersonDTO> getPerson(@PathVariable("id") UUID personId) {
        try {
            PersonDTO dto = personService.findPersonById(personId);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UUID> deletePerson(@PathVariable("id") UUID devicePersonId) {
        try {
            personService.deletePerson(personService.findPersonByIdPerson(devicePersonId).getId());
            return new ResponseEntity<>(devicePersonId, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(devicePersonId, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/persondevices/{id}")
    public ResponseEntity<List<DeviceDTO>> getPersonDevice(@PathVariable("id") UUID personId) {
            List<DeviceDTO> dto = personService.getPersonDevices(personId);
            return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
