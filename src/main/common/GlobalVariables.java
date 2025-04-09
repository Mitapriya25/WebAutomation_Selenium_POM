package main.common;

import java.util.Properties;

import com.aventstack.extentreports.ExtentReports;

public class GlobalVariables {
	public static Properties configProperties = new Properties();
	public static String baseURL;
	public static String currentExtentReportDir;
	public static String baseDir;
	public static String browserName;
	public static ExtentReports extentReports;
	public static String deviceType = "desktop";
	public static int pageLoadTimeout;
	public static int implicitWaitTimeout;
}
