package it.univr.track.tests;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import it.univr.track.controller.api.ShipmentController;
import it.univr.track.entity.Device;
import it.univr.track.entity.Shipment;
import it.univr.track.repository.DeviceRepository;
import it.univr.track.repository.ShipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

class ShipmentControllerUnitTest {

    private ShipmentRepository shipmentRepository;
    private DeviceRepository deviceRepository;
    private ShipmentController shipmentController;

    @BeforeEach
    void setUp() {
        shipmentRepository = mock(ShipmentRepository.class);
        deviceRepository = mock(DeviceRepository.class);

        shipmentController = new ShipmentController();

        ReflectionTestUtils.setField(shipmentController, "shipmentRepository", shipmentRepository);
        ReflectionTestUtils.setField(shipmentController, "deviceRepository", deviceRepository);
    }

    @Test
    void testReadShipmentExists() throws NoSuchFieldException, IllegalAccessException {
        Shipment shipment = new Shipment();
        java.lang.reflect.Field field = shipment.getClass().getSuperclass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(shipment, 1L);

        when(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipment));

        Shipment result = shipmentController.readShipment(1L);
        assertEquals(1L, result.getId());

        verify(shipmentRepository, times(1)).findById(1L);
    }

    @Test
    void testReadShipmentNotExists() {
        when(shipmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> shipmentController.readShipment(1L));

        verify(shipmentRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateShipment() {
        shipmentController.createShipment();

        verify(shipmentRepository, times(1)).save(any(Shipment.class));
    }

    @Test
    void testEditShipmentExists() throws NoSuchFieldException, IllegalAccessException {
        Shipment existing = new Shipment("OldName");

        when(shipmentRepository.findById(1L)).thenReturn(Optional.of(existing));

        Shipment update = new Shipment("NewName");
        java.lang.reflect.Field field = update.getClass().getSuperclass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(update, 1L);


        boolean result = shipmentController.editShipment(update);

        assertTrue(result);

        verify(shipmentRepository, times(1)).save(any(Shipment.class));
    }

    @Test
    void testEditShipmentNotExists() {
        when(shipmentRepository.findById(1L)).thenReturn(Optional.empty());

        Shipment update = new Shipment("NewName");

        assertThrows(ResponseStatusException.class,
                () -> shipmentController.editShipment(update));

        verify(shipmentRepository, never()).save(any());
    }

    @Test
    void testSetShipmentTrackingExists() {
        Shipment shipment = new Shipment();
        shipment.setTracked(false);

        when(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipment));

        shipmentController.setShipmentTracking(1L, true);

        assertTrue(shipment.isTracked());
        verify(shipmentRepository, times(1)).save(shipment);
    }

    @Test
    void testSetShipmentTrackingNotExists() {
        when(shipmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Expect exception
        assertThrows(ResponseStatusException.class,
                () -> shipmentController.setShipmentTracking(1L, true));

        verify(shipmentRepository, never()).save(any());
    }


    @Test
    void testAllocateDeviceSuccess() {
        Shipment shipment = new Shipment();
        shipment.setDevices(new ArrayList<>());

        Device device = new Device();
        device.setShipment(null);

        when(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipment));
        when(deviceRepository.findById(2L)).thenReturn(Optional.of(device));

        shipmentController.allocateDevice(1L, 2L, false);

        assertEquals(shipment, device.getShipment());
        assertTrue(shipment.getDevices().contains(device));
        verify(deviceRepository, times(1)).save(device);
    }

    @Test
    void testAllocateDeviceAlreadyAllocatedThrowsConflict() {
        Shipment shipment = new Shipment();
        Shipment existingShipment = new Shipment();

        Device device = new Device();
        device.setShipment(existingShipment); // Already allocated

        when(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipment));
        when(deviceRepository.findById(2L)).thenReturn(Optional.of(device));

        assertThrows(ResponseStatusException.class,
                () -> shipmentController.allocateDevice(1L, 2L, false));

        verify(deviceRepository, never()).save(any());
    }


    @Test
    void testDeallocateDeviceSuccess() {
        Shipment shipment = new Shipment();
        shipment.setDevices(new ArrayList<>());

        Device device = new Device();
        device.setShipment(shipment);
        shipment.getDevices().add(device);

        when(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipment));
        when(deviceRepository.findById(2L)).thenReturn(Optional.of(device));

        shipmentController.deallocateDevice(1L, 2L);

        assertNull(device.getShipment());
        assertFalse(shipment.getDevices().contains(device));
        verify(deviceRepository, times(1)).save(device);
    }
}
