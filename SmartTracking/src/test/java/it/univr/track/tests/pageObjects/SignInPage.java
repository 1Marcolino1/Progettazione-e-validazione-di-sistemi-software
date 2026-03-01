package it.univr.track.tests.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SignInPage extends PageObject {

    @FindBy(xpath ="//form[@action='/profile']//input[@type='submit']")
    private WebElement loginButton;

    public SignInPage(WebDriver driver) {
        super(driver);
    }

    public ProfilePage clickLoginButton() {
        loginButton.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/profile"));
        return new ProfilePage(driver);
    }
}
