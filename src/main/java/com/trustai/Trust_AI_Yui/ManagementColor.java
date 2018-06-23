package com.trustai.Trust_AI_Yui;

import java.io.*;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

public class ManagementColor {
	final String[] blackFeelings = {"恐怖","男性的","格好いい","上品"};
	final String[] deepBlueFeelings = {"悲しみ","活発","安全","冷静","誠実","清潔","若い","爽やか","孤独","気品","厳粛","崇高","宇宙"};
	final String[] greenFeelings = {"信頼","安らぎ","平和","田舎","弱い自己出張","穏やかさ","調和","自然","平和","バランス","協調"};
	final String[] lightBlueFeelings = {"驚き"};
	final String[] redFeelings = {"怒り","激怒","生命力","活動的","情熱的","衝動的"};
	final String[] purpleFeelings = {"嫌悪","高級","不安","癒し","伝統","古典","不安","嫉妬"};
	final String[] yellowFeelings = {"喜び","明るさや希望","にぎやか","幸福","幼稚","未来","知恵","かわいい","未熟","軽快","カジュアル"};
	final String[] grayFeelings = {"調和","憂鬱","不安","過去","薄暗い","思い出"};

	/**
	 * @param resultJson
	 * @return String(Feelings)
	 */
	public String changeColor(JsonNode resultJson){
		final String[] tokenColorlist = {"123","65","22","0.0001","123","65","22","0.0003"};
		int red = 0;
		int green = 0;
		int blue = 0;

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

		// 8色に減色したリストを作成
		ArrayList<Integer> changeColoor = new ArrayList<Integer>();
		for(int no=0 ; no < colorlist.size()-1 ; no=no+4){
			for(int i = 0 ; i <3 ;i++){
				if(colorlist.get(no+i)>=128){
					changeColoor.add(192);
				}else {
					changeColoor.add(64);
				}
			}
			changeColoor.add(colorlist.get(no+3));
		}
		System.out.println("減色後　：　" + changeColoor);

		// 画像に含ている割合が一番高い色の名前を取得
		try {
			
			String s;
			int cnt = 0;
			String list = null;
			for(int no=0 ; no < changeColoor.size()-1 ; no=no+4){
				BufferedReader br = new BufferedReader( new FileReader("files/rgbList/RGB_List.csv") );
				//　1行単位
				while( (s = br.readLine()) != null ) {
					System.out.println(s);

					String array[] = s.split( "," ); 
					if( changeColoor.get(no).equals(array[1]) && 
							changeColoor.get(no+1).equals(array[2]) && 
							changeColoor.get(no+2).equals(array[3])) {
						list = array[0] + " : " + array[4];
						break;
					}

				}
				br.close();
				System.out.println(list);
			}
			
			

		} catch( Exception e )  {
			System.out.println( e );
		}



		return null;

	}

}
