package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
    //List<Device> findByLocation(String location);
    List<Device> findByPerson(Person person);
}
