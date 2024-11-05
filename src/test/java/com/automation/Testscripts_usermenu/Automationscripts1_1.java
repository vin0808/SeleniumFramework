package com.automation.Testscripts_usermenu;

import java.time.Duration;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.SeleniumFramework.Base.Salesforce_Login;
import com.SeleniumFramework.Utility.Constants;
import com.SeleniumFramework.Utility.ExtentReportsUtility;
import com.SeleniumFramework.Utility.Properties_Utility;

public class Automationscripts1_1 extends Salesforce_Login {

	private static Logger mylog = LogManager.getLogger(Salesforce_Login.class);
	ExtentReportsUtility reportUtil = ExtentReportsUtility.getInstance();

	// Login method for reuse across tests
	private void loginToSalesforce(WebDriverWait wait) {
		String username = Properties_Utility.readDataFromPropertyFile(Constants.APP_PROPERTIES, "username");
		String password = Properties_Utility.readDataFromPropertyFile(Constants.APP_PROPERTIES, "password");
		slfLogin(username, password);

		WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("Login")));
		loginButton.click();
		mylog.info("Logged in to Salesforce successfully.");
	}

	@Test
	public void test5_Usermenu() {

		reportUtil.startSingleTestReport("test5_Usermenu"); // Start test report
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Create a wait object

		try {
			String username_data = Properties_Utility.readDataFromPropertyFile(Constants.APP_PROPERTIES, "username");
			String password_data = Properties_Utility.readDataFromPropertyFile(Constants.APP_PROPERTIES, "password");

			// Logging in
			slfLogin(username_data, password_data);

			WebElement buttonelement = wait.until(ExpectedConditions.elementToBeClickable(By.id("Login")));
			buttonelement.click();

			WebElement userMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userNav-arrow")));
			Assert.assertTrue(userMenu.isDisplayed(), "User menu is not displayed - login may have failed.");

			// Validate that the correct username is displayed on the homepage
			userMenu.click(); // Open the user dropdown
			WebElement usernameDisplay = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("userNavLabel")));

			String expectedUsername = "Vinutha Prabhakar"; // Replace with the actual username for validation
			String actualUsername = usernameDisplay.getText();
			Assert.assertEquals(actualUsername, expectedUsername,
					"Logged-in username does not match expected username.");

			// Log successful validation
			mylog.info("Salesforce login successful, home page is displayed with correct username: " + actualUsername);

			// Step 4: Wait for the dropdown options to be visible
			List<WebElement> dropdownOptions = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("userNav-menuItems")));

			// Step 5: Validate the dropdown options
			String[] expectedOptions = { "My Profile", "My Settings", "Developer Console", "Logout",
					"Switching to Lightning Experience" };

			for (String expectedOption : expectedOptions) {
				boolean optionFound = false; // Flag to track if the option is found

				// Loop through the dropdown options
				for (WebElement option : dropdownOptions) {
					if (option.getText().equals(expectedOption)) {
						optionFound = true; // Set flag to true if the option is found
						break; // Exit the loop as we found the option
					}
				}
			}

			// Optional: Log the successful validation
			mylog.info("All expected options are displayed in the user menu dropdown.");
		} catch (Exception e) {
			reportUtil.logTestFailedWithException(e);
			mylog.error("Test failed with exception: " + e.getMessage());
		} finally {
			reportUtil.endReport(); // End test report
		}
	}

	@Test
	public void test8_Usermenu() {
		reportUtil.startSingleTestReport("test8_Usermenu");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		try {
			loginToSalesforce(wait);

			WebElement userMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userNav-arrow")));
			userMenu.click();
			mylog.info("User menu opened.");

			List<WebElement> dropdownOptions = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("userNav-menuItems")));
			dropdownOptions.forEach(option -> mylog.info(option.getText())); // Log each option

			WebElement consoleTab = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='userNav-menuItems']/a[3]")));
			consoleTab.click();

			// Store the current window handle (main window) before switching
			String originalWindow = driver.getWindowHandle();

			// Switch to the new Developer Console window
			for (String windowHandle : driver.getWindowHandles()) {
				if (!windowHandle.equals(originalWindow)) {
					driver.switchTo().window(windowHandle);
					driver.close(); // Close the Developer Console window
					mylog.info("Developer Console window closed.");
					break;
				}
			}

			// Switch back to the original window
			driver.switchTo().window(originalWindow);

			// Optional: verify the driver is in the correct window and log success
			String currentUrl = driver.getCurrentUrl();
			Assert.assertEquals(currentUrl,
					"https://tekarch79-dev-ed.develop.my.salesforce.com/setup/forcecomHomepage.apexp?setupid=ForceCom",
					"Failed to open Developer Console.");

		} catch (Exception e) {
			reportUtil.logTestFailedWithException(e);
			mylog.error("Test failed with exception: ", e);
		} finally {
			reportUtil.endReport();
		}
	}

	@Test
	public void test9_Usermenu() {
		reportUtil.startSingleTestReport("test9_Usermenu");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		try {
			loginToSalesforce(wait);

			WebElement userMenu = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='userNav-arrow']")));
			userMenu.click();

			WebElement logoutOption = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@title='Logout']")));
			logoutOption.click();

			String currentUrl = driver.getCurrentUrl();
			Assert.assertEquals(currentUrl, "https://tekarch79-dev-ed.develop.my.salesforce.com/secur/logout.jsp",
					"Logout failed or URL mismatch.");

			mylog.info("Logout successful, login page displayed.");

		} catch (Exception e) {
			reportUtil.logTestFailedWithException(e);
			mylog.error("Test failed with exception: ", e);
		} finally {
			reportUtil.endReport();
		}
	}
}
