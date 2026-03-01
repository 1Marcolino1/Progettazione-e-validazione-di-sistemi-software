package it.univr.track.controller.api;

import it.univr.track.repository.DeviceRepository;
import it.univr.track.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import it.univr.track.entity.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class ShipmentController {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;

    // create a shipment
    @PostMapping("/shipment")
    public boolean createShipment() {
        Shipment shipment = new Shipment();
        shipmentRepository.save(shipment);
        return true;
    }

    @GetMapping("/shipment/{shipmentId}")
    @ResponseBody
    public Shipment readShipment(@PathVariable Long shipmentId) {
        return shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Shipment not found"));
    }

    // update a shipment
    @PostMapping("/api/shipment")
    public boolean editShipment(@RequestBody Shipment shipment) {
        Shipment existing = shipmentRepository.findById(shipment.getId()) .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Shipment not found"));;
        existing.setName(shipment.getName());
        shipmentRepository.save(existing);
        return true;
    }


    @DeleteMapping("/api/shipment")
    public void deleteShipment(
            @RequestParam long id,
            @RequestParam(defaultValue = "false") boolean forceDeletion
    ) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Shipment not found"));

        if (!forceDeletion && !shipment.getDevices().isEmpty()) {
            throw new ResponseStatusException(CONFLICT,
                    "Shipment has associated devices");
        }
        else if (!shipment.getDevices().isEmpty()) {
            for (Device device : shipment.getDevices()) {
                device.setShipment(null);
                deviceRepository.save(device);
            }
        }

        shipmentRepository.delete(shipment);
    }
    // list all the shipments
    @GetMapping("/api/shipments")
    public Shipment[] readShipments() {
        Iterable<Shipment> iterable = shipmentRepository.findAll();
        List<Shipment> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list.toArray(new Shipment[0]);
    }

    // start/stop tracking
    @PostMapping("/api/shipment/tracking")
    public boolean setShipmentTracking(@RequestParam long id, @RequestParam boolean tracked) {
       Shipment existingShipment = shipmentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Shipment not found"));;
       existingShipment.setTracked(tracked);
       shipmentRepository.save(existingShipment);
       return true;
    }

    @PostMapping("/api/shipment/allocate")
    public void allocateDevice(
            @RequestParam long shipmentId,
            @RequestParam long deviceId,
            @RequestParam(defaultValue = "false") boolean forceAllocation
    ) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Shipment not found"));

        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Device not found"));

        if (!forceAllocation && device.getShipment() != null) {
            throw new ResponseStatusException(CONFLICT,
                    "Device already allocated to another shipment");
        }

        device.setShipment(shipment);
        shipment.getDevices().add(device);
        deviceRepository.save(device);
    }

    @PostMapping("/api/shipment/deallocate")
    public void deallocateDevice(
            @RequestParam long shipmentId,
            @RequestParam long deviceId
    ) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Shipment not found"));

        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Device not found"));

        if (device.getShipment() != shipment) {
            throw new ResponseStatusException(CONFLICT,
                    "Device not associated with this shipment");
        }

        device.setShipment(null);
        shipment.getDevices().remove(device);
        deviceRepository.save(device);
    }
}
