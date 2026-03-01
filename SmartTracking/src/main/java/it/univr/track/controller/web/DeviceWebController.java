package it.univr.track.controller.web;

import it.univr.track.entity.Device;
import it.univr.track.entity.Shipment;
import it.univr.track.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class DeviceWebController {

    @Autowired
    DeviceRepository deviceRepository;

    //provisioning of a new device (QR-code?)
    @RequestMapping("/web/provision")
    public String provision() {
        return "provision";
    }

    //decommissioning of an old device
    @RequestMapping("/web/decommission")
    public String decommission() {
        return "decommission";
    }

    //list devices
    @RequestMapping("/web/devices")
    public String devices() {
        return "devices";
    }

    //view device configuration
    @RequestMapping("/web/configDevice")
    public String configDevice() {
        return "configDevice";
    }

    //edit device configuration
    @RequestMapping("/web/editConfigDevice")
    public String editConfigDevice() {
        return "editConfigDevice";
    }

    //send configuration to device
    @RequestMapping("/web/sendConfigDevice")
    public String sendConfigDevice() {
        return "sendConfigDevice";
    }

    @RequestMapping("/web/createDevice")
    public String createDevice() {
        deviceRepository.save(new Device());

        return "redirect:/web/devices";
    }

}
