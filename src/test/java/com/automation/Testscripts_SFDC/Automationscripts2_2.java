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

public class Automationscripts2_2 extends Salesforce_Login {

	private static final Logger mylog = LogManager.getLogger(Automationscripts2_2.class);
	private static final String username_data = Properties_Utility.readDataFromPropertyFile(Constants.APP_PROPERTIES,
			"username");
	private static final String password_data = Properties_Utility.readDataFromPropertyFile(Constants.APP_PROPERTIES,
			"password");

	@BeforeMethod
	public void setupTest(Method method) {
		ExtentReportsUtility.getInstance().startSingleTestReport(method.getName());
		mylog.info("Starting test: " + method.getName());
	}

	@AfterMethod
	public void tearDown() {
		ExtentReportsUtility.getInstance().endReport();
		closeBrowser();
	}

	private WebElement waitForVisibility(By locator, int timeoutSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	private void verifyAndClick(By locator, String elementDescription) throws InterruptedException {
		WebElement element = driver.findElement(locator);
		Assert.assertTrue(element.isDisplayed(), elementDescription + " is not displayed as expected.");
		mylog.info(elementDescription + " is displayed.");
		element.click();
		Thread.sleep(2000);
	}

	private void loginAndValidateError(String expectedErrorMessage) throws InterruptedException {
		slfLogin(username_data, "");
		WebElement loginButton = driver.findElement(By.id("Login"));
		loginButton.click();
		Thread.sleep(2000);

		try {
			WebElement errorMessageElement = waitForVisibility(
					By.xpath("//div[contains(text(), 'Please enter your password.')]"), 10);
			String actualErrorMessage = errorMessageElement.getText();
			Assert.assertEquals(actualErrorMessage, expectedErrorMessage, "Incorrect error message displayed.");
			mylog.info("Correct error message displayed: " + actualErrorMessage);
		} catch (TimeoutException e) {
			mylog.error("Expected error message did not appear: " + expectedErrorMessage);
			Assert.fail("Error message validation failed.");
		}
	}

	@Test
	public void test1_SFDC() throws InterruptedException {
		loginAndValidateError("Please enter your password.");
	}

	@Test
	public void test2_SFDC() throws InterruptedException {
		WebElement usernameField = driver.findElement(By.id("username"));
		Assert.assertTrue(usernameField.isDisplayed(), "Salesforce login page is not displayed as expected.");
		mylog.info("Salesforce login page is displayed.");

		slfLogin(username_data, password_data);
		WebElement loginButton = driver.findElement(By.id("Login"));
		loginButton.click();
		Thread.sleep(2000);

		try {
			WebElement homeTab = driver.findElement(By.id("home_Tab"));
			Assert.assertTrue(homeTab.isDisplayed(), "Salesforce home page is not displayed after login.");
			Thread.sleep(2000);
			mylog.info("Salesforce home page is displayed after login.");
		} catch (Exception e) {
			mylog.error("Salesforce home page verification failed.");
			Assert.fail("Salesforce home page was not displayed after login.");
		}
	}

	@Test
	public void test3_SFDC() throws InterruptedException {
		WebElement usernameField = waitForVisibility(By.id("username"), 10);
		Assert.assertTrue(usernameField.isDisplayed(), "Salesforce login page is not displayed as expected.");

		if (usernameField.getAttribute("value").isEmpty()) {
			usernameField.sendKeys(username_data);
		}

		WebElement rememberUsernameCheckbox = driver.findElement(By.id("rememberUn"));
		if (rememberUsernameCheckbox.isDisplayed() && !rememberUsernameCheckbox.isSelected()) {
			rememberUsernameCheckbox.click();
			Thread.sleep(2000);
			Assert.assertTrue(rememberUsernameCheckbox.isSelected(),
					"Remember Username checkbox could not be selected.");
			mylog.info("Remember Username checkbox is selected.");
		}

		slfLogin(username_data, password_data);
		WebElement loginButton = driver.findElement(By.id("Login"));
		loginButton.click();
		Thread.sleep(2000);
	}

	@Test
	public void testForgotPasswordFunctionality() throws InterruptedException {
		mylog.info("SFDC application launched.");
		try {
			WebElement usernameField = waitForVisibility(By.id("username"), 30);
			Assert.assertTrue(usernameField.isDisplayed(), "SFDC login page is not displayed as expected.");
			mylog.info("SFDC login page is displayed successfully.");

			verifyAndClick(By.id("forgot_password_link"), "Forgot Password link");

			WebElement forgotPasswordHeader = waitForVisibility(By.xpath("//*[@id=\"header\"]"), 30);
			Assert.assertTrue(forgotPasswordHeader.isDisplayed(), "Forgot Password page is not displayed as expected.");
			mylog.info("Forgot Password page is displayed successfully.");

			WebElement forgotUsernameField = driver.findElement(By.id("un"));
			forgotUsernameField.sendKeys(username_data);

			WebElement continueButton = driver.findElement(By.id("continue"));
			continueButton.click();
			Thread.sleep(2000);

			WebElement resetMessage = waitForVisibility(By.xpath("//p[contains(text(), 'We’ve sent you an email')]"),
					30);
			Assert.assertTrue(resetMessage.getText().contains("We’ve sent you an email"),
					"Password reset message page is not displayed as expected.");
			mylog.info("Password reset message page is displayed successfully.");
		} catch (Exception e) {
			mylog.error("SFDC Forgot Password functionality failed.", e);
			Assert.fail("Forgot Password process did not complete as expected.");
		}
	}
}
