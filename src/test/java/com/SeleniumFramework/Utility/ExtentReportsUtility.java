package com.SeleniumFramework.Utility;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;

import org.openqa.selenium.WebDriver;

public class ExtentReportsUtility {
	protected WebDriver driver;
	public static ExtentReports report;
	public static ExtentSparkReporter spark;
	public static ExtentTest testLogger;
	protected static ExtentReportsUtility extentObject = null;

	protected ExtentReportsUtility() {
		startExtentReport();
	}

	public static ExtentReportsUtility getInstance() {
		if (extentObject == null) {
			extentObject = new ExtentReportsUtility();
		}
		return extentObject;
	}

	public void startExtentReport() {
		try {
			File reportDir = new File(Constants.SPARKS_HTML_REPORT_PATH).getParentFile();
			if (!reportDir.exists()) {
				reportDir.mkdirs();
			}

			report = new ExtentReports();
			spark = new ExtentSparkReporter(Constants.SPARKS_HTML_REPORT_PATH);
			report.setSystemInfo("Host Name", "Salesforce");
			report.setSystemInfo("Environment", "QA");
			report.setSystemInfo("User Name", "Vinutha");

			spark.config().setDocumentTitle("Test Execution Report");
			spark.config().setReportName("Salesforce Regression Tests");
			spark.config().setTheme(Theme.DARK);
			report.attachReporter(spark);

			System.out.println("Extent report started successfully at: " + Constants.SPARKS_HTML_REPORT_PATH);
		} catch (Exception e) {
			System.out.println("Error starting extent report: " + e.getMessage());
		}
	}

	public void startSingleTestReport(String methodName) {
		testLogger = report.createTest(methodName);
	}

	public void endReport() {
		if (report != null) {
			report.flush();
		}
	}

	public void logTestInfo(String text) {
		if (testLogger != null) {
			testLogger.log(Status.INFO, text);
		} else {
			System.out.println("testLogger is not initialized. Call startSingleTestReport() first.");
		}
	}

	public void logTestPassed(String text) {
		if (testLogger != null) {
			testLogger.log(Status.PASS, MarkupHelper.createLabel(text, ExtentColor.GREEN));
		}
	}

	public void logTestFailed(String text) {
		if (testLogger != null) {
			testLogger.log(Status.FAIL, MarkupHelper.createLabel(text, ExtentColor.RED));
		}
	}

	public void logTestFailedWithException(Throwable e) {
		if (testLogger != null) {
			testLogger.log(Status.FAIL, e);
		}
	}

	public void logTestWithScreenshot(String path) {
		if (testLogger != null) {
			try {
				testLogger.fail(MediaEntityBuilder.createScreenCaptureFromPath(path).build());
			} catch (Exception e) {
				System.out.println("Screenshot path error: " + e.getMessage());
			}
		}
	}
}
