package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.PersonDTO;
import ro.tuc.ds2020.dtos.PersonDetailsDTO;
import ro.tuc.ds2020.services.PersonService;

import javax.validation.Valid;
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
    }

    @GetMapping()
    public ResponseEntity<List<PersonDTO>> getPersons() {
        List<PersonDTO> dtos = personService.findPersons();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UUID> insertPerson(@Valid @RequestBody PersonDetailsDTO personDTO) {
        UUID id = personService.insert(personDTO);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PersonDTO> getPerson(@PathVariable("id") UUID personId) {
        PersonDTO dto = personService.findPersonById(personId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(value = "/username/{username}")
    public ResponseEntity<PersonDTO> getPersonByUsername(@PathVariable("username") String username) {
        PersonDTO dto = personService.findPersonByUsername(username);
        if (dto == null)
            return new ResponseEntity<>(new PersonDTO(), HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PersonDTO> updatePerson(@PathVariable("id") UUID personId, @Valid @RequestBody PersonDetailsDTO personDTO) {
        PersonDTO updatedPerson = personService.updatePerson(personId, personDTO);
        return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UUID> deletePerson(@PathVariable("id") UUID personId) {
        personService.deletePerson(personId);
        return new ResponseEntity<>(personId, HttpStatus.OK);
    }
}
