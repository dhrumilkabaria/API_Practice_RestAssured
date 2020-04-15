package com.google.googlePlaceApi.testSuites;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.googlePlaceApi.common.BaseTestCase;
import com.google.googlePlaceApi.utils.GooglePlaceApiUtility;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestSuite1 extends BaseTestCase{

	@Test(priority = 0)
	public void addPlace()
	{
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().log().all();
		requestSpecification.queryParam("key", "qaclick123");
		requestSpecification.body(GooglePlaceApiUtility.getJsonObjectForJsonFile(System.getProperty("user.dir")+"//resources//JSONData//AddPlace.json")).log().all();
		
		Response response = requestSpecification.request(Method.POST, propertiesMap.get("googlePlace.addPlace.Uri"));
		
		Assert.assertEquals(response.getStatusCode(), 200);
		
		String responseBodyString = response.body().asString();
		System.out.println("Add Place Response Body: "+responseBodyString);
		
		JsonPath jsonPath = response.jsonPath();
		Assert.assertTrue(jsonPath.getString("status").equals("OK"), "Response status is not OK");
		Assert.assertTrue(jsonPath.getString("scope").equals("APP"), "Response scope is not APP");
		Assert.assertTrue(responseBodyString.contains("place_id"), "Place Id is not present in the response");
		
		String placeId = jsonPath.getString("place_id");
		propertiesMap.put("place_id", placeId);
		
		getPlacePositive();
	}
	
	@Test(dependsOnMethods = "addPlace", priority = 1)
	public void getPlacePositive()
	{
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().log().all();
		HashMap<String, String> parametersMap = new HashMap<String, String>();
		parametersMap.put("key", "qaclick123");
		parametersMap.put("place_id", propertiesMap.get("place_id"));
		requestSpecification.queryParams(parametersMap);
		Response response = requestSpecification.request(Method.GET, propertiesMap.get("googlePlace.getPlace.Uri"));
		
		Assert.assertEquals(response.getStatusCode(), 200);
		
		String responseBodyString = response.body().asString();
		System.out.println("Get Place Response Body: "+responseBodyString);
	}
	
	@Test
	public void getPlaceNegative()
	{
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().log().all();
		HashMap<String, String> parametersMap = new HashMap<String, String>();
		parametersMap.put("key", "qaclick123");
		parametersMap.put("place_id", "WrongPlaceId");
		requestSpecification.queryParams(parametersMap);
		Response response = requestSpecification.request(Method.GET, propertiesMap.get("googlePlace.getPlace.Uri"));
		int statusCode = response.getStatusCode();
		System.out.println("Status Code: "+statusCode);
		
		Assert.assertTrue(statusCode == 404, "Get Place response status code for wrong place id should be 404");
		System.out.println("Get Place Negative Body: "+response.asString());
		
		JsonPath jsonPath = response.jsonPath();
		String message = jsonPath.getString("msg");
		Assert.assertEquals(message, "Get operation failed, looks like place_id  doesn't exists");
		
	}
	
	@Test(dependsOnMethods = "addPlace", priority = 2)
	public void updatePlace()
	{
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().log().all();
		HashMap<String, String> parametersMap = new HashMap<String, String>();
		parametersMap.put("key", "qaclick123");
		parametersMap.put("place_id", propertiesMap.get("place_id"));
		requestSpecification.queryParams(parametersMap);
		
		
		String updateJsonString = GooglePlaceApiUtility.readFileAsString(System.getProperty("user.dir")+"//resources//JSONData//UpdatePlace.json");
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, String> updateJsonMap = new HashMap<String, String>();
		try {
			updateJsonMap = objectMapper.readValue(updateJsonString, Map.class);
			updateJsonMap.put("place_id", propertiesMap.get("place_id"));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		requestSpecification.body(updateJsonMap);
		
		Response response = requestSpecification.request(Method.PUT, propertiesMap.get("googlePlace.updatePlace.Uri"));
		
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	
	@Test(dependsOnMethods = "addPlace", priority = 3)
	public void deletePlace()
	{
		RequestSpecification requestSpecification = RestAssured.given().relaxedHTTPSValidation().log().all();
		requestSpecification.queryParam("key", "qaclick123");
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		String deleteJsonString = GooglePlaceApiUtility.readFileAsString(System.getProperty("user.dir")+"//resources//JSONData//DeletePlace.json");
		Map<String, String> payloadMap = new HashMap<String, String>();
		try {
			payloadMap = objectMapper.readValue(deleteJsonString,  Map.class);
			payloadMap.put("place_id", propertiesMap.get("place_id"));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		requestSpecification.body(payloadMap);
		
		Response response = requestSpecification.request(Method.DELETE, propertiesMap.get("googlePlace.deletePlace.Uri"));
		
		Assert.assertEquals(response.getStatusCode(), 200);
	}
}
