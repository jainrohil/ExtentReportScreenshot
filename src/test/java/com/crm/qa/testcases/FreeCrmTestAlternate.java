package com.crm.qa.testcases;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class FreeCrmTestAlternate {

	public WebDriver driver;
	public ExtentReports extent;
	public ExtentTest extentTest;

	@BeforeTest
	public void setExtent() {
		extent = new ExtentReports(System.getProperty("user.dir")+"/test-output/Extenteport.html", true);
		extent.addSystemInfo("Host Name", "Rohil's PC");
		extent.addSystemInfo("User Name", "Rohil J.");
		extent.addSystemInfo("Environment", "QA");
	}
	
	public String getScreenshot(WebDriver driver, String screenShotName) throws IOException {
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		
		TakesScreenshot ts = (TakesScreenshot) driver;
		
		File source = ts.getScreenshotAs(OutputType.FILE);
		
		String destination = System.getProperty("user.dir")+"/FailedTestsScreenshots/"+screenShotName+dateName+
				".png";
		
		File finalDestination = new File(destination);
		
		FileUtils.copyFile(source, finalDestination);
		
		return destination;
	}
	
	@BeforeMethod
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "D:\\ChromeDriver\\chromedriver.exe");
		driver = new ChromeDriver();
		
		driver.manage().window().maximize();
		
		//PageLoadTimeOut and ImplicitWait could also be defined in the config.properties file
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		driver.get("https://ui.cogmento.com/");		
	}
	
	@Test(priority = 1)
	public void freeCrmTitleTest() {
		extentTest = extent.startTest("freeCrmTitleTest");
		
		String title = driver.getTitle();
		System.out.println("title-->"+title);
		Assert.assertEquals(title, "Cogmento CRMM","The title not as expected");
	}
	
	@Test(priority = 2)
	public void freeCrmSignUpTest() {
		extentTest = extent.startTest("freeCrmSignUpTest");
		
		boolean signUpBtn = driver.findElement(By.xpath("//a[contains(text(),'Sign Up')]")).isDisplayed();
		
		Assert.assertEquals(signUpBtn, false);
	}

	@AfterMethod
	public void tearDown(ITestResult result) throws IOException {
		if(result.getStatus()==ITestResult.FAILURE) {
			extentTest.log(LogStatus.FAIL, "Test Case Failed is-->"+result.getName()); //to add name in extent report
			extentTest.log(LogStatus.FAIL, "Error message is-->"+result.getThrowable()); //to add the error/exception in the exten report
			
			String screenshotPath = getScreenshot(driver, result.getName()); // to capture the screenshot
			
			extentTest.log(LogStatus.FAIL, extentTest.addScreenCapture(screenshotPath)); //to attach 
		}
		else if(result.getStatus()==ITestResult.SKIP) {
			extentTest.log(LogStatus.SKIP, "Test case skipped is-->"+ result.getName());
		}
		else if(result.getStatus()==ITestResult.SUCCESS) {
			extentTest.log(LogStatus.PASS, "Test Case Passed is-->"+result.getName());
		}
		
		extent.endTest(extentTest); //ending the test and prepare to create Extent HTML report
		
		driver.quit();
	}
	
	@AfterTest
	public void endReport() {
		extent.flush();
		extent.close();
	}
}
