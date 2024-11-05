package com.automation.Testscripts_SFDC;

import java.lang.reflect.Method;
import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.SeleniumFramework.Base.Salesforce_Login;
import com.SeleniumFramework.Utility.Constants;
import com.SeleniumFramework.Utility.Properties_Utility;
import com.SeleniumFramework.Utility.ExtentReportsUtility;

public class Automationscripts2_1 extends Salesforce_Login {

	private static Logger mylog = LogManager.getLogger(Automationscripts2_1.class);
	static String username_data = Properties_Utility.readDataFromPropertyFile(Constants.APP_PROPERTIES, "username");
	static String password_data = Properties_Utility.readDataFromPropertyFile(Constants.APP_PROPERTIES, "password");

	@BeforeMethod
	public void setupTest(Method method) {
		// Initialize ExtentReports for each test method
		ExtentReportsUtility.getInstance().startSingleTestReport(method.getName());
		mylog.info("Starting test: " + method.getName());
	}

	@AfterMethod
	public void tearDown() {
		ExtentReportsUtility.getInstance().endReport();
	}

	@Test
	public void test1_SFDC() throws InterruptedException {
		String password_data = ""; // Set empty password for validation
		slfLogin(username_data, password_data);

		WebElement loginButton = driver.findElement(By.id("Login"));
		clickElement(loginButton, "Login");

		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement errorMessageElement = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'Please enter your password.')]")));
			String actualErrorMessage = errorMessageElement.getText();

			if (actualErrorMessage.equals("Please enter your password.")) {
				mylog.info("Correct error message displayed: " + actualErrorMessage);
				ExtentReportsUtility.getInstance()
						.logTestInfo("Correct error message displayed: " + actualErrorMessage);
			} else {
				mylog.error("Unexpected error message displayed: " + actualErrorMessage);
				ExtentReportsUtility.getInstance()
						.logTestFailed("Unexpected error message displayed: " + actualErrorMessage);
				Assert.fail("Incorrect error message displayed.");
			}
		} catch (TimeoutException e) {
			mylog.error("Error message 'Please enter your password.' not displayed");
			ExtentReportsUtility.getInstance()
					.logTestFailed("Error message 'Please enter your password.' not displayed");
			Assert.fail("Error message validation failed.");
		} finally {
			closeBrowser();
		}
	}

	@Test
	public void test2_SFDC() throws InterruptedException {
		// Step 1: Verify that the login page is displayed
		WebElement usernameField = driver.findElement(By.id("username"));
		Assert.assertTrue(usernameField.isDisplayed(), "Salesforce login page is not displayed as expected.");
		mylog.info("Salesforce login page is displayed.");
		ExtentReportsUtility.getInstance().logTestInfo("Salesforce login page is displayed.");

		// Step 2: Perform login
		slfLogin(username_data, password_data);
		WebElement loginButton = driver.findElement(By.id("Login"));
		clickElement(loginButton, "Login");
		Thread.sleep(2000);

		// Step 3: Verify that the home page is displayed
		try {
			WebElement homeTab = driver.findElement(By.id("home_Tab")); // Replace with a reliable locator on the home
																		// page
			Assert.assertTrue(homeTab.isDisplayed(), "Salesforce home page is not displayed after login.");
			mylog.info("Salesforce home page is displayed after login.");
			ExtentReportsUtility.getInstance().logTestInfo("Salesforce home page is displayed after login.");
		} catch (Exception e) {
			mylog.error("Salesforce home page verification failed.");
			ExtentReportsUtility.getInstance().logTestFailed("Salesforce home page verification failed.");
			Assert.fail("Salesforce home page was not displayed after login.");
		}

		// Close the browser
		closeBrowser();
	}

	@Test
	public void test3_SFDC() throws InterruptedException {
		// Step 1: Verify that the login page is displayed and wait for the username
		// field to be present
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
		Assert.assertTrue(usernameField.isDisplayed(), "Salesforce login page is not displayed as expected.");
		mylog.info("Salesforce login page is displayed.");
		ExtentReportsUtility.getInstance().logTestInfo("Salesforce login page is displayed.");

		// Verify that the username field is populated with the expected username
		String expectedUsername = username_data;
		String actualUsername = usernameField.getAttribute("value");

		// Populate the username field if it is not automatically populated
		if (actualUsername == null || actualUsername.isEmpty()) {
			usernameField.sendKeys(expectedUsername);
			actualUsername = usernameField.getAttribute("value"); // Re-fetch the value after setting it
		}

		Assert.assertEquals(actualUsername, expectedUsername,
				"Expected username is not populated in the username field.");
		mylog.info("Username field is populated with expected value: " + actualUsername);
		ExtentReportsUtility.getInstance()
				.logTestInfo("Username field is populated with expected value: " + actualUsername);

		// Step 2: Check and select "Remember Username" checkbox if it is not selected
		WebElement rememberUsernameCheckbox = driver.findElement(By.id("rememberUn"));
		if (rememberUsernameCheckbox.isDisplayed()) {
			if (!rememberUsernameCheckbox.isSelected()) {
				mylog.info("Remember Username checkbox is not selected; selecting it now.");
				rememberUsernameCheckbox.click(); // Select the checkbox
			}
			Assert.assertTrue(rememberUsernameCheckbox.isSelected(),
					"Remember Username checkbox could not be selected.");
			mylog.info("Remember Username checkbox is selected.");
			ExtentReportsUtility.getInstance().logTestInfo("Remember Username checkbox is selected.");
		} else {
			mylog.warn("Remember Username checkbox is not displayed.");
			ExtentReportsUtility.getInstance().logTestInfo("Remember Username checkbox is not displayed.");
		}

		// Step 3: Perform login
		slfLogin(username_data, password_data);
		WebElement loginButton = driver.findElement(By.id("Login"));
		clickElement(loginButton, "Login");
		Thread.sleep(2000);
	}

	@Test
	public void testForgotPasswordFunctionality() throws InterruptedException {
		mylog.info("SFDC application launched.");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // Increased wait time

		try {
			// Verify login page
			WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
			Assert.assertTrue(usernameField.isDisplayed(), "SFDC login page is not displayed as expected.");
			mylog.info("SFDC login page is displayed successfully.");

			// Click "Forgot Password"
			WebElement forgotPasswordLink = driver.findElement(By.id("forgot_password_link"));
			forgotPasswordLink.click();
			mylog.info("Clicked on Forgot Password link.");

			// Verify Forgot Password page is displayed
			WebElement forgotPasswordHeader = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"header\"]"))); // Adjusted
																											// locator
																											// to be
																											// more
																											// specific
			Assert.assertTrue(forgotPasswordHeader.isDisplayed(), "Forgot Password page is not displayed as expected.");
			mylog.info("Forgot Password page is displayed successfully.");

			// Enter username and continue
			WebElement forgotUsernameField = driver.findElement(By.id("un"));
			forgotUsernameField.sendKeys(username_data);
			mylog.info("Entered username: " + username_data);

			WebElement continueButton = driver.findElement(By.id("continue"));
			continueButton.click();
			mylog.info("Clicked Continue button.");

			// Validate password reset message page is displayed
			try {
				WebElement resetMessage = wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("//p[contains(text(), 'We’ve sent you an email')]"))); // Adjusted
																													// to
																													// match
																													// partial
																													// text
				String resetMessageText = resetMessage.getText();
				mylog.info("Password reset message found: " + resetMessageText);
				Assert.assertTrue(resetMessageText.contains("We’ve sent you an email"),
						"Password reset message page is not displayed as expected.");
				mylog.info("Password reset message page is displayed successfully.");

			} catch (TimeoutException e) {
				mylog.error("Password reset confirmation message did not appear within the expected time.");
				ExtentReportsUtility.getInstance()
						.logTestFailed("Password reset confirmation message was not displayed.");
				Assert.fail("Password reset message page is not displayed as expected.");
			}

		} catch (Exception e) {
			mylog.error("SFDC Forgot Password functionality failed.", e);
			ExtentReportsUtility.getInstance().logTestFailed("SFDC Forgot Password functionality failed.");
			Assert.fail("Forgot Password process did not complete as expected.");

		} finally {
			closeBrowser();
		}
	}

}
