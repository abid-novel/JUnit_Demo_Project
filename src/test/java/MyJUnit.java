import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
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

    @Test
    public void handleAlerts() throws InterruptedException {
        driver.get("https://demoqa.com/alerts");

        //alert button 1
        driver.findElement(By.id("alertButton")).click();
        driver.switchTo().alert().accept();
        Thread.sleep(1000);

        //alert button 3
        driver.findElement(By.id("confirmButton")).click();
        driver.switchTo().alert().dismiss();
        Thread.sleep(1000);

        //alert button 4
        driver.findElement(By.id("promtButton")).click();
        driver.switchTo().alert().sendKeys("Novel");
        Thread.sleep(2000);
        driver.switchTo().alert().accept();

        String alertResult = driver.findElement(By.id("promptResult")).getText();
        Assert.assertTrue(alertResult.contains("Novel"));

    }

    @Test
    public void selectDate() {
        driver.get("https://demoqa.com/date-picker");
        driver.findElement(By.id("datePickerMonthYearInput")).clear();
        driver.findElement(By.id("datePickerMonthYearInput")).sendKeys("01/01/2022");
        driver.findElement(By.id("datePickerMonthYearInput")).sendKeys(Keys.ENTER);

    }

    @Test
    public void selectDropDown() {
        driver.get("https://demoqa.com/select-menu");

        //Single selection
        Select select1 = new Select(driver.findElement(By.id("oldSelectMenu")));
        select1.selectByValue("4");

        //Multiple selection
        Select select2 = new Select(driver.findElement(By.id("cars")));
        if (select2.isMultiple()) {
            select2.selectByValue("saab");
            select2.selectByValue("audi");
        }
    }

    @Test
    public void handleNewTab() throws InterruptedException {
        driver.get("https://demoqa.com/links");
        driver.findElement(By.id("simpleLink")).click();
        ArrayList <String> w = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(w.get(1));


        wait = new WebDriverWait(driver,Duration.ofSeconds(50));
        boolean status = wait.until(ExpectedConditions.elementToBeClickable(By.className("banner-image"))).isDisplayed();
        Assert.assertEquals(true,status);

        driver.close();
        driver.switchTo().window(w.get(0));

    }





}
