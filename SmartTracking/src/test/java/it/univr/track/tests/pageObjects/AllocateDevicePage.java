package it.univr.track.tests.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class AllocateDevicePage extends PageObject {
    public AllocateDevicePage(WebDriver driver) {
        super(driver);
    }

    private final WebElement allocateDeviceButton = driver.findElement(By.cssSelector("button[type='submit']"));

    public ShipmentsPage allocateDevices(Map<Long, List<Long>> shipmentToDeviceIds) {

        List<WebElement> rows =
                driver.findElements(By.cssSelector("tbody tr"));

        for (WebElement row : rows) {

            WebElement deviceIdInput =
                    row.findElement(By.name("deviceIds"));

            Long deviceId =
                    Long.valueOf(deviceIdInput.getAttribute("value"));

            Long shipmentIdForDevice = null;

            for (Map.Entry<Long, List<Long>> entry : shipmentToDeviceIds.entrySet()) {
                if (entry.getValue().contains(deviceId)) {
                    shipmentIdForDevice = entry.getKey();
                    break;
                }
            }

            // device not part of this test case
            if (shipmentIdForDevice == null) {
                continue;
            }

            WebElement select =
                    row.findElement(By.name("shipmentIds"));

            Select shipmentSelect = new Select(select);
            shipmentSelect.selectByValue(String.valueOf(shipmentIdForDevice));
        }

        allocateDeviceButton.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[text()='Shipment list']")
        ));
        return new ShipmentsPage(driver);
    }

}
