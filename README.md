# Trust-AI-Yui

* For programming work of the Trust-AI

## 変更が必要な箇所

* セキュリティの関係でAPIキーをソースに書いてないので開発の際にはPCの環境変数にAPIキーを登録してください。
  * [CloudVisionApi.java](https://github.com/iwamotoyst/Trust-AI-Yui/blob/master/src/main/java/com/trustai/Trust_AI_Yui/CloudVisionApi.java)

    ```
	  /**
	   * GoogleVision-DevKey
	   */
	  private final String Google_KEY = System.getenv("DevKey");
    ```

  * コマンドプロンプトで環境変数 "DevKey" を設定する

    ```
    C:\> setx DevKey "Input GoogleVision-DevKey"
    ```

## end