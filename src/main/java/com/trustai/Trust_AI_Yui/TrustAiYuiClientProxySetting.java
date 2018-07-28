package com.trustai.Trust_AI_Yui;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

public class TrustAiYuiClientProxySetting {

	/**
	 * @return httpClient
	 */
	public HttpClient getClientProxy(){
		
		// システム環境変数からプロキシの設定を取得
		// C:\> setx http_proxy "http://ユーザ名:パスワード@proxy.example.com:8080"
		final String httpProxy = System.getenv("http_proxy");
		
		// httpClientを初期化
		HttpClient httpClient = null;
		
		// プロキシの設定ありの場合
		if(httpProxy != null) {
			String[] proxyList = httpProxy.replaceAll("http://","").split("@");
			String userName = proxyList[0].split(":")[0];
			String password = proxyList[0].split(":")[1];
			String hostName = proxyList[1].split(":")[0];
			String portNo = proxyList[1].split(":")[1];
			HttpHost proxy = new HttpHost(hostName, Integer.parseInt(portNo));		
			CredentialsProvider credsProvider = new BasicCredentialsProvider();	
			credsProvider.setCredentials(new AuthScope(proxy),new UsernamePasswordCredentials(userName, password));	
			RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
			httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).setDefaultRequestConfig(config).build();
		// プロキシの設定なしの場合
		}else {
			httpClient = HttpClientBuilder.create().build();
		}
		
		// HTTPクライアントの設定をReturn
		return httpClient;
	}

}
