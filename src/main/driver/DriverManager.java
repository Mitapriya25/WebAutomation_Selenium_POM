package main.driver;

import org.openqa.selenium.WebDriver;

public class DriverManager {

	private static DriverManager instance = new DriverManager();
	private ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();


	/***
	 * Get Instance of DriverManager
	 * 
	 * @return		Object of DriverManager
	 */
	public static DriverManager getInstance() {
		return instance;
	}

	/***
	 * Get Driver
	 * 
	 * @return		Objecto of WebDriver
	 */	
	public WebDriver getDriver() {
		return webDriver.get();
	}

	/***
	 * Sets WebDriver
	 * 
	 * @param	driver		Driver Object to set
	 */
	public void setWebDriver(WebDriver driver) {
		webDriver.set(driver);
	}

	/***
	 * Closes the browser
	 */
	public void closeBrowser() {
		try {
			webDriver.get().close();
			webDriver.get().quit();
			webDriver.remove();
		}catch(Exception e) {
		}
	}
}
