package it.univr.track.tests;

import it.univr.track.controller.web.ShipmentWebController;
import it.univr.track.entity.Device;
import it.univr.track.entity.Shipment;
import it.univr.track.repository.DeviceRepository;
import it.univr.track.repository.ShipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShipmentWebControllerUnitTest {

    private ShipmentWebController controller;
    private ShipmentRepository shipmentRepository;
    private DeviceRepository deviceRepository;

    @BeforeEach
    void setUp() throws Exception {
        shipmentRepository = mock(ShipmentRepository.class);
        deviceRepository = mock(DeviceRepository.class);

        controller = new ShipmentWebController();

        // Inject private fields using reflection
        Field shipmentField = ShipmentWebController.class.getDeclaredField("shipmentRepository");
        shipmentField.setAccessible(true);
        shipmentField.set(controller, shipmentRepository);

        Field deviceField = ShipmentWebController.class.getDeclaredField("deviceRepository");
        deviceField.setAccessible(true);
        deviceField.set(controller, deviceRepository);
    }

    @Test
    void testNewShipment() {
        String viewName = controller.newShipment();
        assertEquals("newShipment", viewName);
    }

    @Test
    void testShipments() {
        List<Shipment> shipments = new ArrayList<>();
        shipments.add(new Shipment());

        when(shipmentRepository.findAll()).thenReturn(shipments);

        Model model = new BindingAwareModelMap();
        String view = controller.shipments(model);

        assertEquals("shipments", view);
        assertTrue(model.containsAttribute("shipments"));
        assertEquals(shipments, model.getAttribute("shipments"));

        verify(shipmentRepository, times(1)).findAll();
    }

    @Test
    void testTrackingExistingShipment() {
        Shipment shipment = new Shipment();
        when(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipment));

        Model model = new BindingAwareModelMap();
        String view = controller.tracking(1L, model);

        assertEquals("tracking", view);
        assertEquals(shipment, model.getAttribute("shipment"));
    }

    @Test
    void testTrackingNonExistingShipment() {
        when(shipmentRepository.findById(1L)).thenReturn(Optional.empty());

        Model model = new BindingAwareModelMap();
        String view = controller.tracking(1L, model);

        assertEquals("shipments", view);
    }

    @Test
    void testCreateShipment() {
        String redirect = controller.createShipment();

        assertEquals("redirect:/web/shipments", redirect);
        verify(shipmentRepository, times(1)).save(any(Shipment.class));
    }

    @Test
    void testAllocateAllDevices() throws Exception {
        Device device = new Device();
        device.setShipment(null);

        Shipment shipment = new Shipment();
        shipment.setDevices(new ArrayList<>());

        Field deviceIdField = Device.class.getSuperclass().getDeclaredField("id");
        deviceIdField.setAccessible(true);
        deviceIdField.set(device, 1L);

        Field shipmentIdField = Shipment.class.getSuperclass().getDeclaredField("id");
        shipmentIdField.setAccessible(true);
        shipmentIdField.set(shipment, 1L);

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));
        when(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipment));

        String redirect = controller.allocateAllDevices(List.of(1L), List.of(1L));

        assertEquals("redirect:/web/shipments", redirect);
        assertEquals(shipment, device.getShipment());
        assertTrue(shipment.getDevices().contains(device));

        verify(deviceRepository, times(1)).save(device);
        verify(shipmentRepository, times(1)).save(shipment);
    }

    @Test
    void testToggleTracking() {
        Shipment shipment = new Shipment();
        shipment.setTracked(false);

        when(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipment));

        String redirect = controller.toggleTracking(1L);

        assertEquals("redirect:/web/shipments", redirect);
        assertTrue(shipment.isTracked());
        verify(shipmentRepository, times(1)).save(shipment);
    }

    @Test
    void testReadShipmentExists() throws Exception {
        Shipment shipment = new Shipment();
        // set ID
        Field idField = Shipment.class.getSuperclass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(shipment, 1L);

        when(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipment));

        Model model = new BindingAwareModelMap();
        String view = controller.readShipment(1L, model);

        assertEquals("shipment", view);
        assertEquals(shipment, model.getAttribute("shipment"));
    }

    @Test
    void testReadShipmentNotExists() {
        when(shipmentRepository.findById(1L)).thenReturn(Optional.empty());

        Model model = new BindingAwareModelMap();
        String view = controller.readShipment(1L, model);

        assertEquals("shipments", view);
    }

    @Test
    void testDeleteShipmentExists() throws Exception {
        Shipment shipment = new Shipment();
        shipment.setDevices(new ArrayList<>());

        Device device1 = new Device();
        Device device2 = new Device();

        Field deviceIdField = Device.class.getSuperclass().getDeclaredField("id");
        deviceIdField.setAccessible(true);
        deviceIdField.set(device1, 1L);
        deviceIdField.set(device2, 2L);

        shipment.getDevices().add(device1);
        shipment.getDevices().add(device2);


        device1.setShipment(shipment);
        device2.setShipment(shipment);

        when(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipment));

        Model model = new BindingAwareModelMap();
        String view = controller.deleteShipment(1L, model);

        assertEquals("shipments", view);

        assertNull(device1.getShipment());
        assertNull(device2.getShipment());

        verify(deviceRepository, times(1)).save(device1);
        verify(deviceRepository, times(1)).save(device2);
        verify(shipmentRepository, times(1)).delete(shipment);
    }

    @Test
    void testDeleteShipmentNotExists() {
        when(shipmentRepository.findById(1L)).thenReturn(Optional.empty());

        Model model = new BindingAwareModelMap();
        String view = controller.deleteShipment(1L, model);

        assertEquals("shipments", view);

        verify(shipmentRepository, never()).delete(any());
        verify(deviceRepository, never()).save(any());
    }
}
