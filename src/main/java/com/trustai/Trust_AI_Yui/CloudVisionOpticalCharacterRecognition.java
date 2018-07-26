package com.trustai.Trust_AI_Yui;

import com.fasterxml.jackson.databind.JsonNode;

public class CloudVisionOpticalCharacterRecognition {
	
	/**
	 * @param resultJson
	 * @return String(text)
	 */
	public String textDetection(JsonNode resultJson){
		String text = resultJson.get("responses").get(0).get("textAnnotations").get(0).get("description").asText();
		System.out.println(text);
		return text;
	}

}
