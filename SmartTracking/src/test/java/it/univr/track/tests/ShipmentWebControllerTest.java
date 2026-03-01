package it.univr.track.tests;

import it.univr.track.tests.pageObjects.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ShipmentWebControllerTest extends BaseTest {

    @Test
    @DirtiesContext
    public void createShipmentTest() {
        driver.get("http://localhost:8080");

        SignInPage signInPage = new SignInPage(driver);
        ProfilePage profilePage = signInPage.clickLoginButton();
        ShipmentsPage shipmentsPage = profilePage.goToShipmentsPage();
        NewShipmentPage newShipmentPage = shipmentsPage.goToNewShipmentPage();
        shipmentsPage = newShipmentPage.createShipment();

        assertEquals(1,shipmentsPage.getShipmentCount());
    }

    @Test
    @DirtiesContext
    public void toggleShipmentTrackingTest() {
        driver.get("http://localhost:8080");
        SignInPage signInPage = new SignInPage(driver);
        ProfilePage profilePage = signInPage.clickLoginButton();
        ShipmentsPage shipmentsPage = profilePage.goToShipmentsPage();
        NewShipmentPage newShipmentPage = shipmentsPage.goToNewShipmentPage();
        shipmentsPage = newShipmentPage.createShipment();
        TrackingPage trackingPage = shipmentsPage.goToTrackingPageById(1);
        shipmentsPage = trackingPage.toggleShipmentTracking();
        assertTrue(shipmentsPage.getTrackingStatus(1));
        trackingPage = shipmentsPage.goToTrackingPageById(1);
        shipmentsPage = trackingPage.toggleShipmentTracking();
        assertFalse(shipmentsPage.getTrackingStatus(1));
    }

    @Test
    @DirtiesContext
    public void allocateExistingDeviceTest() {
        driver.get("http://localhost:8080");
        SignInPage signInPage = new SignInPage(driver);
        ProfilePage profilePage = signInPage.clickLoginButton();
        DevicePage devicePage = profilePage.goToDevicesPage();
        ProvisionDevicePage provisionDevicePage = devicePage.provisionDevice();
        devicePage = provisionDevicePage.provisionDevice();
        profilePage = devicePage.goToProfilePage();
        ShipmentsPage shipmentsPage = profilePage.goToShipmentsPage();
        NewShipmentPage newShipmentPage = shipmentsPage.goToNewShipmentPage();
        shipmentsPage = newShipmentPage.createShipment();

        Map<Long, List<Long>> allocation = Map.of(
                1L, List.of(1L)
        );

        AllocateDevicePage allocateDevicePage = shipmentsPage.goToAllocateDevicePage();
        shipmentsPage = allocateDevicePage.allocateDevices(allocation);
        List<Long> device_ids = shipmentsPage.getDeviceIdsForShipment(1L);
        assertEquals(device_ids,List.of(1L));


    }

    @Test
    @DirtiesContext
    public void deallocateExistingDeviceTest() {
        driver.get("http://localhost:8080");
        SignInPage signInPage = new SignInPage(driver);
        ProfilePage profilePage = signInPage.clickLoginButton();
        DevicePage devicePage = profilePage.goToDevicesPage();
        ProvisionDevicePage provisionDevicePage = devicePage.provisionDevice();
        devicePage = provisionDevicePage.provisionDevice();
        profilePage = devicePage.goToProfilePage();
        ShipmentsPage shipmentsPage = profilePage.goToShipmentsPage();
        NewShipmentPage newShipmentPage = shipmentsPage.goToNewShipmentPage();
        shipmentsPage = newShipmentPage.createShipment();

        //ShipmentId mapped to list of allocated DeviceIds
        Map<Long, List<Long>> allocation = Map.of(
                1L, List.of(1L)
        );

        AllocateDevicePage allocateDevicePage = shipmentsPage.goToAllocateDevicePage();
        shipmentsPage = allocateDevicePage.allocateDevices(allocation);
        allocateDevicePage = shipmentsPage.goToAllocateDevicePage();
        allocation =  Map.of(
                1L, List.of()
        );
        shipmentsPage = allocateDevicePage.allocateDevices(allocation);
        List<Long> device_ids = shipmentsPage.getDeviceIdsForShipment(1L);
        assertEquals(device_ids,List.of());

    }
    @Test
    @DirtiesContext
    public void readExistingShipmentTest() {
        driver.get("http://localhost:8080");
        SignInPage signInPage = new SignInPage(driver);
        ProfilePage profilePage = signInPage.clickLoginButton();
        ShipmentsPage shipmentsPage = profilePage.goToShipmentsPage();
        NewShipmentPage newShipmentPage = shipmentsPage.goToNewShipmentPage();
        shipmentsPage = newShipmentPage.createShipment();
        ReadShipmentPage readShipmentPage = shipmentsPage.goToShipmentDetails(1L);
        assertEquals(1L, readShipmentPage.getShipmentId());
    }

    @Test
    @DirtiesContext
    public void deleteExistingShipmentTest() {
        driver.get("http://localhost:8080");
        SignInPage signInPage = new SignInPage(driver);
        ProfilePage profilePage = signInPage.clickLoginButton();
        ShipmentsPage shipmentsPage = profilePage.goToShipmentsPage();
        NewShipmentPage newShipmentPage = shipmentsPage.goToNewShipmentPage();
        shipmentsPage = newShipmentPage.createShipment();
        assertEquals(1,shipmentsPage.getShipmentCount());
        shipmentsPage = shipmentsPage.clickDeleteShipment(1L);
        assertEquals(0,shipmentsPage.getShipmentCount());
    }



}
