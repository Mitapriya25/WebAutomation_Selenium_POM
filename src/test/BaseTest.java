package test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import main.pages.*;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import main.common.GlobalVariables;
import main.driver.DriverFactory;
import main.driver.DriverManager;
import main.reporter.ExtentFactory;
import main.reporter.ExtentManager;

public class BaseTest {
	protected  WebDriver driver;
	private final static Logger log = LogManager.getLogger(BaseTest.class);
	ExtentTest extentTest;
	HashMap<String,String> testData = new HashMap<>();
	TipTopPage tipTopPage;

	/***
	 * Initializing all the test data from properties files.
	 */
	public BaseTest() {
		addToTestData(this.getClass().getSimpleName()+".properties");
		addToTestData("UserCredentials.properties");
		addToTestData("SuccessMessages.properties");
		addToTestData("ErrorMessages.properties");
		addToTestData("PageTitles.properties");
	}

	/***
	 * Setting up the extent report
	 *
	 * @throws IOException
	 */
	@BeforeSuite
	public void beforeSuite() throws IOException {
		initializeGlobalVariables();
		GlobalVariables.extentReports = ExtentManager.setupExtentReport();
	}

	@AfterSuite
	public void afterSuite() throws IOException {
		GlobalVariables.extentReports.setSystemInfo("Device Type", GlobalVariables.deviceType);
		if(!GlobalVariables.deviceType.toLowerCase().equals("desktop")) {
			GlobalVariables.extentReports.setSystemInfo("Device Name", GlobalVariables.configProperties.getProperty("deviceName"));
		}
		GlobalVariables.extentReports.flush();
	}

	/***
	 * Initializing the driver
	 *
	 * @param context		Object of TestContext
	 * @param method		Object of Method
	 *
	 * @throws IOException
	 */
	@BeforeMethod
	public void beforeMethod(ITestContext context, Method method) throws IOException{
		DriverManager.getInstance().setWebDriver(DriverFactory.createInstance());
		driver = DriverManager.getInstance().getDriver();
		driver.manage().timeouts().implicitlyWait(GlobalVariables.implicitWaitTimeout, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(GlobalVariables.pageLoadTimeout,TimeUnit.SECONDS);
		log.info("Driver initialized successfully");

		driver.get(GlobalVariables.baseURL);
		String methodName = method.getName();
		log.info(methodName+" Test Method started");
		extentTest = GlobalVariables.extentReports.createTest(methodName);
		ExtentFactory.getInstance().setExtentTest(extentTest);
	}


	/***
	 * Setting the test status after test execution
	 *
	 * @param result		Object of TestResult
	 * @param method		Object of Method
	 */
	@AfterMethod
	public void afterMethod(ITestResult result, Method method){
		int status =result.getStatus();
		String testName = method.getName();
		if(status == ITestResult.SUCCESS) {
			ExtentFactory.getInstance().getExtentTest().pass("Test Method "+testName+ " Passed");
		}else if(status == ITestResult.FAILURE) {
			ExtentFactory.getInstance().getExtentTest().fail("Test Method "+testName+ " Failed");
			ExtentFactory.getInstance().getExtentTest().log(Status.FAIL, result.getThrowable());
			try {
				ExtentFactory.getInstance().getExtentTest().addScreenCaptureFromPath(captureScreenShot(), testName);
			} catch (IOException e) {
				log.error("Failed to attach screen shot to extent report "+ e);
			}
		}else if(status == ITestResult.SKIP) {
			ExtentFactory.getInstance().getExtentTest().skip("Test Method "+testName+ " Skipped");
		}
		DriverManager.getInstance().closeBrowser();
	}

	/***
	 * Captures screenshot
	 *
	 * @return			Returns file path of the screenshot
	 */
	synchronized public String captureScreenShot() {
		File srcFile = ((TakesScreenshot) DriverManager.getInstance().getDriver()).getScreenshotAs(OutputType.FILE);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String ts = timestamp.toString().replace(" ", "").replace(".", "_").replace(":", "_");
		String destFilePath = GlobalVariables.baseDir + "/reports/ExtentReports/" + GlobalVariables.currentExtentReportDir + "/screenshots/" + ts + ".jpeg";
		try {
			FileUtils.copyFile(srcFile, new File(destFilePath));
			log.info("Screen shot File " + destFilePath + " copied");
		} catch (Exception e) {
			log.error("Failed to capture to screen shot");
		}
		return destFilePath;
	}
	/***
	 * Initialize Global Variables
	 */
	private void initializeGlobalVariables() {
		try {
			FileReader reader=new FileReader("config.properties");
			GlobalVariables.configProperties =new Properties();
			GlobalVariables.configProperties.load(reader);
			GlobalVariables.baseDir = System.getProperty("user.dir");
			GlobalVariables.baseURL =GlobalVariables.configProperties.getProperty("url");
			GlobalVariables.browserName = GlobalVariables.configProperties.getProperty("browserName");
			GlobalVariables.pageLoadTimeout = Integer.parseInt(GlobalVariables.configProperties.getProperty("pageLoadTimeout"));
			GlobalVariables.implicitWaitTimeout = Integer.parseInt(GlobalVariables.configProperties.getProperty("implicitWaitTimeout"));

		}catch (IOException e) {
			log.error("Failed to read config.properties"+ e);
		}
	}
	/***
	 * Adds data to test data map.
	 *
	 * @param fileName		FileName data to be added to map
	 */
	protected void addToTestData(String fileName) {
		String filePath = "testData/"+fileName;
		Properties prop = null;
		try {
			File file = new File(filePath);
			if(file.exists()) {
				FileReader reader=new FileReader(filePath);
				prop =new Properties();
				prop.load(reader);
				for (final String name: prop.stringPropertyNames()) {
					testData.put(name, prop.getProperty(name));
				}
			} else {
				log.warn("Test Data File not present: " + fileName);
			}
		}catch (IOException e) {
			log.error("Failed to read "+filePath+" and add to testData map"+ e);
		}
	}
}