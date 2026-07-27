package testBase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

public class BaseClass {

	public static WebDriver driver;
	public Logger logger;
	public Properties p;
	
	@BeforeClass(groups= {"Sanity","Regression","Master"})
	@Parameters({"os","browser"})
	public void setUp(String os, String br) throws IOException {
		
		//Loading config.properties file
		FileReader file = new FileReader("./src//test//resources//confi.properties");
		p = new Properties();
		p.load(file);
		
		logger = LogManager.getLogger(this.getClass());//log4j2 
		
		// Get execution environment
           String executionEnv = p.getProperty("execution_env");

       // Print execution environment
          System.out.println("Execution Environment : " + execution_env);
		
		//Remote execution
		if(p.getProperty("execution_env").equalsIgnoreCase("remote"))
		{
		DesiredCapabilities capabilities=new DesiredCapabilities();

		//os
		if(os.equalsIgnoreCase("windows")){
		capabilities.setPlatform(Platform.WIN11);
		}
		else if (os.equalsIgnoreCase("linux")){
			capabilities.setPlatform(Platform.LINUX);
			}
		else if (os.equalsIgnoreCase("mac")){
		capabilities.setPlatform(Platform.MAC);
		}
		else {
		System.out.println("No matching os");
		return;
		}
		
		//browser
		switch(br.toLowerCase()) {
		case "chrome": capabilities.setBrowserName("chrome"); break;
		case "edge" : capabilities.setBrowserName("MicrosoftEdge"); break;
		case "firefox" : capabilities.setBrowserName("firefox"); break;
		default : System.out.println("No matching browser"); break ;
		}
		
		driver = new RemoteWebDriver(URI.create("http://localhost:4444").toURL(), capabilities);//URL(" http://10.203.99.70:4444/wd/hub")
		
		}
		
		//Local execution
		if(p.getProperty("execution_env").equalsIgnoreCase("local")) {
		switch(br.toLowerCase()) {
		case "chrome": driver = new ChromeDriver(); break;
		case "edge" : driver = new EdgeDriver(); break;
		case "firefox" : driver = new FirefoxDriver(); break;
		default : System.out.println("Invalid driver"); return;
		}
		}
		System.out.println(driver.getClass().getName());
		
		
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get(p.getProperty("appURL"));// Reading url from properties file
	}
	
	@AfterClass(groups= {"Sanity","Regression","Master"})
	public void tearDown() {
		driver.quit();
	}
	
	public String randomString() {
		String generatedstring = RandomStringUtils.secure().nextAlphabetic(5);
		return generatedstring;
	}
	
	public String randomNumber() {
		String generatednumber = RandomStringUtils.secure().nextNumeric(10);
		return generatednumber;
	}
	
	public String randomAlphaNumberic() {
		String generatedstring = RandomStringUtils.secure().nextAlphabetic(3);
		String generatednumber = RandomStringUtils.secure().nextNumeric(3);
		return(generatedstring+"@"+generatednumber);
	}
	
	public String captureScreen(String tname)throws IOException {
		String timestamp = new SimpleDateFormat("yymmddhhmmss").format(new Date());
		
		TakesScreenshot takesScreenshot = (TakesScreenshot)driver;
		File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);

		String targetFilePath = System.getProperty("user.dir")+"\\screenshots\\"+tname+"_"+timestamp+".png";
		File targetFile = new File(targetFilePath);
		sourceFile.renameTo(targetFile);
		return targetFilePath;
	}
}
