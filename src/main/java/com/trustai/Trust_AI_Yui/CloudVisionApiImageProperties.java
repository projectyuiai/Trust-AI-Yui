package com.trustai.Trust_AI_Yui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

public class CloudVisionApiImageProperties {


	/**
	 * @param resultJson
	 * @return String(Feelings)
	 */
	public String changeColor(JsonNode resultJson){

		// resultJsonからRGB値を取得
		ArrayList<Integer> colorlist = new ArrayList<Integer>();
		int size = resultJson.get("responses").get(0).get("imagePropertiesAnnotation").get("dominantColors").get("colors").size();

		for(int i = 0; i < size; i++) {
			try {
				colorlist.add(resultJson.get("responses").get(0).get("imagePropertiesAnnotation").get("dominantColors").get("colors").get(i).get("color").get("red").asInt());
				colorlist.add(resultJson.get("responses").get(0).get("imagePropertiesAnnotation").get("dominantColors").get("colors").get(i).get("color").get("green").asInt());
				colorlist.add(resultJson.get("responses").get(0).get("imagePropertiesAnnotation").get("dominantColors").get("colors").get(i).get("color").get("blue").asInt());
				double d = resultJson.get("responses").get(0).get("imagePropertiesAnnotation").get("dominantColors").get("colors").get(i).get("score").asDouble()*100;
				colorlist.add((int)d);
			}catch (Exception e){
				break;
			}
		}

		// 8色に変換したリストを作成する
		ArrayList<Integer> changeColoor = new ArrayList<Integer>();
		for (int no = 0; no < colorlist.size() - 1; no = no + 4) {
			for (int i = 0; i < 3; i++) {
				if (colorlist.get(no + i) >= 128) {
					changeColoor.add(192);
				} else {
					changeColoor.add(64);
				}
			}
			changeColoor.add(colorlist.get(no + 3));
		}
		System.out.println();
		System.out.println("変換後　：　" + changeColoor);

		// 色の名前を取得する
		try {
			// scoreを入れる配列の準備
			int cntscore[] = new int[8];

			// scoreを入れる配列の初期化
			for (int i = 0; i <= 7; i++) {
				cntscore[i] = 0;
			}

			String s;
			String list = null;
			File f = new File("files/rgbList/ColorList.csv");

			for (int no = 0; no < changeColoor.size(); no = no + 4) {

				int num = 0;

				BufferedReader br = new BufferedReader(new FileReader(f));

				// 行単位で読む
				while ((s = br.readLine()) != null) { // 1行ずつCSVファイルを読み込む
					String array[] = s.split(","); // カンマで分割して配列に入れる

					// 変更後のRGB値と数値化した感情が一致したものをlistに入れる
					if ((changeColoor.get(no).equals(Integer.valueOf((array[1]))))
							&& (changeColoor.get(no + 1).equals(Integer.valueOf((array[2]))))
							&& (changeColoor.get(no + 2).equals(Integer.valueOf((array[3]))))) {

						// scoreを加算
						cntscore[num] = cntscore[num] + changeColoor.get(no + 3);
						list = array[0] + " : " + array[4];
					}
					num++;

				}
				System.out.println(list);
				br.close();
				num = 0;
			}

			int maxline = 0;
			int score = 0;

			// csvの行番号取得
			for (int n = 0; n <= 7; n++) {
				if (score <= cntscore[n]) {
					score = cntscore[n];
					maxline = n;
				}
			}

			// 色の名前を入れる変数
//			String colorname = null;
			
			// 色の感情を入れる変数
			String colorfeelings = null;

			BufferedReader br = new BufferedReader(new FileReader(f));
			int i = 0;
			while ((s = br.readLine()) != null) {
				String array[] = s.split(",");
				if (i == maxline) {
//					colorname = array[0]; //　今は使用しないのでコメントアウト
					colorfeelings = array[4];
				}
				i++;
			}
			br.close();
			return colorfeelings;

		} catch (Exception e) {
			System.out.println(e);
			return "error";
		}

	}

}
