package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.services.DeviceService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/device")
public class DeviceController {
    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping()
    public ResponseEntity<UUID> insertDevice(@Valid @RequestBody DeviceDTO deviceDTO) {
        UUID deviceID = deviceService.insert(deviceDTO);
        return new ResponseEntity<>(deviceID, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<DeviceDTO>> getDevices() {
        List<DeviceDTO> dtos = deviceService.findDevices();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DeviceDTO> getDevice(@PathVariable("id") UUID deviceId) {
        try{
            DeviceDTO dto = deviceService.findDeviceById(deviceId);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping(value = "/{id}")
    public ResponseEntity<DeviceDTO> updateDevice(@PathVariable("id") UUID deviceId, @Valid @RequestBody DeviceDTO deviceDTO) {
        try{
            DeviceDTO updatedDevice = deviceService.updateDevice(deviceId, deviceDTO);
            return new ResponseEntity<>(updatedDevice, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(deviceDTO, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UUID> deleteDevice(@PathVariable("id") UUID deviceId) {
        try{
            deviceService.deleteDevice(deviceId);
            return new ResponseEntity<>(deviceId, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(deviceId, HttpStatus.NOT_FOUND);
        }

    }
}
