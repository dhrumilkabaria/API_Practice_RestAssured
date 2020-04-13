package com.google.googlePlaceApi.testSuites;

import org.testng.annotations.Test;

import com.google.googlePlaceApi.common.BaseTestCase;
import com.google.googlePlaceApi.utils.GooglePlaceApiUtility;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestSuite1 extends BaseTestCase{

	@Test
	public void addPlace()
	{
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation();
		requestSpecification.queryParam("key", "qaclick123");
		requestSpecification.body(GooglePlaceApiUtility.getJsonObjectForJsonFile(System.getProperty("user.dir")+"//resources//JSONData//AddPlace.json"));
		
		Response response = requestSpecification.request(Method.POST, propertiesMap.get("googlePlace.addPlace.Uri"));
		
		System.out.println("Status Code: "+response.getStatusCode());
		
	}
}
