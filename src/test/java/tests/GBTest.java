package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import pom.LoginPage;
import pom.MainPage;
import org.openqa.selenium.support.ui.WebDriverWait;
import pom.ProfilePage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Пример использования самых базовых методов библиотеки Selenium.
 */
public class GBTest {

    private LoginPage loginPage;
    private MainPage mainPage;
    private ProfilePage profilePage;

    private static String USERNAME;
    private static String PASSWORD;

    private static String firstNameDummie = "Gil"+ System.currentTimeMillis();
    private static String loginDummie = "Al"+ System.currentTimeMillis();



    @BeforeAll
    public static void setupClass(){
        // mvn clean test -Dgeek_login=USER -Dgeek_password=PASS
        USERNAME = System.getProperty("geek_login", System.getenv("geek_login"));
        PASSWORD = System.getProperty("geek_password", System.getenv("geek_password"));

    }

    @BeforeEach
    public void setupTest() throws MalformedURLException{
        // Создаём экземпляр драйвера
        Selenide.open("https://test-stand.gb.ru/login");
        // Объект созданного Page Object
        loginPage = Selenide.page(LoginPage.class);
    }

    @Test
    @DisplayName("Добавление нового болванчика")
    public void testAddingDummieOnMainPage() throws IOException {
        // Логин в систему с помощью метода из класса Page Object
        loginPage.login(USERNAME, PASSWORD);
        // Инициализация объекта класса MainPage
        mainPage = Selenide.page(MainPage.class);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        // Создание болванчика. Даём уникальное имя, чтобы в каждом запуске была проверка нового имени
        firstNameDummie = "Gil"+ System.currentTimeMillis();
        loginDummie = "Al"+ System.currentTimeMillis();
        mainPage.createStudent(firstNameDummie, loginDummie);

        // Проверка, что болванчик добавлен и находится в таблице
        assertTrue(mainPage.waitAndGetStudentTitleByText(firstNameDummie).isDisplayed());
        // Делаем скриншот браузера
//        byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
//        Files.write(Path.of("src/test/java/resources/addingDummie_" + firstNameDummie + ".png"), screenshotBytes);
    }

    @Test
    @DisplayName("Удаление болванчика, помещение в архив и возврат из него")
    void testArchiveDummieOnMainPage() throws IOException{
        // Обычный логин
        loginPage.login(USERNAME, PASSWORD);
        mainPage = Selenide.page(MainPage.class);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        // Берем первую запись (уже созданную) болванчика за стартовую точку проверок
        firstNameDummie = mainPage.getFirstGeneratedStudentName();
        assertEquals("active", mainPage.getStatusOfStudentWithName(firstNameDummie));
        mainPage.clickTrashIconOnStudentWithName(firstNameDummie);
        assertEquals("inactive", mainPage.getStatusOfStudentWithName(firstNameDummie));
//        byte[] screenshotBytes1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
//        Files.write(Path.of("src/test/java/resources/inactive_" + firstNameDummie + ".png"), screenshotBytes1);
        mainPage.clickRestoreFromTrashIconOnStudentWithName(firstNameDummie);
        assertEquals("active", mainPage.getStatusOfStudentWithName(firstNameDummie));
//        byte[] screenshotBytes2 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
//        Files.write(Path.of("src/test/java/resources/restoreActive_" + firstNameDummie + ".png"), screenshotBytes2);
    }

    @Test
    @DisplayName("Логин с пустыми полями")
    public void testLoginEmptyFields() throws IOException{
        // Клик на кнопку LOGIN, данные не вводим
        loginPage.clickLoginButton();
        Selenide.sleep(3000);
        // Проверка ожидаемой ошибки
        assertEquals("401 Invalid credentials.", loginPage.getErrorBlockText());
        // Делаем скриншот браузера
//        byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
//        Files.write(Path.of("src/test/java/resources/negativeTest404_" + System.currentTimeMillis() + ".png"), screenshotBytes);
    }

    @Test
    public void testFullNameOnProfilePage() throws IOException, InterruptedException {
        // Логин в систему с помощью метода из класса Page Object
        loginPage.login(USERNAME, PASSWORD);
        // Инициализация объекта класса MainPage
        mainPage = Selenide.page(MainPage.class);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        // Навигация на ProfilePage
        mainPage.clickUsernameLabel();
        mainPage.clickProfileLabel();
        // Инициализация ProfilePage с помощью Selenide
        ProfilePage profilePage = Selenide.page(ProfilePage.class);
        assertEquals("GB202409 b969d8", profilePage.getFullNameFromAdditionalInfo());
        assertEquals("GB202409 b969d8", profilePage.getFullNameFromAvatarSection());


    }

    @AfterEach
    public void teardown(){
        // Закрываем все окна браузера и процесс драйвера
        WebDriverRunner.closeWebDriver();
    }
}