package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;
import testBase.BaseClass;

public class TC001_AccountRegistrationTest extends BaseClass {


	@Test(groups={"Regression","Master"})
	public void verify_account_registration() throws InterruptedException {
		
		logger.info("***Starting TC001_AccountRegistrationTest***");
		
		try {
		HomePage hp = new HomePage(driver);
		hp.clickMyaccount();
		logger.info("Clicked on my account link");
		hp.clickRegister();
		logger.info("Clicked on register link");
		
		AccountRegistrationPage regpage = new AccountRegistrationPage(driver);
		logger.info("Providing customer details");
		regpage.setFirstName(randomString().toUpperCase());
		regpage.setLastName(randomString().toUpperCase());
		regpage.setEmail(randomString()+"@gmail.com");// randomly generate the email
		regpage.setTelephone(randomNumber());
		
		String password = randomAlphaNumberic();
		regpage.setPassword(password);
		regpage.setConfirmPassword(password);
		
		regpage.setPrivacyPolicy();
		regpage.clickOnContinue();
		
		logger.info("Validating expected message");
		String confmsg = regpage.getConfirmationMsg();
		
		if(confmsg.equals("Your Account Has Been Created!")) {
			Assert.assertTrue(true);
		} else {
			
			logger.error("Test failed");
			logger.debug("debug logs..");
			Assert.assertTrue(false);
		}
		
		Thread.sleep(4000);
		
	    }catch(Exception e) {
		
		Assert.fail();
	    }
		logger.info("***TC001_AccountRegistrationTest Finished***");
		
	}

}
