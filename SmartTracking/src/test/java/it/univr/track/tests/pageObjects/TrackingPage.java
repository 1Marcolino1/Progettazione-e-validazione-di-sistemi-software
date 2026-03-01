package it.univr.track.tests.pageObjects;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TrackingPage extends PageObject{
    public TrackingPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath ="//form[@action='/toggleTracking']//button[@type='submit']")
    WebElement toggleTrackingButton;

    public ShipmentsPage toggleShipmentTracking()
    {
        toggleTrackingButton.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[text()='Shipment list']")
        ));
        return new ShipmentsPage(driver);
    }
}
