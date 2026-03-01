package it.univr.track.controller.web;

import it.univr.track.entity.Device;
import it.univr.track.entity.Shipment;
import it.univr.track.repository.DeviceRepository;
import it.univr.track.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class ShipmentWebController {

    @Autowired
    private ShipmentRepository shipmentRepository;
    @Autowired
    private DeviceRepository deviceRepository;


    //create new shipment
    @RequestMapping("/web/newShipment")
    public String newShipment() {
        return "newShipment";
    }

    //list shipments
    @RequestMapping("/web/shipments")
    public String shipments(Model model) {
        model.addAttribute("shipments", shipmentRepository.findAll());
        return "shipments";
    }

    //activate/deactivate tracking
    @RequestMapping("/web/tracking")
    public String tracking(
            @RequestParam(name="id", required=true) Long id,Model model) {
        Optional<Shipment> result;
        result = shipmentRepository.findById(id);
        if (result.isPresent()){
            Shipment shipment = result.get();
            model.addAttribute("shipment", shipment);
            return "tracking";
        }
        else
            return "shipments";
    }

    //allocate a device to a shipment
    @RequestMapping("/web/shipmentAllocate")
    public String shipmentAllocate(Model model) {
        model.addAttribute("shipments", shipmentRepository.findAll());
        model.addAttribute("devices", deviceRepository.findAll());
        return "shipmentAllocate";
    }

    @RequestMapping("/web/createShipment")
    public String createShipment() {
        shipmentRepository.save(new Shipment());

        return "redirect:/web/shipments";
    }

    @PostMapping("/web/allocateAll")
    public String allocateAllDevices(
            @RequestParam(name="deviceIds", required=false) List<Long> deviceIds,
            @RequestParam(name="shipmentIds", required=false) List<Long> shipmentIds) {

        if (deviceIds == null) deviceIds = List.of();
        if (shipmentIds == null) shipmentIds = List.of();
        for (int i = 0; i < deviceIds.size(); i++) {
            Device device = deviceRepository.findById((Long)(deviceIds.get(i))).orElseThrow();
            Long shipmentId = shipmentIds.get(i);

            Shipment currentShipment = device.getShipment();
            if(currentShipment != null)
            {
                currentShipment.getDevices().remove(device);
                shipmentRepository.save(currentShipment);
            }

            if (shipmentId == -1) {
                device.setShipment(null);
            } else {
                Shipment shipment = shipmentRepository.findById(shipmentId).orElseThrow();
                shipment.getDevices().add(device);
                device.setShipment(shipment);
                shipmentRepository.save(shipment);

            }

            deviceRepository.save(device);
        }
        return "redirect:/web/shipments";
    }





    @RequestMapping("/toggleTracking")
    public String toggleTracking(@RequestParam Long id) {
        Optional<Shipment> shipment = shipmentRepository.findById(id);

        if (shipment.isPresent()) {
            shipment.get().setTracked(!shipment.get().isTracked());
            shipmentRepository.save(shipment.get());
        }
        return "redirect:/web/shipments";
    }

    @RequestMapping("/web/read")
    public String readShipment(@RequestParam(name="id", required=true) Long id,Model model)
    {
        Optional<Shipment> result;
        result = shipmentRepository.findById(id);
        if (result.isPresent()){
            Shipment shipment = result.get();
            model.addAttribute("shipment", shipment);
            return "shipment";
        }
        else
            return "shipments";
    }

    @RequestMapping("/web/delete")
    public String deleteShipment(@RequestParam(name="id", required=true) Long id,Model model)
    {
        Optional<Shipment> result;
        result = shipmentRepository.findById(id);
        if (result.isPresent()){
            Shipment shipment = result.get();
            for (Device device : shipment.getDevices()) {
                device.setShipment(null);
                deviceRepository.save(device);
            }
            shipmentRepository.delete(shipment);
        }
        return "shipments";
    }


}
