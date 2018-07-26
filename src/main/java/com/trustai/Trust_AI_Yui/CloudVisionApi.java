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
	public Object run(Model model, MultipartFile multipartFile, String featureType) throws Exception{ 

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
		
		// リクエストを送るためのJSONを作成
		ManagementJson MJson = new ManagementJson();
		String json = MJson.CloudVisionApiJson(base64Image, featureType, 1);
		
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

		// Resultの初期化
		String result = null;
		
		// 画像認識のタイプで分岐（JSONから値を取り出す）
		if (featureType.equals("LABEL_DETECTION")){
			result = resultJson.get("responses").get(0).get("labelAnnotations").get(0).get("description").asText();
		}else if (featureType.equals("IMAGE_PROPERTIES")){
			CloudVisionApiImageProperties color = new CloudVisionApiImageProperties();
			result = color.changeColor(resultJson);
		}else if (featureType.equals("TEXT_DETECTION")){
			CloudVisionOpticalCharacterRecognition ocr = new CloudVisionOpticalCharacterRecognition();
			result = ocr.textDetection(resultJson);
		}

		// Resultに結果をセット
		return model.addAttribute("result",result);
	}
}
