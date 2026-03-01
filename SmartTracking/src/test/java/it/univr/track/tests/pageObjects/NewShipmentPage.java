package it.univr.track.tests.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class NewShipmentPage extends PageObject {
    public NewShipmentPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath ="//form[@action='/web/createShipment']//button[@type='submit']")
    private WebElement createShipmentButton;

    public ShipmentsPage createShipment()
    {
        createShipmentButton.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table")
        ));
        return new ShipmentsPage(driver);
    }
}
