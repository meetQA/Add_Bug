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
    String response=given().relaxedHTTPSValidation().header("Content-Type","application/json").body("{    \"username\": \"meet.g\",    \"password\": \"Nehmi@1210\"}").log().all().filter(session).when().post("/rest/auth/1/session").then().log().all().extract().response().asString();
    //Add Bug
    String response1=given().relaxedHTTPSValidation().header("Content-Type","application/json").body("{\r\n"
    		+ "    \"fields\": {\r\n"
    		+ "        \"project\": {\r\n"
    		+ "            \"key\": \"QAT\"\r\n"
    		+ "        },\r\n"
    		+ "        \"summary\":\""+excelUtils.getCellData(2,1)+"\",\r\n"
    		+ "        \"description\" : \""+excelUtils.getCellData(2,2)+"\" ,\r\n"
    		+ "        \"components\" : [ { \"name\": \""+Constants.Component+"\"}  ],\r\n"
    		+ "        \"priority\": {\r\n"
    		+ "\r\n"
    		+ "            \"name\": \""+excelUtils.getCellData(2,5)+"\"\r\n"
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
     String attachment = excelUtils.getCellData(2,4);
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
    		+ "                  \"key\":\""+excelUtils.getCellData(2,7)+"\"\r\n"
    		+ "               }\r\n"
    		+ "            }\r\n"
    		+ "         }\r\n"
    		+ "      ]\r\n"
    		+ "   }\r\n"
    		+ "}").log().all().filter(session).when().put("/rest/api/2/issue/"+commentId2).then().log().all().assertThat().statusCode(204).extract().response().asString();
    }
	
	
	
	
}

