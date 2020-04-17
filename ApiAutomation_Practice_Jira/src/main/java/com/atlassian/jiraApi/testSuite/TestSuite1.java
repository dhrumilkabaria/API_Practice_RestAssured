package com.atlassian.jiraApi.testSuite;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.atlassian.jiraApi.common.BaseTestCase;
import com.atlassian.jiraApi.utility.JiraApiUtility;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestSuite1 extends BaseTestCase{

	@Test(priority = 0)
	public void createIssue()
	{
		RequestSpecification requestSpecification = RestAssured.given().log().ifValidationFails().filter(session);
		requestSpecification.header("Content-Type", "application/json");
		
		Map<String, Object> issueDetail = new HashMap<String, Object>();
		issueDetail.put("summary", "Issue "+Math.random());
		JiraApiUtility.updateJsonFile(JiraApiUtility.getFilePath("CreateIssue.json"), issueDetail);
		
		requestSpecification.body(JiraApiUtility.getJsonObjectForJsonFile(JiraApiUtility.getFilePath("CreateIssue.json")));
		Response response = requestSpecification.request(Method.POST, properetiesMap.get("createIssue.Uri")).then().log().ifValidationFails().extract().response();
		
		Assert.assertTrue(response.getStatusCode()==201, "Issue could not be created. Response error code: "+response.getStatusCode());
		
		
	}
	
}
