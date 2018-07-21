package com.trustai.Trust_AI_Yui;

public class ManagementJson {
	
	/**
	 * This Method is Used in CloudVisionApi 
	 * 
	 * @param base64Image
	 * @param type
	 * @param maxResults
	 * @return String
	 */
	public String CloudVisionApiJson(String base64Image, String type, int maxResults){

		String json = "{  \n" + 
				"    \"requests\":\n" + 
				"     [  \n" + 
				"      {  \n" + 
				"       \"image\":\n" + 
				"         {  \n" + 
				"              \"content\":\"" + base64Image + "\"\n" +
				"         },  \n" + 
				"     \"features\":\n" + 
				"       [  \n" + 
				"        {  \n" + 
				"           \"type\":\""+type+"\",  \n" + 
				"            \"maxResults\":\""+maxResults+"\"  \n" + 
				"        }  \n" + 
				"       ]  \n" + 
				"     }  \n" + 
				"   ]  \n" + 
				" }";
		return json;
		
	}

}
