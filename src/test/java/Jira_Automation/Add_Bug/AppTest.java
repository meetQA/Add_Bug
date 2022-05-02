package Jira_Automation.Add_Bug;




    
import utilities.*;
import static io.restassured.RestAssured.*;
import java.io.File;
import java.io.IOException;

import org.testng.Assert;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

public class AppTest {
	static ExcelUtils excelUtils = new ExcelUtils();
	static String excelFilePath =Constants.Path_TestData+Constants.File_TestData;
	
	public static void main(String[] args) throws IOException {
		excelUtils.setExcelFile(excelFilePath,"Sheet1");
    // TODO Auto-generated method stub
    RestAssured.baseURI="http://jira.sgligis.com:8080";
    //Login Scenario
    SessionFilter session=new SessionFilter();
    String response=given().relaxedHTTPSValidation().header("Content-Type","application/json").body("{    \"username\": \""+Constants.Username+"\",    \"password\": \""+Constants.Password+"\"}").log().all().filter(session).when().post("/rest/auth/1/session").then().log().all().extract().response().asString();
    //Add Bug
    System.out.println(excelUtils.getCellData(3,1));
   System.out.println(excelUtils.getRowCountInSheet());
   int j;
   for( j=2;j<excelUtils.getRowCountInSheet();j=j+2)
   {
	   if(excelUtils.getCellData(j,1).equals(""))
	   {
		   System.out.println(j);
		   break;
	   }  

   }
   for(int i=2;i<j;i=i+2)
   {
	   
    String summary = excelUtils.getCellData(i,1).replace("\"", "'");
    String description = excelUtils.getCellData(i,2).replace("\"", "'").replace("\n", " ");
    System.out.println(description);
    String response1=given().relaxedHTTPSValidation().header("Content-Type","application/json").body("{\r\n"
    		+ "    \"fields\": {\r\n"
    		+ "        \"project\": {\r\n"
    		+ "            \"key\": \"QAT\"\r\n"
    		+ "        },\r\n"
    		+ "        \"summary\":\""+summary+"\",\r\n"
    		+ "        \"description\" :\"" +description+"\",\r\n"
    		+ "        \"components\" : [ { \"name\": \""+Constants.Component+"\"}  ],\r\n"
    		+ "        \"priority\": {\r\n"
    		+ "\r\n"
    		+ "            \"name\": \""+excelUtils.getCellData(i,5)+"\"\r\n"
     		+ "        },\r\n"
     		+"\"assignee\": {\r\n"
     		+ "\"name\": \""+Constants.Assignee+"\"\r\n"
     		+ "},"
    		+ "        \"issuetype\": {\r\n"
    		+ "\r\n"
    		+ "            \"name\": \"Bug\"\r\n"
    		+ "        }\r\n"
    		+ "    }\r\n"
    		+ "}").log().all().filter(session).when().post("/rest/api/2/issue").then().log().all().assertThat().statusCode(201).extract().response().asString();
    JsonPath js=new JsonPath(response1);

    String commentId= js.getString("key");
    System.out.println(commentId);
    
    
    String commentId2= js.getString("id");
    System.out.println(commentId2);
    
  //Add Attachment
     String attachment = excelUtils.getCellData(i,4);
     System.out.println(commentId2);
     
    given().header("X-Atlassian-Token","no-check").filter(session).pathParam("key", commentId)

    .header("Content-Type","multipart/form-data")

    .multiPart("file",new File(Constants.Path_TestData+attachment)).when().

    post("rest/api/2/issue/{key}/attachments").then().log().all().assertThat().statusCode(200);
    
  //link issue 
    
    given().relaxedHTTPSValidation().header("Content-Type","application/json").body("{\r\n"
    		+ "   \"update\":{\r\n"
    		+ "      \"issuelinks\":[\r\n"
    		+ "         {\r\n"
    		+ "            \"add\":{\r\n"
    		+ "               \"type\":{\r\n"
    		+ "                  \"name\":\"Blocks\",\r\n"
    		+ "                  \"inward\":\"is blocked by\",\r\n"
    		+ "                  \"outward\":\"blocks\"\r\n"
    		+ "               },\r\n"
    		+ "               \"outwardIssue\":{\r\n"
    		+ "                  \"key\":\""+excelUtils.getCellData(i,7)+"\"\r\n"
    		+ "               }\r\n"
    		+ "            }\r\n"
    		+ "         }\r\n"
    		+ "      ]\r\n"
    		+ "   }\r\n"
    		+ "}").log().all().filter(session).when().put("/rest/api/2/issue/"+commentId2).then().log().all().assertThat().statusCode(204).extract().response().asString();
    
    excelUtils.setCellValue(i,8,commentId,excelFilePath);
    }
    }
}	
    
	
	


