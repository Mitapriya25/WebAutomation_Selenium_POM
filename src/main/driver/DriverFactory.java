package main.driver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import main.common.GlobalVariables;
import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {

	/***
	 * Creates driver instance
	 * 
	 * @return		Object of WebDriver
	 * 
	 * @throws IOException
	 */
	public static WebDriver createInstance() throws IOException {
		WebDriver driver = null;
		String executionMode = GlobalVariables.configProperties.getProperty("executionMode");
		if (executionMode.equalsIgnoreCase("local")) {
			return getLocalWebDriver();
		} else if (executionMode.equalsIgnoreCase("saucelab")) {
			driver = getRemoteWebDriverForSauceLab();
			return driver;
		} else if (executionMode.equalsIgnoreCase("grid")) {
			driver = getRemoteWebDriverForGrid();
			return driver;
		}
		return driver;
	}

	/***
	 * Get Local Chrome Driver
	 * 
	 * @return	Object of ChromeDriver
	 */
	private static synchronized ChromeDriver getLocalChromeDriver() {
		ChromeDriverService chromeDriverService = new ChromeDriverService.Builder().usingAnyFreePort().build();
		ChromeOptions options = getChromeOptionsForDevice();
		return new ChromeDriver(chromeDriverService, options);
	}

	/***
	 * Get Remote Web Driver For Sauce Lab
	 * 
	 * @return			Object of WebDriver
	 * 
	 * @throws MalformedURLException
	 */
	private static synchronized WebDriver getRemoteWebDriverForSauceLab() throws MalformedURLException {
		DesiredCapabilities caps = null;

		String url = GlobalVariables.configProperties.getProperty("remoteUrl");
		String userName = GlobalVariables.configProperties.getProperty("remoteUsername");
		String accessKey = GlobalVariables.configProperties.getProperty("remoteAccessKey");
		String platform = GlobalVariables.configProperties.getProperty("remoteOsName");
		String platformVersion = GlobalVariables.configProperties.getProperty("remoteOsVersion");

		url = url.replace("{username}", userName);
		url = url.replace("{accessKey}", accessKey);
		caps = getDesiredCapForBrowser(GlobalVariables.configProperties.getProperty("browserName"));
		caps.setCapability("platform", platform);
		caps.setCapability("version", platformVersion);
		return new RemoteWebDriver(new URL(url), caps);
	}

	/***
	 * Get Remote Web Driver for Grid
	 * 
	 * @return			Object of WebDriver
	 * 
	 * @throws MalformedURLException
	 */
	private static synchronized WebDriver getRemoteWebDriverForGrid() throws MalformedURLException {
		DesiredCapabilities caps = null;

		String url = GlobalVariables.configProperties.getProperty("gridUrl");
		String platform = GlobalVariables.configProperties.getProperty("gridOsName");

		caps = getDesiredCapForBrowser(GlobalVariables.configProperties.getProperty("gridBrowserName"));
		caps.setCapability("platform", platform);
		return new RemoteWebDriver(new URL(url), caps);
	}

	/***
	 * Get Local Web Driver
	 * 
	 * @return			Object of WebDriver
	 */
	private static synchronized WebDriver getLocalWebDriver() {
		String browserName = GlobalVariables.configProperties.getProperty("browserName");
		WebDriver driver = null;
		if (browserName.toLowerCase().equals("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			return driver;
		}
		if (browserName.toLowerCase().equals("internet explorer") | browserName.toLowerCase().equals("ie")) {
			WebDriverManager.iedriver().setup();
			driver = new InternetExplorerDriver();
			return driver;
		}
		if (browserName.toLowerCase().equals("edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			return driver;
		}
		if (browserName.toLowerCase().equals("opera")) {
			WebDriverManager.operadriver().setup();
			driver = new OperaDriver();
			return driver;
		}
		if (browserName.toLowerCase().equals("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = getLocalChromeDriver();
			driver.manage().window().maximize();
			return driver;
		}
		return driver;

	}

	/***
	 * Get Desired Capabilities for the specified browser
	 * 
	 * @param browserName		Name of the browser
	 * 
	 * @return			Object of DesiredCapabilities
	 */
	@SuppressWarnings("deprecation")
	private static DesiredCapabilities getDesiredCapForBrowser(String browserName) {
		DesiredCapabilities caps = null;
		switch (browserName.toLowerCase()) {
		case "chrome":
			caps = getDesiredCapsForChrome();
			break;
		case "ie":
		case "internet explorer":
			caps = DesiredCapabilities.internetExplorer();
			break;
		case "firefox":
			caps = DesiredCapabilities.firefox();
			break;
		case "opera":
			caps = DesiredCapabilities.opera();
			break;
		case "edge":
			caps = DesiredCapabilities.edge();
			break;

		}
		return caps;
	}

	/***
	 * Get Desired Capabilities for Chrome
	 * 
	 * @return			Object of DesiredCapabilities
	 */
	private static DesiredCapabilities getDesiredCapsForChrome() {
		DesiredCapabilities caps =DesiredCapabilities.chrome();
		ChromeOptions chromeOptions = getChromeOptionsForDevice();
		caps.setCapability(ChromeOptions.CAPABILITY,chromeOptions);
		return caps;
	}

	/***
	 * Get Chrome Options for Device
	 * 
	 * @return			Return Object of ChromeOptions
	 */
	private static ChromeOptions getChromeOptionsForDevice() {
		ChromeOptions options =new ChromeOptions();
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("safebrowsing.enabled", "true");
		chromePrefs.put("credentials_enable_service", false);
		chromePrefs.put("profile.password_manager_enabled", false);
		options.setExperimentalOption("prefs", chromePrefs);
		options.addArguments("start-maximized");
		options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--disable-browser-side-navigation");
		options.addArguments("--disable-gpu");

		String deviceType = GlobalVariables.configProperties.getProperty("deviceType");
		switch(deviceType.toLowerCase()) {
		case "desktop": 
			return options;
		case "mobile":
		case "tablet":
			GlobalVariables.deviceType = GlobalVariables.configProperties.getProperty("deviceType");
			Map<String, String> mobileEmulation = new HashMap<>();
			mobileEmulation.put("deviceName", GlobalVariables.configProperties.getProperty("deviceName"));
			options.setExperimentalOption("mobileEmulation", mobileEmulation);
			return options;
		}
		return options;
	}
}