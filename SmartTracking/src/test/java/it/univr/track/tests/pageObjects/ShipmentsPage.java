package it.univr.track.tests.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ShipmentsPage extends PageObject {
    public ShipmentsPage(WebDriver driver) {
        super(driver);
    }

    private final WebElement addNewShipmentLink = driver.findElement(By.linkText("Add new shipment"));;
    private final WebElement allocateDeviceLink = driver.findElement(By.linkText("Associate a device to a shipment"));


    public NewShipmentPage goToNewShipmentPage()
    {
        addNewShipmentLink.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[text()='Create a new shipment']"))
        );
        return new NewShipmentPage(driver);
    }

    public TrackingPage goToTrackingPageById(long id)
    {
        WebElement trackLink = driver.findElement(
                By.xpath("//a[contains(@href,'/web/tracking') and contains(@href,'id=" + id + "')]")
        );
        trackLink.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Tracking status']"))
        );
        return new TrackingPage(driver);

    }

    public int getShipmentCount() {
        return driver.findElements(By.xpath("//tbody/tr")).size();
    }

    public boolean getTrackingStatus(long shipmentId) {
        WebElement row = driver.findElement(By.xpath("//tbody/tr[td[text()='" + shipmentId + "']]"));
        String statusText = row.findElement(By.xpath("td[2]")).getText();
        return Boolean.parseBoolean(statusText.trim());
    }

    public AllocateDevicePage goToAllocateDevicePage() {
        allocateDeviceLink.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/web/shipmentAllocate"));
        return new AllocateDevicePage(driver);
    }

    public List<Long> getDeviceIdsForShipment(Long shipmentId) {

        WebElement row = driver.findElement(
                By.xpath("//tbody/tr[td[1][normalize-space()='" + shipmentId + "']]")
        );

        WebElement devicesCell = row.findElement(By.xpath("td[3]"));

        List<WebElement> deviceElements =
                devicesCell.findElements(By.tagName("p"));

        List<Long> deviceIds = new ArrayList<>();

        for (WebElement device : deviceElements) {
            deviceIds.add(Long.valueOf(device.getText().trim()));
        }

        return deviceIds;
    }

    public ReadShipmentPage goToShipmentDetails(Long shipmentId) {
        WebElement detailsLink = driver.findElement(By.cssSelector(
                "a[href='/web/read?id=" + shipmentId + "']"
        ));
        detailsLink.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[text()='Shipment Details']")
        ));
        return new ReadShipmentPage(driver);
    }

    public ShipmentsPage clickDeleteShipment(Long shipmentId) {
        WebElement deleteLink = driver.findElement(By.xpath(
                "//tr[td[normalize-space(text())='" + shipmentId + "']]//a[contains(@href,'/web/delete?id=')]"
        ));
        deleteLink.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[text()='Shipment list']")
        ));
        return new ShipmentsPage(driver);
    }
}
