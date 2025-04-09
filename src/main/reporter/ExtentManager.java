package main.reporter;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import main.common.GlobalVariables;

public class ExtentManager {
	private static ExtentReports extentReports;

	/***
	 * Sets up Extent Report
	 * 
	 * @return	Object of ExtentReports
	 */
    public static ExtentReports setupExtentReport() {
        if(extentReports==null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyy HH-mm-ss");
            Date date = new Date();
            String today = simpleDateFormat.format(date);
            GlobalVariables.currentExtentReportDir = today;
            String reportFilePath = GlobalVariables.baseDir + "/reports/ExtentReports/" + today + "/Report_" + today + ".html";
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFilePath);
            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            sparkReporter.config().setDocumentTitle("Odyssey Web UI Suite Execution");
            extentReports.setSystemInfo("Browser Name", GlobalVariables.browserName);
            extentReports.setSystemInfo("OS Name", System.getProperty("os.name"));
            extentReports.setSystemInfo("Host", getHostName());
            extentReports.setSystemInfo("Base URL", GlobalVariables.baseURL);
        }
        return extentReports;
    }
    
    private static String getHostName() {
    	java.net.InetAddress localMachine = null;
		try {
			localMachine = java.net.InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
	
		}
    	return localMachine.getHostName();
    }
}
