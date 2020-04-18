package com.atlassian.jiraApi.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.atlassian.jiraApi.utility.JiraApiUtility;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BaseTestCase {

	public static HashMap<String, String> properetiesMap = new HashMap<String, String>();
	public static SessionFilter session = new SessionFilter();
	public static Map<String, String> headers = new HashMap<String, String>();
	
	@BeforeSuite
	public void init()
	{
		Properties properties = new Properties();
		
		File file = new File(System.getProperty("user.dir")+"//resources//application.properties");
		try {
			properties.load(new FileInputStream(file));
			for (Map.Entry<Object, Object> property : properties.entrySet()) {
				properetiesMap.put(property.getKey().toString(), property.getValue().toString());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("File not found at "+file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("File could not be accessed at "+file.getAbsolutePath());
		}
	}
	
	@BeforeTest
	public void loginToJira()
	{
		RestAssured.baseURI = properetiesMap.get("jiraApi.BaseUri");
		RequestSpecification requestSpecification = RestAssured.given().log().ifValidationFails().filter(session);
		
		requestSpecification.header("Content-Type", "application/json");
		
		Map<String, Object> userDetails = new HashMap<String, Object>();
		userDetails.put("username", "dhrumilkabaria");
		userDetails.put("password", "2409dhrumil");
		
		JiraApiUtility.updateJsonFile(JiraApiUtility.getJsonFilePath("Login.json"), userDetails);
		requestSpecification.body(JiraApiUtility.getJsonObjectForJsonFile(JiraApiUtility.getJsonFilePath("Login.json")));
		
		Response response = requestSpecification.request(Method.POST, properetiesMap.get("login.Uri")).then().log().ifValidationFails().extract().response();
		Assert.assertTrue(response.getStatusCode()==200, "Login failed. Session is not generated.");
		
		JsonPath jsonPath = response.jsonPath();
		headers.put("Cookie", jsonPath.getString("session.name")+"="+jsonPath.getString("session.value"));
	}
}
