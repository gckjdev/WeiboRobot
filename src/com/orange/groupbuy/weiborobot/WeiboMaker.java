package com.orange.groupbuy.weiborobot;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import weibo4j.Status;

import com.orange.common.snsrequest.SNSRequest;
import com.orange.common.snsrequest.SinaWeiboSNS;
import com.orange.common.utils.http.HttpDownload;

public class WeiboMaker {
	
	public static final int REQUEST_Type_QQ_WEIBO = 1;
	public static final int REQUEST_Type_SINA_WEIBO = 2;
	public static final int REQUEST_Type_RENREN = 3;
	public static final String LOCAL_PATH = "D:/tmp/";
	
	String sinaAppKey ;
	String sinaAppSecret ;
	
	String oauthToken ;
	String oauthSecret;

	String text;
	String imageUrl;
	File imageFile;

	List<Status> userStatusList;
	SinaWeiboSNS weibo;
	SNSRequest weiboRequest;

	
	public WeiboMaker(String inputSinaAppKey, String inputSinaAppSecret,
			String inputOauthToken, String inputOauthSecret, String inputText,
			String inputImageUrl) {
		super();
		
		this.sinaAppKey = inputSinaAppKey;
		this.sinaAppSecret = inputSinaAppSecret;
		this.oauthToken = inputOauthToken;
		this.oauthSecret = inputOauthSecret;
		this.text = inputText;
		this.imageUrl = inputImageUrl;
		this.imageFile = getHttpImage();
		weibo = new SinaWeiboSNS(sinaAppKey, sinaAppSecret);
		
		weiboRequest = new SNSRequest(REQUEST_Type_SINA_WEIBO, inputText, this.imageFile, this.oauthToken, this.oauthSecret);
		
		this.userStatusList = weibo.getRecentWeibo(inputSinaAppKey, inputSinaAppSecret, weiboRequest, 20);
	}


	
	@Override
	public String toString() {
		return "WeiboMaker [SINA_APP_KEY=" + sinaAppKey
				+ ", SINA_APP_SECRET=" + sinaAppSecret + ", oauth_token="
				+ oauthToken + ", oauth_secret=" + oauthSecret + ", text="
				+ text + ", imageFile=" + imageUrl + ", userStatus="
				+ userStatusList + ", testWeibo=" + weibo + ", testReq="
				+ weiboRequest + "]";
	}
	

	
	public File getHttpImage() {
		File imageFile = null;
		String imageTempName = "tmp.jpg";
		try {
			imageTempName = URLEncoder.encode(this.imageUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(HttpDownload.downloadFile(this.imageUrl, LOCAL_PATH+imageTempName))
		{
			imageFile = new File(LOCAL_PATH+imageTempName);
		}
		if(imageFile.canRead())
			return imageFile;		
		else
			return null;
	}

	
	public boolean weiboCheck(String text){
		
		if(userStatusList != null)
		{
			for(Status status : userStatusList)
			{
				String getText = status.getText();
				int indexOfUrl = 0;
				indexOfUrl = getText.indexOf("http");
				if(indexOfUrl >=0)
					getText = getText.substring(0, indexOfUrl);
				if(getText == text)
					return false;
			}
		}
		
		return true;
	}
	
	public boolean weibosend(){
		weibo.publishWeibo(sinaAppKey, sinaAppSecret, weiboRequest);

		if(this.imageFile != null)
			this.imageFile.delete();

		return true;
	}

}
