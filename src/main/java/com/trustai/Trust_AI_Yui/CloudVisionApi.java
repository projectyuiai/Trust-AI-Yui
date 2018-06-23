package com.trustai.Trust_AI_Yui;

import java.net.URI;
import java.util.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CloudVisionApi {

	/**
	 * GoogleVision-DevKey 
	 */
	private final String Google_KEY = System.getenv("DevKey");

	/**
	 * CloudVisionApi is carried out
	 * @param model
	 * @param multipartFile
	 * @return Object
	 * @throws Exception
	 */
	public Object run(Model model, MultipartFile multipartFile) throws Exception{ 

		// ファイルが空の場合には画像選択画面に戻る
		if(multipartFile.isEmpty()){
			return model.addAttribute("result_error", "file is empty");
		}

		
		////////////////////
		// Cloud Vision API 
		////////////////////

		// エンドポイントの指定
		URIBuilder uriBuilder = new URIBuilder("https://vision.googleapis.com/v1/images:annotate");

		// RequestParameterの設定
		uriBuilder.addParameter("key", Google_KEY);
		URI uri = uriBuilder.build();
		HttpPost request = new HttpPost(uri);

		// RequestHeaderの設定
		request.setHeader("Content-Type", "application/json");

		// 画像をBase64に変換
		String base64Image = Base64.getEncoder().encodeToString(multipartFile.getBytes());

		// Typeの指定
		// 色の情報を得たい場合は"IMAGE_PROPERTIES"を使用
//		String type = "IMAGE_PROPERTIES";
		// ラベルを得たい場合は"LABEL_DETECTION"を使用
		String type = "LABEL_DETECTION";

		// リクエストを送るためのJSONを作成
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
				"            \"maxResults\":1  \n" + 
				"        }  \n" + 
				"       ]  \n" + 
				"     }  \n" + 
				"   ]  \n" + 
				" }";

		//RequestにJSONを追加
		StringEntity reqEntity = new StringEntity(json);
		request.setEntity(reqEntity);

		// 実行準備
		HttpClient httpClient = null;
		httpClient = HttpClientBuilder.create().build();

		// 実行
		HttpResponse response = httpClient.execute(request);

		// 結果取得
		HttpEntity entity = response.getEntity();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode resultJson = objectMapper.readTree(EntityUtils.toString(entity));

		// JSONから値を取り出す
		String result = null;

		// 分岐
		if (type == "LABEL_DETECTION"){
			result = resultJson.get("responses").get(0).get("labelAnnotations").get(0).get("description").asText();
		}else if (type == "IMAGE_PROPERTIES"){
			ManagementColor color = new ManagementColor();
			color.changeColor(resultJson);
		}

		// Resultに結果をセット
		return model.addAttribute("result",result);
	}
}
