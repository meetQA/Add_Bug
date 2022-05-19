package utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Read_Data {
	public static  String Path_TestData,File_TestData, project_code,Username,Password,Starting_Row;

	 public void data() throws IOException {

		 Path_TestData = Files.readAllLines(Paths.get("D:\\Automation Testing\\Demo\\Jira_Bug\\Demo_HTML\\bugdata.txt")).get(0) ;
		 File_TestData= Files.readAllLines(Paths.get("D:\\Automation Testing\\Demo\\Jira_Bug\\Demo_HTML\\bugdata.txt")).get(1) + ".xls";
		 project_code= Files.readAllLines(Paths.get("D:\\Automation Testing\\Demo\\Jira_Bug\\Demo_HTML\\bugdata.txt")).get(2);
		
		   Username = Files.readAllLines(Paths.get("D:\\Automation Testing\\Demo\\Jira_Bug\\Demo_HTML\\bugdata.txt")).get(3);
		   Password = Files.readAllLines(Paths.get("D:\\Automation Testing\\Demo\\Jira_Bug\\Demo_HTML\\bugdata.txt")).get(4);
		   Starting_Row = Files.readAllLines(Paths.get("D:\\Automation Testing\\Demo\\Jira_Bug\\Demo_HTML\\bugdata.txt")).get(5);
		    
	        System.out.println(Path_TestData);  
	        System.out.println(File_TestData);  
	        System.out.println( project_code);  
	       
	        System.out.println(Username);  
	        System.out.println(Password);   
	        System.out.println(Starting_Row );  
	    }  
	   
}
