import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class MyJUnit {
    WebDriver driver;
    WebDriverWait wait;
    @Before
    public void SetupFile(){
        System.setProperty("webdriver.gecko.driver","./src/test/resources/geckodriver.exe");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("--headed");
        driver = new FirefoxDriver(firefoxOptions);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

    }

    @Test
    public void getTitle() {
        driver.get("https://demoqa.com/");
        String title = driver.getTitle();
        Assert.assertTrue(title.contains("ToolsQA"));
    }

    @Test
    public void checkIfElementExists() {
        driver.get("https://demoqa.com/");
        wait = new WebDriverWait(driver,Duration.ofSeconds(40));
        boolean status = wait.until(ExpectedConditions.elementToBeClickable(By.className("banner-image"))).isDisplayed();
        Assert.assertTrue(status);

    }

    @Test
    public void fillUpForm() {
        driver.get("https://demoqa.com/text-box");
        driver.findElement(By.id("userName")).sendKeys("Mr. Rahim");
        driver.findElement(By.id("userEmail")).sendKeys("Rahim@gmail.com");
        driver.findElement(By.id("currentAddress")).sendKeys("Bashundhara R/A");
        driver.findElement(By.id("permanentAddress")).sendKeys("Tangail");
        driver.findElement(By.id("submit")).click();
    }

    @Test
    public void clickButton() {
        driver.get("https://demoqa.com/buttons");
        WebElement doubleClickBtnElement = driver.findElement(By.id("doubleClickBtn"));
        WebElement rightClickBtnElement = driver.findElement(By.id("rightClickBtn"));
        Actions actions = new Actions(driver);

        actions.doubleClick(doubleClickBtnElement).perform();
        actions.contextClick(rightClickBtnElement).perform();

        String doubleClickBtnMsg = driver.findElement(By.id("doubleClickMessage")).getText();
        String rightClickBtnMsg = driver.findElement(By.id("rightClickMessage")).getText();

        Assert.assertTrue(doubleClickBtnMsg.contains("You have done a double click"));
        Assert.assertTrue(rightClickBtnMsg.contains("You have done a right click"));

    }

    @Test
    public void clickIfMultipleButton() {
        driver.get("https://demoqa.com/buttons");
        List<WebElement> buttonElement = driver.findElements(By.tagName("button"));
        Actions actions = new Actions(driver);
        actions.doubleClick(buttonElement.get(1)).perform();
        actions.contextClick(buttonElement.get(2)).perform();
        actions.click(buttonElement.get(3)).perform();

    }



}
