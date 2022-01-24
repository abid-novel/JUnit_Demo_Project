import jdk.nashorn.internal.ir.WhileNode;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

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

    @Test
    public void handleChildWindow() {
        driver.get("https://demoqa.com/browser-windows");
        wait = new WebDriverWait(driver,Duration.ofSeconds(40));
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("windowButton"))));
        button.click();

        String mainWindowHandle = driver.getWindowHandle();
        Set<String> allWindowHandle = driver.getWindowHandles();
        Iterator<String> iterator = allWindowHandle.iterator();

        while (iterator.hasNext()) {
            String childWindow = iterator.next();
            if (!mainWindowHandle.equalsIgnoreCase(childWindow)) {
                driver.switchTo().window(childWindow);
                String getText = driver.findElement(By.id("sampleHeading")).getText();
                Assert.assertTrue(getText.contains("This is a sample page"));

            }
        }
    }

    @Test
    public void modalDialog() {
        driver.get("https://demoqa.com/modal-dialogs");
        wait = new WebDriverWait(driver,Duration.ofSeconds(40));
        WebElement element =  wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("showSmallModal"))));
        element.click();
        driver.findElement(By.id("closeSmallModal")).click();

    }

    @Test
    public void webTables() {
        driver.get("https://demoqa.com/webtables");
        driver.findElement(By.xpath("//span[@id='edit-record-1']")).click();
        driver.findElement(By.id("firstName")).clear();
        driver.findElement(By.id("firstName")).sendKeys("Abid");

        driver.findElement(By.id("lastName")).clear();
        driver.findElement(By.id("lastName")).sendKeys("Novel");

        driver.findElement(By.id("userEmail")).clear();
        driver.findElement(By.id("userEmail")).sendKeys("abid@test.com");

        driver.findElement(By.id("age")).clear();
        driver.findElement(By.id("age")).sendKeys("26");

        driver.findElement(By.id("salary")).clear();
        driver.findElement(By.id("salary")).sendKeys("20000");

        driver.findElement(By.id("department")).clear();
        driver.findElement(By.id("department")).sendKeys("SQA");

        driver.findElement(By.id("submit")).click();

    }

    @Test
    public void scrapDataFromWebTables() {
        driver.get("https://demoqa.com/webtables");
        WebElement table = driver.findElement(By.className("rt-table"));
        List <WebElement> allRows = driver.findElements(By.className("rt-tr-group"));
        int i = 0;

        for (WebElement row : allRows) {
            List <WebElement> allColumns = driver.findElements(By.className("rt-td"));
            for (WebElement column : allColumns) {
                i++;
                System.out.println("num["+ i + "] = " +column.getText() );
            }
        }
    }

    @Test
    public void uploadImage() {
        driver.get("https://demoqa.com/upload-download");
        wait = new WebDriverWait(driver,Duration.ofSeconds(60));
        WebElement uploadButton = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("uploadFile"))));
        uploadButton.sendKeys("C:\\Users\\Novel\\Desktop\\upload.jpg");

        String text = driver.findElement(By.id("uploadedFilePath")).getText();
        Assert.assertTrue(text.contains("upload.jpg"));
    }

    @Test
    public void handleIframe() {
        driver.get("https://demoqa.com/frames");
        driver.switchTo().frame("frame2");
        String iframeText = driver.findElement(By.id("sampleHeading")).getText();
        Assert.assertTrue(iframeText.contains("This is a sample page"));
        driver.switchTo().defaultContent();
    }

    @Test
    public void mouseHover() throws InterruptedException {
        driver.get("http://www.iub.edu.bd/");
        WebElement aboutElement = driver.findElement(By.xpath("//body/div[4]/div[1]/div[1]/ul[1]/li[1]/a[1]"));
        Actions actions = new Actions(driver);
        actions.moveToElement(aboutElement).perform();
        Thread.sleep(5000);

    }

    @Test
    public void keyBoardEvent() throws InterruptedException {
        driver.get("https://www.google.com/");
        WebElement searchBar = driver.findElement(By.name("q"));
        Actions actions = new Actions(driver);
        actions.moveToElement(searchBar)
                .keyDown(Keys.SHIFT)
                .sendKeys("Selenium Webdriver")
                .keyUp(Keys.SHIFT)
                .doubleClick()
                .contextClick()
                .perform();

        Thread.sleep(5000);
    }

    @Test
    public void takeScreenShot() throws IOException {
        driver.get("https://demoqa.com/");
        File screenShotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String time = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss-aa").format(new Date());
        String fileWithPath = "./src/test/resources/screenshots/" + time + ".png";
        File destinationFile = new File(fileWithPath);
        FileUtils.copyFile(screenShotFile,destinationFile);

    }

    public static void readFromExcel(String filePath, String fileName, String sheetName ) throws IOException {
        File file = new File(filePath + "\\" +fileName);
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = null;
        String fileExtensionName = fileName.substring(fileName.indexOf("."));

        if (fileExtensionName.equals(".xls")) {
            workbook = new HSSFWorkbook(inputStream);
        }

        Sheet sheet = workbook.getSheet(sheetName);
        int rowCount = sheet.getFirstRowNum() - sheet.getLastRowNum();
        for (int i = 0; i < rowCount+1; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < row.getLastCellNum(); j++) {
                System.out.print((row.getCell(j).getStringCellValue()) + "||");
            }
            System.out.println();
        }
    }

    @Test
    public void readDataFromExcel() throws IOException {
        readFromExcel("D:\\Document\\Road to SDET All\\", "Name.xls", "Sheet1");
    }

    @After
    public void closeBrowser() {
        driver.close();
        //driver.quit();
    }

}
