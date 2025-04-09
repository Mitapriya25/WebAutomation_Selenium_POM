package test;

import main.pages.TipTopPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TipTopAssignmentTest extends BaseTest {
    String expectedUrl="https://d3pv22lioo8876.cloudfront.net/tiptop/submitted.html?my-name=Mitapriya&my-password=Mitapriya123&my-readonly=Readonly+input&my-select=white";
    @BeforeMethod
    public void init() {
        tipTopPage= new TipTopPage(driver);
    }

    @Test(description = "Verify that the text input element with xpath .//input[@name='my-disabled'] is disabled in the form")
    public void TC001_VerifyInputNameFieldDisabled() {
        Assert.assertFalse(tipTopPage.getInputDisableField().isEnabled(),"input field is not disabled");
    }

    @Test(description = "Verify that the text input with value 'Readonly input' is in readonly state by using 2 xpaths")
    public void TC002_VerifyInputNameFieldInReadonlyMode() {
        Assert.assertTrue(tipTopPage.getReadOnlyInputField().isEnabled(),"input field is not disabled");
    }

    @Test(description = "Verify that the dropdown field to select color is having 8 elements using 2 xpaths")
    public void TC003_VerifyAllColoursInDropdown() {
        Assert.assertTrue(tipTopPage.clickOnColourDropdown(),"input field is not disabled");
        String[] expected={"White","Violet","Indigo","Blue","Green","Yellow","Orange","Red"};
        String[] allOptions = TipTopPage.getAllDropdownOptions(tipTopPage.getDropDownColour()).toArray(new String[0]);
        Assert.assertEquals(allOptions, expected, "Dropdown options don't match!");

    }

    @Test(description = "Verify that the text input with value 'Readonly input' is in readonly state by using 2 xpaths")
    public void TC004_VerifySubmitButtonDisabledBeforeAddingRequiredDetails() {
        Assert.assertFalse(tipTopPage.getSubmitButton().isEnabled(),"input field is not disabled");
    }

    @Test(description = "Verify that the text input with value 'Readonly input' is in readonly state by using 2 xpaths")
    public void TC005_VerifySubmitButtonEnabledAfterAddingRequiredDetails() {
        tipTopPage.clearAndEnterText(tipTopPage.getUserName(),"User Name","Mitapriya");
        tipTopPage.clearAndEnterText(tipTopPage.getUserPassword(),"Password","Mitapriya@123");
        Assert.assertTrue(tipTopPage.getSubmitButton().isEnabled(),"input field is not disabled");
    }

    @Test(description = "Verify that the text input with value 'Readonly input' is in readonly state by using 2 xpaths")
    public void TC006_VerifyReceivedTextAfterSubmittingFrom() {
        tipTopPage.clearAndEnterText(tipTopPage.getUserName(),"User Name","Mitapriya");
        tipTopPage.clearAndEnterText(tipTopPage.getUserPassword(),"Password","Mitapriya123");
        Assert.assertTrue(tipTopPage.getSubmitButton().isEnabled(),"input field is not disabled");
        tipTopPage.clickOnSubmitButton();
        Assert.assertEquals(tipTopPage.getFormSubmitText(),"Form submitted","Form submitted text does not match");
        Assert.assertEquals(tipTopPage.getReceivedText(),"Received!","Received Text does not match");;
    }

    @Test(description = "Verify that the text input with value 'Readonly input' is in readonly state by using 2 xpaths")
    public void TC007_VerifyURLAfterSubmittingForm() {
        tipTopPage.clearAndEnterText(tipTopPage.getUserName(),"User Name","Mitapriya");
        tipTopPage.clearAndEnterText(tipTopPage.getUserPassword(),"Password","Mitapriya123");
        Assert.assertTrue(tipTopPage.getSubmitButton().isEnabled(),"input field is not disabled");
        tipTopPage.clickOnSubmitButton();
        String actualUrl = tipTopPage.getCurrentURL();
        Assert.assertEquals(actualUrl, expectedUrl,"expected and actual does not match");
    }

}
