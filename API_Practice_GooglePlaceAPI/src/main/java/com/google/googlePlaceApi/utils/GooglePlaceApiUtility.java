package com.google.googlePlaceApi.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Reporter;

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
}
