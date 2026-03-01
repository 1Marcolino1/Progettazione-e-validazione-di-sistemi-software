package it.univr.track.tests.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class DevicePage  extends PageObject {
    public DevicePage(WebDriver driver) {
        super(driver);
    }

    private final WebElement provisionDeviceLink = driver.findElement(By.linkText("Provision Device"));;
    private final WebElement profileLink = driver.findElement(By.linkText("Return to profile"));

    public ProvisionDevicePage provisionDevice() {
        provisionDeviceLink.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//form[@action='/web/createDevice']//button[@type='submit']")
        ));

        return new ProvisionDevicePage(driver);
    }

    public ProfilePage goToProfilePage() {
        profileLink.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/profile"));
        return new ProfilePage(driver);
    }
}
