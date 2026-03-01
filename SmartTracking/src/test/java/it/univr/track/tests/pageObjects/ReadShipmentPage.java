package it.univr.track.tests.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ReadShipmentPage extends PageObject {
    public ReadShipmentPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//a[text()=' Show the list ']")
    private WebElement listLink;

    public ShipmentsPage goBackToList() {
        listLink.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[text()='Shipment list']")
        ));
        return new ShipmentsPage(driver);
    }

    public Long getShipmentId() {
        String idText = driver.findElement(By.xpath("//tr[th[text()='ID']]/td")).getText();;
        return Long.parseLong(idText);
    }

}
