package com.atlassian.jiraApi.utility;

import java.util.HashMap;
import java.util.Map;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("username", "dddd");
		data.put("password", "1234");
		JiraApiUtility.updateJsonFile(System.getProperty("user.dir")+"//resources//JSONData//Login.json", data);
	}

}
