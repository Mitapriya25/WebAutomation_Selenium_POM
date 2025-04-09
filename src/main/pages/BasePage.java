package main.pages;

import java.util.List;

import main.reporter.ExtentFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.Status;

class BasePage {
	static final int TIMEOUT = 15;
	private static final int POLLING = 100;
	private WebDriverWait wait;
	private WebDriver driver;
	private final Logger log = LogManager.getLogger(BasePage.class);


	/***
	 * Setting WebDriver object
	 * 
	 * @param driver		Object of WebDriver to set
	 */
	public BasePage(WebDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, TIMEOUT, POLLING);
		PageFactory.initElements(new AjaxElementLocatorFactory(driver, TIMEOUT), this);
	}

	/***
	 * Wait for Element to be clickable
	 * 
	 * @param element		Object of WebElement
	 */
	private void waitForElementToBeClickable(WebElement element) {
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	/***
	 * Clicks on Element
	 * 
	 * @param element				Object of WebElement
	 * @param elementNameForLog		Name for logging
	 * 		
	 * @return						True / False
	 */
	protected boolean clickOnElement(WebElement element, String elementNameForLog) {
		boolean status = false;
		String stepName = "Click on "+elementNameForLog+"";
		try {
			waitForElementToBeClickable(element);
			element.click();
			status = true;
			ExtentFactory.getInstance().getExtentTest().log(Status.PASS,stepName);
		} catch (Exception e) {
			ExtentFactory.getInstance().getExtentTest().log(Status.FAIL,stepName);
			log.error("Failed to click on element: " + e);
		}
		return status;
	}




	/***
	 * Clears text box and enters text
	 * 
	 * @param element					Object of WebElement
	 * @param elementNameForLog			Name for Logging
	 * @param textToEnter				Text to enter in text box
	 * 
	 * @return							True / Fale
	 */
	public boolean clearAndEnterText(WebElement element, String elementNameForLog, String textToEnter) {
		String stepName = "Clear and Enter text as "+textToEnter+" in "+elementNameForLog;
		boolean status = false;
		try {
			element.clear();
			element.sendKeys(textToEnter);
			status = true;
			ExtentFactory.getInstance().getExtentTest().log(Status.PASS,stepName);
		} catch (Exception e) {
			ExtentFactory.getInstance().getExtentTest().log(Status.FAIL,stepName);
			log.error("Failed to clear and enter text in element: " + element);
		}
		return status;
	}


	/***
	 * Gets the current url
	 * 
	 * @return		Current URL
	 */
	public String getCurrentURL() {
		return driver.getCurrentUrl();
	}

}