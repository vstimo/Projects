package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ro.tuc.ds2020.controllers.handlers.ResponseMessage;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.PersonDTO;
import ro.tuc.ds2020.dtos.PersonDetailsDTO;
import ro.tuc.ds2020.dtos.builders.PersonBuilder;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.repositories.PersonRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PersonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);
    private final PersonRepository personRepository;
    private final RestTemplate restTemplate;

    private final String ip = "http://172.18.0.5:8080/person/";

    @Autowired
    public PersonService(PersonRepository personRepository, RestTemplate restTemplate) {
        this.personRepository = personRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional(rollbackOn = {IOException.class, InterruptedException.class})
    public UUID insert(PersonDetailsDTO personDTO) {
        Person person = PersonBuilder.toEntity(personDTO);
        person = personRepository.save(person);

        String url = ip + person.getId_person();
        System.out.println("Request URL: " + url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<UUID> response = restTemplate.exchange(url, HttpMethod.POST, request, UUID.class);
            System.out.println("Response: " + response.getBody());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            LOGGER.error("Error inserting person: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw e;
        }
    }


    public List<PersonDTO> findPersons() {
        List<Person> personList = personRepository.findAll();
        return personList.stream()
                .map(PersonBuilder::toPersonDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO findPersonById(UUID id) {
        Optional<Person> prosumerOptional = personRepository.findById(id);
        if (prosumerOptional.isEmpty()) {
            LOGGER.error("Person with id {} was not found in db", id);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + id); //getSimpleUsername??
        }
        return PersonBuilder.toPersonDTO(prosumerOptional.get());
    }

    public PersonDTO findPersonByUsername(String username) {
        List<Person> persons = personRepository.findByUsername(username);
        if (persons.isEmpty()) {
            LOGGER.debug("Person with username {} was not found in db", username);
            return null;
        }
        Person person = persons.get(0);
        return PersonBuilder.toPersonDTO(person);
    }

    @Transactional(rollbackOn = {IOException.class, InterruptedException.class})
    public void deletePerson(UUID id) {
        Optional<Person> personOptional = personRepository.findById(id);

        if (personOptional.isEmpty()) {
            //LOGGER.error("Person with id {} was not found in db", id);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        }

        String url = ip + id;
        System.out.println("Request URL: " + url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<UUID> response = restTemplate.exchange(url, HttpMethod.DELETE, request, UUID.class);
            System.out.println("Response: " + response.getBody());
            //return response.getBody();
        } catch (HttpClientErrorException e) {
            //LOGGER.error("Error inserting person: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw e;
        }

        personRepository.deleteById(id);
        //LOGGER.debug("Person with id {} was deleted from db", id);
    }

    public PersonDTO updatePerson(UUID id, PersonDetailsDTO personDTO) {
        Optional<Person> personOptional = personRepository.findById(id);
        if (personOptional.isEmpty()) {
            //LOGGER.error("Person with id {} was not found in db", id);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        }

        Person person = personOptional.get();
        person.setUsername(personDTO.getUsername());
        person.setPassword(personDTO.getPassword());
        person.setAge(personDTO.getAge());
        person.setAdmin(personDTO.isAdmin());

        Person updatedPerson = personRepository.save(person);
        System.out.println("Person with " + updatedPerson.getId_person() + " was updated in db");
        LOGGER.debug("Person with id {} was updated in db", updatedPerson.getId_person());
        return PersonBuilder.toPersonDTO(updatedPerson);
    }
}
