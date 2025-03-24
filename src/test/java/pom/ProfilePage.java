package pom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Condition.visible;

public class ProfilePage {

    @FindBy(xpath = "//div[contains(text(), 'Full name')]/following-sibling::div")
    public SelenideElement fullNameInAdditionalInfo;
    @FindBy(css = "div.mdc-card h2")
    public SelenideElement fullNameInAvatarSection;

    public String getFullNameFromAdditionalInfo(){
        return fullNameInAdditionalInfo.shouldBe(visible).text();
    }

    public String getFullNameFromAvatarSection(){
        return fullNameInAvatarSection.shouldBe(visible).text();
    }
}
