package com.google.googlePlaceApi.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Reporter;

import io.restassured.response.Response;

public class GooglePlaceApiUtility {

	public static JSONObject getJsonObjectForJsonFile(String filePath) {
		FileReader fileReader = null;
		JSONObject jsonObject = null;
		try {
			fileReader = new FileReader(filePath);
		} catch (FileNotFoundException e) {
			Reporter.log("File not found at location: " + filePath);
		}
		JSONParser jsonParser = new JSONParser();
		try {
			try {
				jsonObject = (JSONObject) jsonParser.parse(fileReader);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return jsonObject;
	}
	
	private static JSONObject getJsonObject(String json) {
		JSONObject jsonObject = null;
		JSONParser jsonParser = new JSONParser();
		try {
			jsonObject = (JSONObject) jsonParser.parse(json);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	public static JSONObject getJsonObjectForRawJsonResponse(Response response)
	{
		return getJsonObject(response.asString());
	}
	
	public static String readFileAsString(String path)
	{
		String fileContent = null;
		try {
			fileContent = new String(Files.readAllBytes(Paths.get(path)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fileContent;
	}
}
