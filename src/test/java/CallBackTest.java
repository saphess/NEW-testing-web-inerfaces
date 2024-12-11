import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CallBackTest {
    private WebDriver driver;

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");

        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSendForm() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[name='name']")).sendKeys("Иван Лазарь");
        form.findElement(By.cssSelector("[name='phone']")).sendKeys("+79501112332");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();

        form.findElement(By.cssSelector("button")).click();

        String actualText = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().strip();
        String expectText = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";

        assertEquals(expectText, actualText);
    }

    @Test
    void testValidationName() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[name='name']")).sendKeys("Ivan Rudskoi");
        form.findElement(By.cssSelector("[name='phone']")).sendKeys("+79501112332");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();

        form.findElement(By.cssSelector("button")).click();

        String actualText = form.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"))
                .getText().strip();
        String expectText = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";

        assertEquals(expectText, actualText);
    }

    @Test
    void testValidationPhone() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[name='name']")).sendKeys("Иван Лазарь");
        form.findElement(By.cssSelector("[name='phone']")).sendKeys("79501112332");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();

        form.findElement(By.cssSelector("button")).click();

        String actualText = form.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"))
                .getText().strip();
        String expectText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        assertEquals(expectText, actualText);
    }

    @Test
    void testValidationAgreement() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[name='name']")).sendKeys("Иван Лазарь");
        form.findElement(By.cssSelector("[name='phone']")).sendKeys("+79501112332");

        form.findElement(By.cssSelector("button")).click();

        boolean actualIsDisplayed = form.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid"))
                .isDisplayed();
        boolean expectIsDisplayed = true;

        assertEquals(expectIsDisplayed, actualIsDisplayed);
    }

    @Test
    void testValidationNameNull() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[name='phone']")).sendKeys("+79501112332");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();

        form.findElement(By.cssSelector("button")).click();

        String actualText = form.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"))
                .getText().strip();
        String expectText = "Поле обязательно для заполнения";

        assertEquals(expectText, actualText);
    }

    @Test
    void testValidationPhoneNull() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[name='name']")).sendKeys("Иван Лазарь");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();

        form.findElement(By.cssSelector("button")).click();

        String actualText = form.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"))
                .getText().strip();
        String expectText = "Поле обязательно для заполнения";

        assertEquals(expectText, actualText);
    }
}
