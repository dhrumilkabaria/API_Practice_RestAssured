package com.google.googlePlaceApi.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.testng.annotations.BeforeSuite;

public class BaseTestCase {
	public static HashMap<String, String> propertiesMap = new HashMap<String, String>();
	
	@BeforeSuite
	public void init()
	{
		File resourcesFile = new File(System.getProperty("user.dir")+"\\resources\\resources.properties");
		
		Properties properties = new Properties();
		
		try {
			properties.load(new FileInputStream(resourcesFile));
			
			for (Map.Entry<Object, Object> propertySet : properties.entrySet()) {
				propertiesMap.put(propertySet.getKey().toString(), propertySet.getValue().toString());
			}
			
		} catch (FileNotFoundException e) {
			System.err.println(resourcesFile.getAbsolutePath()+" not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(resourcesFile.getAbsolutePath()+" could not be accessed");
			e.printStackTrace();
		}
	}
}
