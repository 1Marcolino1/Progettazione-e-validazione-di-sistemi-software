package it.univr.track.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeEach
    public void setUp() {
        WebDriverManager manager = WebDriverManager.firefoxdriver();
        if (driver == null)
            driver = manager.create();
    }

    @AfterEach
    public void tearDown() {
        // Quit the driver to clean up resources
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
