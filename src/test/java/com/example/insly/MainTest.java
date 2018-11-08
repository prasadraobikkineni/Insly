package com.example.insly;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainTest {

    private static WebDriver driver;

    private static String companyName;
    private static String password;

    @BeforeClass
    public static void before() {
         WebDriverManager.chromedriver().setup();
         driver = new ChromeDriver();
         driver.navigate().to("http://signup.insly.com");
        companyName = String.format("MyTestCompanyName%d", new Date().getTime());

    }

    @AfterClass
    public static void after() {
         if (driver != null) {
             driver.quit();
         }
    }

    @Test
    public void test_1 () {
        assertEquals(driver.findElement(By.tagName("h1")).getText(), "Sign up and start using");
    }

    @Test
    public void test_1_2(){
       assertEquals(driver.findElements(By.className("subtitle")).get(3).getText(), "Administrator account details");

       WebElement emailRow = driver.findElement(By.id("field_broker_admin_email"));
       assertNotNull(emailRow);
       assertEquals(emailRow.findElement(By.className("title")).getText(), "WORK E-MAIL");
       assertEquals(emailRow.findElement(By.tagName("input")).getAttribute("name"), "broker_admin_email");

        WebElement nameRow = driver.findElement(By.id("field_broker_admin_name"));
        assertNotNull(nameRow);
        assertEquals(nameRow.findElement(By.className("title")).getText(), "ACCOUNT MANAGER NAME");
        assertEquals(nameRow.findElement(By.tagName("input")).getAttribute("name"), "broker_admin_name");


        WebElement passwordRow = driver.findElement(By.id("field_broker_person_password"));
        assertNotNull(passwordRow);
        assertEquals(passwordRow.findElement(By.className("label")).getText(), "PASSWORD");
        assertEquals(passwordRow.findElement(By.tagName("input")).getAttribute("name"), "broker_person_password");
        assertEquals(passwordRow.findElement(By.tagName("a")).getText(), "suggest a secure password");

        WebElement phoneRow = driver.findElement(By.id("field_broker_admin_phone"));
        assertNotNull(phoneRow);
        assertEquals(phoneRow.findElement(By.className("label")).getText(), "PHONE");
        assertEquals(phoneRow.findElement(By.tagName("input")).getAttribute("name"), "broker_admin_phone");


       assertEquals(driver.findElements(By.className("subtitle")).get(4).getText(), "Terms and conditions");

        WebElement termsRow = driver.findElement(By.id("field_terms"));
        assertNotNull(termsRow);
        assertEquals(termsRow.findElement(By.className("title")).getText(), "TERMS AND CONDITIONS");
        List<WebElement> checkboxs = termsRow.findElement(By.className("checklist")).findElements(By.xpath("label/input"));

        assertEquals(checkboxs.size(), 3);
        assertEquals(checkboxs.get(0).getAttribute("id"), "agree_termsandconditions");
        assertEquals(checkboxs.get(1).getAttribute("id"), "agree_privacypolicy");
        assertEquals(checkboxs.get(2).getAttribute("id"), "agree_data_processing");

        assertEquals(driver.findElements(By.className("alert-warning")).get(1).getText(), "You must read Privacy Policy before proceeding.");
    }

    @Test
    public void test_2() {
        WebElement name = driver.findElement(By.id("broker_name"));
        name.sendKeys(companyName);

        WebElement country = driver.findElement(By.id("broker_address_country"));
        country.sendKeys("Costa Rica");

        WebElement tag = driver.findElement(By.id("broker_tag"));
        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(ExpectedConditions.attributeContains(tag, "value", companyName.toLowerCase()));
        assertEquals(tag.getAttribute("value"), companyName.toLowerCase());

        WebElement profile = driver.findElement(By.id("prop_company_profile"));
        profile.sendKeys("Other");

        WebElement employee = driver.findElement(By.id("prop_company_no_employees"));
        employee.sendKeys("101-500");

        WebElement person = driver.findElement(By.id("prop_company_person_description"));
        person.sendKeys("I am a tech guy");

        assertEquals(driver.findElement(By.id("broker_name")).getAttribute("value"), companyName);
        assertEquals(driver.findElement(By.id("broker_address_country")).getAttribute("value"), "CR");
        assertEquals(driver.findElement(By.id("prop_company_profile")).getAttribute("value"), "O");
        assertEquals(driver.findElement(By.id("prop_company_no_employees")).getAttribute("value"), "30");
        assertEquals(driver.findElement(By.id("prop_company_person_description")).getAttribute("value"), "tech");
    }

    @Test
    public void test_3 () {
        WebElement email = driver.findElement(By.id("broker_admin_email"));
        email.sendKeys(String.format("info@%s.com", companyName));

        WebElement name = driver.findElement(By.id("broker_admin_name"));
        name.sendKeys("Admin Name");

        WebElement passwordField = driver.findElement(By.id("broker_person_password"));
        passwordField.sendKeys("123456");

        WebElement repeatPassword = driver.findElement(By.id("broker_person_password_repeat"));
        repeatPassword.sendKeys("123456");

        WebElement phone = driver.findElement(By.id("broker_admin_phone"));
        phone.sendKeys("555-555-55");


        driver.findElement(By.id("field_broker_person_password")).findElement(By.tagName("a")).click();
        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("ui-dialog"))));
        password = driver.findElement(By.cssSelector("#insly_alert b")).getText();
        driver.findElement(By.className("ui-dialog")).findElement(By.className("primary")).click();
    }

    @Test
    public void test_4 () {
        WebElement checklist = driver.findElement(By.id("field_terms"))
                .findElement(By.className("checklist"));
        List<WebElement> checkboxs = checklist
                .findElements(By.xpath("label/span"));
        checkboxs.get(0).click();
        checkboxs.get(1).click();
        checkboxs.get(2).click();


        List<WebElement> labels = checklist.findElements(By.xpath("label"));
        labels.get(0).findElement(By.tagName("a")).click();
        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(ExpectedConditions.textToBe(
                By.xpath("//div[contains(@class, 'ui-dialog') and contains(@style, 'display: block')]/div/span[contains(@class, 'ui-dialog-title')]"),
                "Terms and conditions"
        ));
        driver.findElement(By.cssSelector(".privacy-policy-dialog + .ui-dialog-buttonpane")).findElement(By.className("primary")).click();

        labels.get(1).findElement(By.tagName("a")).click();
        WebDriverWait wait2 = new WebDriverWait(driver, 15);
        wait2.until(ExpectedConditions.textToBe(
                        By.xpath("//div[contains(@class, 'ui-dialog') and contains(@style, 'display: block')]/div/span[contains(@class, 'ui-dialog-title')]"),
                 "Privacy Policy"
        ));

        driver.findElement(By.cssSelector(".privacy-policy-dialog.ui-dialog-content.ui-widget-content")).click(); // there is no such element. What youu need at this line?
        Actions action = new Actions(driver);
        action.moveToElement(driver.findElement(By.cssSelector("#document-content + div")));
        action.perform();

        driver.findElement(By.xpath("//div[contains(@class, 'ui-dialog') and contains(@style, 'display: block')]/div/a[contains(@class, 'ui-dialog-titlebar-close')]")).click();
        WebElement saveButton = driver.findElement(By.id("submit_save"));
        assertNotNull(saveButton);
        wait.until(ExpectedConditions.not(ExpectedConditions.attributeContains(saveButton,"disabled", "disabled")));
    }

    @Test
    public void test_5 () {
        WebElement saveButton = driver.findElement(By.id("submit_save"));
        saveButton.click();
        WebDriverWait wait = new WebDriverWait(driver, 120);
        wait.until(ExpectedConditions.titleIs(String.format("%s · Insly", companyName)));
        assertEquals(driver.getCurrentUrl(), String.format("https://%s.insly.com/dashboard", companyName.toLowerCase()));
        assertEquals(driver.findElement(By.cssSelector("#user-info strong")).getText(), "Admin Name");
        assertEquals(driver.findElement(By.cssSelector("#user-info small")).getText(), companyName);
    }
}
