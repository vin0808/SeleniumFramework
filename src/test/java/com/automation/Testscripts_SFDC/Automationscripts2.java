package com.automation.Testscripts_SFDC;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.SeleniumFramework.Base.Base_Login_1;
import com.SeleniumFramework.Base.Salesforce_Login;
import com.SeleniumFramework.Utility.Constants;
import com.SeleniumFramework.Utility.Properties_Utility;

public class Automationscripts2 extends Salesforce_Login {

	private static Logger mylog = LogManager.getLogger(Base_Login_1.class);

	static String username_data = Properties_Utility.readDataFromPropertyFile(Constants.APP_PROPERTIES, "username");
	static String password_data = Properties_Utility.readDataFromPropertyFile(Constants.APP_PROPERTIES, "password");

	@Test
	public static void test1_SFDC() throws InterruptedException {
		String password_data = ""; // Set empty password for validation
		slfLogin(username_data, password_data);

		// Locate the login button and click it
		WebElement buttonelement1 = driver.findElement(By.id("Login"));
		clickElement(buttonelement1, "Login");

		// Pause briefly to allow the error message to display
		Thread.sleep(2000);

		// Validation: check if the error message 'Please enter your password.' is
		// displayed
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement errorMessageElement = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'Please enter your password.')]"))); // Adjust
																														// locator
																														// as
																														// needed
			String actualErrorMessage = errorMessageElement.getText();

			if (actualErrorMessage.equals("Please enter your password.")) {
				mylog.info("Correct error message displayed: " + actualErrorMessage);
				extentreport.logTestInfo("Correct error message displayed: " + actualErrorMessage);
			} else {
				mylog.error("Unexpected error message displayed: " + actualErrorMessage);
				extentreport.logTestInfo("Unexpected error message displayed: " + actualErrorMessage);
				Assert.fail("Incorrect error message displayed.");
			}

		} catch (TimeoutException e) {
			mylog.error("Error message 'Please enter your password.' not displayed");
			extentreport.logTestInfo("Error message 'Please enter your password.' not displayed");
			Assert.fail("Error message validation failed.");
		} finally {
			closeBrowser();
		}

	}

	@Test
	public static void test2_SFDC() throws InterruptedException {

		slfLogin(username_data, password_data); // if home page is displayed print succesfull on the console
		WebElement buttonelement = driver.findElement(By.id("Login"));
		clickElement(buttonelement, "Login");
		Thread.sleep(2000);
		mylog.info("Login succesfull ");
		extentreport.logTestInfo("Login succesfull ");
		closeBrowser();
	}

	@Test
	public static void test3_SFDC() throws InterruptedException {

		slfLogin(username_data, password_data);

		WebElement rememberMeCheckbox = driver.findElement(By.id("rememberUn"));
		if (!rememberMeCheckbox.isSelected()) {
			clickElement(rememberMeCheckbox, "Remember_me");
			mylog.info("Remember_me checkbox checked");
			extentreport.logTestInfo("Remember_me checkbox checked");
		}

		WebElement buttonelement = driver.findElement(By.id("Login"));
		clickElement(buttonelement, "Login");
		Thread.sleep(2000);

		// display home page logout and display username

		WebElement username_tab = driver.findElement(By.xpath("//*[@id=\"userNav-arrow\"]"));
		clickElement(username_tab, "user_tab");
		Thread.sleep(2000);

		WebElement logoutLink = driver.findElement(By.xpath("//a[text()='Logout']"));
		logoutLink.click();
		mylog.info("logout link clicked");
		Thread.sleep(5000);

		WebElement usernameField = driver.findElement(By.id("username"));
		if (usernameField.isDisplayed()) {
			mylog.info("Username field is present.");
			extentreport.logTestInfo("Username field is present.");

			String displayedUsername = usernameField.getAttribute("aria-label");
			System.out.println(displayedUsername);

			// Check if the expected username is displayed
			String expectedUsername = "Vinutha Prabhakar";
			if (displayedUsername.equals(expectedUsername)) {
				mylog.info("Correct username is displayed: " + displayedUsername);
				extentreport.logTestInfo("Correct username is displayed: " + displayedUsername);
			} else {
				mylog.info("Incorrect username. Displayed: " + displayedUsername + " Expected: " + expectedUsername);
				extentreport.logTestFailed(
						"Incorrect username. Displayed: " + displayedUsername + " Expected: " + expectedUsername);
			}
		} else {
			mylog.info("Username field is not present.");
			extentreport.logTestFailed("Username field is not present.");
		}
		closeBrowser();
	}

	@Test
	public static void test4A_SFDC() throws InterruptedException {

		slfLogin(username_data, password_data);

		WebElement forgot_password = driver.findElement(By.id("forgot_password_link"));
		if (!forgot_password.isSelected()) {
			forgot_password.click();
		}

		WebElement usernameField = driver.findElement(By.id("un"));
		usernameField.sendKeys("navindu796@gmail.com");
		Thread.sleep(2000);

		WebElement continueButton = driver.findElement(By.id("continue"));
		continueButton.click();
		Thread.sleep(3000);

		WebElement confirmationMessage = driver.findElement(By.cssSelector("p.senttext.mb12"));
		String messageText = confirmationMessage.getText();
		mylog.info(messageText + " : text is displayed ");
		extentreport.logTestFailed(messageText + " : text is displayed ");

		closeBrowser();
	}

}
