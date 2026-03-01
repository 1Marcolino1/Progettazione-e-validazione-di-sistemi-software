package it.univr.track.controller.api;

import it.univr.track.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import it.univr.track.entity.*;

@Controller
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository;

    // add new device
    @PostMapping("/api/device")
    public boolean addDevice() {
        deviceRepository.save(new Device());
        return true;
    }

    // read the device configuration
    @GetMapping("/api/device/{deviceId}")
    public Device readDeviceConfig(@PathVariable("deviceId") Long id) {
        return new Device();
    }

    // update device configuration
    @PutMapping("/api/device")
    public boolean editDevice() {
        return true;
    }

    // decommission a device
    @DeleteMapping("/api/device")
    public boolean deleteDevice() {
        return true;
    }

    // list all the devices that are visible for this user
    @GetMapping("/api/devices")
    public Device[] devices() {
        return new Device[0];
    }


}