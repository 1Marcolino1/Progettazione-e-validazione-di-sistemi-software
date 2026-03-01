package it.univr.track.tests.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProvisionDevicePage extends PageObject {

    public ProvisionDevicePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath ="//form[@action='/web/createDevice']//button[@type='submit']")
    private WebElement provisionDeviceButton;

    public DevicePage provisionDevice() {
        provisionDeviceButton.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/web/provision']")));
        return new DevicePage(driver);
    }

}