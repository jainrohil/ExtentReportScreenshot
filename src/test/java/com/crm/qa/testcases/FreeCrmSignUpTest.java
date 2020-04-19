package com.crm.qa.testcases;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.crm.qa.util.TestUtil;

public class FreeCrmSignUpTest {


	public WebDriver driver;
	public TestUtil testUtil;
	
	@BeforeMethod
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "D:\\ChromeDriver\\chromedriver.exe");
		driver = new ChromeDriver();
		testUtil = new TestUtil();
		
		driver.manage().window().maximize();
		
		//PageLoadTimeOut and ImplicitWait could also be defined in the config.properties file
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		driver.get("https://ui.cogmento.com/");		
	}
	
	@Test
	public void signUpBtnTest() {
		boolean signUpBtn = driver.findElement(By.xpath("//a[contains(text(),'Sign Up')]")).isDisplayed();
		Assert.assertEquals(signUpBtn, false,"SignUp button not found");
	}

	@AfterMethod
	public void tearDown(ITestResult result) throws IOException {
		//Check if the test case failed or was skipped and take screenshot
		if(result.getStatus()==result.FAILURE || result.getStatus()==result.SKIP) {
		String screenshotPath = testUtil.getScreenshot(driver, result.getName());
		result.setAttribute("screenshotPath", screenshotPath); //sets the value the variable/attribute screenshotPath as the path of the sceenshot
		}
		driver.quit();
	}
	
}
