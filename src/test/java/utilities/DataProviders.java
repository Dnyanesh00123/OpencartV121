package utilities;

import java.io.IOException;

import org.testng.annotations.DataProvider;

public class DataProviders {

	//DataProvider1
	@DataProvider(name = "loginData")
	public String [][] getData() throws IOException{
		
		String path = ".\\testData\\OpenCart_login_data.xlsx";//taking excel file from testdata
		ExcelUtility xlutil = new ExcelUtility(path);// Creating an object for xlutility
		
		int totalrows = xlutil.getRowCount("sheet1");
		int totalcols = xlutil.getCellCount("sheet1", 1);
		
		String logindata [][] = new String[totalrows][totalcols];// created for two dimention array which can store
		
		for(int r = 1; r <= totalrows; r++) // read the data from xl stored in two dimentional array
		{
			for(int c = 0; c < totalcols; c++) // r is rows and c is columns
			{
				logindata[r-1][c] = xlutil.getCellData("sheet1", r, c);//1,0
			}
		}
		return logindata;// returning two dimention array
	}
	
	
	//DataProvider2
	//DataProvider3
	//DataProvider4
	
}
