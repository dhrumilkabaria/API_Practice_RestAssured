package com.atlassian.jiraApi.utility;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mozilla.javascript.json.JsonParser;
import org.testng.Reporter;

import io.restassured.response.Response;

public class JiraApiUtility {

	public String readFileAsString(String filePath)
	{
		String fileContent = null;
		try {
			fileContent = new String(Files.readAllBytes(Paths.get("adf")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileContent;		
	}
	
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
	
	public static void updateJsonFile(String filePath, Map<String, Object> data)
	{
		JSONObject jsonObject = getJsonObjectForJsonFile(filePath);
		for (Entry<String, Object> dataToWrite : data.entrySet()) {
			putJsonObject(jsonObject, dataToWrite.getKey(), dataToWrite.getValue());
		}
		
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(filePath);
			fileWriter.write(jsonObject.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not write into file "+filePath);
		}
		finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Could not write into file "+filePath);
			}
		}
	}
	
	private static void putJsonObject(JSONObject jsonObject, String key, Object value)
	{
		jsonObject.remove(key);
		jsonObject.put(key, value);
	}
	
	public static String getJsonFilePath(String jsonFileName) {
		return System.getProperty("user.dir")+"//resources//JSONData//"+jsonFileName;
	}
}
