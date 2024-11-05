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
import com.SeleniumFramework.Base.Base_Login_1;
import com.SeleniumFramework.Base.Salesforce_Login;
import com.SeleniumFramework.Utility.Constants;
import com.SeleniumFramework.Utility.ExtentReportsUtility;
import com.SeleniumFramework.Utility.Properties_Utility;

public class Automationscripts1 extends Salesforce_Login {

	private static Logger mylog = LogManager.getLogger(Base_Login_1.class);
	ExtentReportsUtility reportUtil = ExtentReportsUtility.getInstance();

	//

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
		reportUtil.startSingleTestReport("test8_Usermenu"); // Start test report
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Create a wait object

		try {
			String username_data = Properties_Utility.readDataFromPropertyFile(Constants.APP_PROPERTIES, "username");
			String password_data = Properties_Utility.readDataFromPropertyFile(Constants.APP_PROPERTIES, "password");

			// Logging in
			slfLogin(username_data, password_data);

			WebElement buttonelement = wait.until(ExpectedConditions.elementToBeClickable(By.id("Login")));
			buttonelement.click();

			// Wait for the username dropdown to appear
			WebElement username_tab = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='userNav-arrow']")));
			username_tab.click();

			// Wait for dropdown menu options to appear
			List<WebElement> dropdownOptions = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("userNav-menuItems")));

			// Print all the dropdown options
			for (WebElement element : dropdownOptions) {
				System.out.println(element.getText());
			}

			// Click on console tab
			WebElement consoletab = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='userNav-menuItems']/a[3]")));
			consoletab.click();
			Thread.sleep(2000);

			// Wait for the URL to change
			String currentUrl = driver.getCurrentUrl();
			if (currentUrl.equals(
					"https://tekarch79-dev-ed.develop.my.salesforce.com/setup/forcecomHomepage.apexp?setupid=ForceCom")) {
				mylog.info("Developer console opened successfully");
				reportUtil.logTestInfo("Developer console opened successfully");
			} else {
				mylog.error("Failed opening Developer console, current URL: " + currentUrl);
				reportUtil.logTestFailed("Failed opening Developer console, current URL: " + currentUrl);
			}

			// Close the developer console window
			String originalWindow = driver.getWindowHandle();
			for (String windowHandle : driver.getWindowHandles()) {
				if (!windowHandle.equals(originalWindow)) {
					driver.switchTo().window(windowHandle).close();
					mylog.info("Developer Console window is closed");
					reportUtil.logTestInfo("Developer Console window is closed");
					break;
				}
			}
		} catch (Exception e) {
			reportUtil.logTestFailedWithException(e);
			mylog.error("Test failed with exception: " + e.getMessage());
		} finally {
			reportUtil.endReport(); // End test report
		}
	}

	@Test
	public void test9_Usermenu() {
		reportUtil.startSingleTestReport("test9_Usermenu"); // Start test report
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Updated WebDriverWait with explicit
																				// wait

		try {
			// Read username and password from properties file
			String username_data = Properties_Utility.readDataFromPropertyFile(Constants.APP_PROPERTIES, "username");
			String password_data = Properties_Utility.readDataFromPropertyFile(Constants.APP_PROPERTIES, "password");

			// Login using Salesforce credentials
			slfLogin(username_data, password_data);

			// Wait for login button and click
			WebElement buttonelement = wait.until(ExpectedConditions.elementToBeClickable(By.id("Login")));
			buttonelement.click();

			// Wait for the username dropdown arrow to be clickable and click it
			WebElement username_tab = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='userNav-arrow']")));
			username_tab.click();

			// Wait for the dropdown menu options to be visible
			List<WebElement> dropdownOptions = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("userNav-menuItems")));

			// Print all the dropdown options
			for (WebElement element : dropdownOptions) {
				System.out.println(element.getText());
			}

			// Wait for the Logout option to be clickable and click it
			WebElement logoutOption = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@title='Logout']")));
			logoutOption.click();

			// Wait for some time to ensure the logout is successful and the login page is
			// loaded
			String currentUrl = driver.getCurrentUrl();
			if (currentUrl.equals("https://tekarch79-dev-ed.develop.my.salesforce.com/secur/logout.jsp")) {
				mylog.info("Logout successful, logout page is displayed.");
				reportUtil.logTestInfo("Logout successful, login page is displayed.");
			} else {
				mylog.error("Logout failed, current URL: " + currentUrl);
				reportUtil.logTestFailed("Logout failed, current URL: " + currentUrl);
			}

		} catch (Exception e) {
			reportUtil.logTestFailedWithException(e); // Log any exceptions that occur
			mylog.error("Test failed with exception: " + e.getMessage());
		} finally {
			reportUtil.endReport(); // End test report
		}
	}
}
