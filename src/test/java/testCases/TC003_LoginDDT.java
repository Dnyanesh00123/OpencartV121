package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;
import utilities.DataProviders;

public class TC003_LoginDDT extends BaseClass {

	
	@Test(dataProvider="loginData", dataProviderClass=DataProviders.class, groups="Datadriven")// getting data provider from different class
	public void verify_LoginDDT(String email,String psw,String exp) {

	logger.info("*** Starting TC003_LoginDDT ***");
	try {	
		//Thread.sleep(2000);
	//Home page
	HomePage hp = new HomePage(driver);
	hp.clickMyaccount();
	hp.clickLogin();
	
	Thread.sleep(2000);
	//Login page
	LoginPage lp = new LoginPage(driver);
	lp.setEmail(email);
	lp.setPassword(psw);
	lp.clickLogin();
	
	//My Account
	MyAccountPage macc = new MyAccountPage(driver);
	boolean targetpage = macc.isMyAccountPageExist();
	
	/*
	Data is valid - login success - test pass - logout
	       Data is valid - login failed - test fail

	Data is invalid - login success - test fail - logout
	       Data is invalid - login failed - test pass
	*/
	
	//First two conditions
	if(exp.equalsIgnoreCase("valid")) {
		if(targetpage==true){
			macc.clickLogout();
			Assert.assertTrue(true);
		} else {
			Assert.assertTrue(false);
		}
	}
	
	if(exp.equalsIgnoreCase("invalid")) {
		if(targetpage==true) {
			macc.clickLogout();
			Assert.assertTrue(false);
		} else {
			Assert.assertTrue(true);
		}
	}
	
	}catch(Exception e) {
		Assert.fail();
	}
	
	logger.info("*** Finished TC003_LoginDDT ***");
	
	}	
}
	
