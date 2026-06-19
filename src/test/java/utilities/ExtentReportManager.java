package utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import testBase.BaseClass;

public class ExtentReportManager implements ITestListener {

	public ExtentSparkReporter sparkReporter;
	public ExtentReports extent;
	public ExtentTest test;
	String repName;
	
	public void onStart(ITestContext testcontext) {
	/*
	SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
	Date dt = new Date();
	String currentdatetimestamp = df.format(dt);
	*/	
	String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()); //Time stamp
	repName = "Test-Report-" + timestamp + ".html";
	sparkReporter = new ExtentSparkReporter(".\\reports\\" + repName);//specify location of the report 
	
	sparkReporter.config().setDocumentTitle("Opencart Automation report");//Title of report
	sparkReporter.config().setReportName("Opencart Functional testing");//Name of the report
	sparkReporter.config().setTheme(Theme.DARK);
	
	extent = new ExtentReports();	
	extent.attachReporter(sparkReporter);
	extent.setSystemInfo("Application", "Opencart");
	extent.setSystemInfo("Module", "Admin");
	extent.setSystemInfo("Sub module", "customer");
	extent.setSystemInfo(" User name", System.getProperty("user.name"));
	extent.setSystemInfo("Environment", "QA");
	
	String os = testcontext.getCurrentXmlTest().getParameter("os");
	extent.setSystemInfo("Operating system", os);
	
	String browser = testcontext.getCurrentXmlTest().getParameter("browser");
	extent.setSystemInfo("Browser", browser);
	
	List<String> includedgroups = testcontext.getCurrentXmlTest().getIncludedGroups();
	if(!includedgroups.isEmpty()) {
		extent.setSystemInfo("Groups", includedgroups.toString());
	}
	}
	
	public void onTestSuccess(ITestResult result) {
			 
			 test = extent.createTest(result.getTestClass().getName());// create ne entry in report
			 test.assignCategory(result.getMethod().getGroups());// to display groups in report
			 test.log(Status.PASS,result.getName()+"got successfully executed");
			  }
	
	public void onTestFailure(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());

		test.log(Status.FAIL,result.getName()+"got failed");
		test.log(Status.INFO, result.getThrowable().getMessage());

		try{
		String imgPath = new BaseClass().captureScreen(result.getName());
		test.addScreenCaptureFromPath(imgPath);

		} catch(IOException e1){
		e1.printStackTrace();
		}
	}
	
	public void onTestSkipped(ITestResult result){
		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.SKIP, result.getName()+"got skipped");
		test.log(Status.INFO, result.getThrowable().getMessage());
		}
	
	public void onFinish(ITestContext testContext){
		extent.flush();

		//Below code opnens the report on browser
		String pathOfExtentReport = System.getProperty("user.dir")+"\\reports\\"+repName;
		File extentReport = new File(pathOfExtentReport);

		try{
		Desktop.getDesktop().browse(extentReport.toURI());
		} catch(IOException e){
		e.printStackTrace();
		}
	 /*
	    try{
		URL url = new URL("file://"+System.getProperty("user.dir")+"\\reports\\"+repName);

		//Create the email message
		ImageHtmlEmail email = new ImageHtmlEmail();
		email.setDataSourceResolver(new DataSourceUrlResolver(url));
		email.setHostName("smtp.googlemail.com");
		email.setSmtpPort(465);
		email.setAuthenticator(new DefaultAuthenticator("ghongadednyan100@gmail.com","password"));
		email.setSSLOnConnect(true);
		email.setFrom("ghongadednyan100@gmail.com");//Sender
		email.setSubject("Test Results");
		email.setMsg("Please find attached report...");
		email.addTo("dnyaneshg.001@gmail.com");//Reciever
		email.attach(url, "extent report", "please check report...");
		email.send();// send the email
		}
		catch(Exception e){
		e.printStackTrace();
		}
		*/
}
}
