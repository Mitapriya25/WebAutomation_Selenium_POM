package main.pages;

import main.common.GlobalVariables;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

public class TipTopPage extends BasePage {

    private @FindBy(how = How.XPATH, using =".//input[@name='my-disabled']")
    WebElement inputDisableField;

    private @FindBy(how = How.XPATH, using =".//input[@name='my-readonly']")
    WebElement readOnlyInput;

    private @FindBy(how = How.XPATH, using =".//select[@name='my-select']")
    WebElement dropDownColour;

    private @FindBy(how = How.XPATH, using =".//button[@id='submit-form']")
    WebElement submitButton;

    private @FindBy(how = How.XPATH, using =".//input[@id='my-name-id']")
    WebElement userName;

    private @FindBy(how = How.XPATH, using =".//input[@id='my-password-id']")
    WebElement userPassword;

    private @FindBy(how = How.XPATH, using =".//h1[@class='display-6']")
    WebElement formSubmitText;

    private @FindBy(how = How.XPATH, using =".//p[@id='message']")
    WebElement formReceivedText;


    private final static Logger log = LogManager.getLogger(TipTopPage.class);

    /***
     * Setting WebDriver object
     *
     * @param driver		Object of WebDriver to set
     */
    public TipTopPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, TIMEOUT), this);
    }

    public WebElement getInputDisableField() {
        return inputDisableField;
    }

    public WebElement getReadOnlyInputField() {
        return readOnlyInput;
    }
    public WebElement getDropDownColour() {
        return dropDownColour;
    }

    public WebElement getSubmitButton() {
        return submitButton;
    }

    public WebElement getUserName() {
        return userName;
    }

    public WebElement getUserPassword() {
        return userPassword;
    }

    public boolean clickOnColourDropdown() {
        return clickOnElement(dropDownColour, "dropdown colour");
    }



    public static List<String> getAllDropdownOptions(WebElement dropdownElement) {
        Select dropdown = new Select(dropdownElement);
        return dropdown.getOptions().stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }


    public void clickOnSubmitButton() {
        clickOnElement(getSubmitButton(), "Login button");
    }


    public String getFormSubmitText(){
        return formSubmitText.getText().trim();
    }

    public String getReceivedText(){
        return formReceivedText.getText().trim();
    }

}