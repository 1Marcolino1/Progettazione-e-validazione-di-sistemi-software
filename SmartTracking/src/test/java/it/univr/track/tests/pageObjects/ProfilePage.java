package it.univr.track.tests.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProfilePage extends PageObject {


    private final WebElement shipmentLink = driver.findElement(By.linkText("Your shipments"));;
    private final WebElement deviceLink = driver.findElement(By.linkText("Your devices"));;

    public ProfilePage(WebDriver driver) {
        super(driver);
    }

    public ShipmentsPage goToShipmentsPage() {
        shipmentLink.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[text()='Shipment list']")
        ));

        return new ShipmentsPage(driver);
    }

    public DevicePage goToDevicesPage() {
        deviceLink.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[@href='/web/provision']")
        ));

        return new DevicePage(driver);
    }
}