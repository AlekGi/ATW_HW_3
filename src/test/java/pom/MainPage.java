package pom;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;
import pom.elements.StudentTableRow;


import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {


    @FindBy(css = "nav li.mdc-menu-surface--anchor a")
    private SelenideElement usernameLinkInNavBar;
    @FindBy(css = "nav li.mdc-menu-surface--anchor div li")
    private SelenideElement profileLinkInNavBar;

    private final SelenideElement createButton = $("#create-btn");

    private final SelenideElement submitButton = $x("//form//span[contains(text(), 'Save')]");

    private final SelenideElement firstName = $x("//form//span[contains(text(), 'Fist Name')]/following-sibling::input");

    private final SelenideElement login = $x("//form//span[contains(text(), 'Login')]/following-sibling::input");

    private final ElementsCollection rowsInStudentTable = $$x("//table[@aria-label='Dummies list']/tbody/tr");

    public SelenideElement waitAndGetStudentTitleByText(String title) {
        return $x(String.format("//table[@aria-label='Dummies list']/tbody//td[text()='%s']", title)).shouldBe(visible);
    }

    public void createStudent(String firstNameDummie, String loginDummie) {
        createButton.shouldBe(visible).click();
        firstName.shouldBe(visible).sendKeys(firstNameDummie);
        login.shouldBe(visible).sendKeys(loginDummie);
        submitButton.shouldBe(visible).click();
    }

    public String getUsernameLabelText() {
        return usernameLinkInNavBar.shouldBe(visible).getText().replace("\n", " ");
    }

    public StudentTableRow getStudentRowByName(String Name) {
        return rowsInStudentTable.shouldHave(sizeGreaterThan(0))
                .stream()
                .map(StudentTableRow::new)
                .filter(row -> row.getName().equals(Name))
                .findFirst().orElseThrow();
    }

    public String getFirstGeneratedStudentName() {
        return rowsInStudentTable.shouldHave(sizeGreaterThan(0))
                .stream()
                .map(StudentTableRow::new)
                .findFirst().orElseThrow().getName();
    }

    public void clickTrashIconOnStudentWithName(String name) {
        getStudentRowByName(name).clickTrashIcon();
    }

    public void clickRestoreFromTrashIconOnStudentWithName(String name) {
        getStudentRowByName(name).clickRestoreFromTrashIcon();
    }

    public String getStatusOfStudentWithName(String name) {
        return getStudentRowByName(name).getStatus();
    }

    public void clickUsernameLabel(){
        usernameLinkInNavBar.shouldBe(visible).click();}

    public void clickProfileLabel() {
        profileLinkInNavBar.shouldBe(visible).click();}

}
