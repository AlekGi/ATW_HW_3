package pom;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.codeborne.selenide.Condition.visible;

public class LoginPage {

    @FindBy(css="form#login input[type='text']")
    private SelenideElement usernameField;
    @FindBy(css="form#login input[type='password']")
    private SelenideElement passwordField;
    @FindBy(css="form#login button")
    private SelenideElement loginButton;
    @FindBy(css = "div.error-block")
    private SelenideElement errorBlock;


    public void login(String geek_login, String geek_password) {
        typeUsernameInField(geek_login);
        typePasswordInField(geek_password);
        clickLoginButton();
    }

    public void typeUsernameInField(String username) {
        usernameField.shouldBe(visible).sendKeys(username);
    }

    public void typePasswordInField(String password) {
        passwordField.shouldBe(visible).sendKeys(password);
    }

    public void clickLoginButton() {
        loginButton.shouldBe(visible).click();
    }

    public String getErrorBlockText() {
        return errorBlock.shouldBe(visible)
                .getText().replace("\n", " ");
    }

}