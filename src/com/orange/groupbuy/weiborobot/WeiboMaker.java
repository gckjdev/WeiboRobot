package com.orange.groupbuy.weiborobot;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import weibo4j.Status;

import com.orange.common.snsrequest.SNSRequest;
import com.orange.common.snsrequest.SinaWeiboSNS;
import com.orange.common.utils.http.HttpDownload;

public class WeiboMaker {
	
	String SINA_APP_KEY ;
	String SINA_APP_SECRET;
	
	String oauth_token;
	String oauth_secret;
	String text;
	String imageUrl;
	File imageFile;
	String LocalPath = "D:/tmp/";
	List<Status> userStatusList;
	SinaWeiboSNS weibo;
	SNSRequest weiboRequest;

	
	public WeiboMaker(String sINA_APP_KEY, String sINA_APP_SECRET,
			String oauth_token, String oauth_secret, String text,
			String imageUrl) {
		super();
		SINA_APP_KEY = sINA_APP_KEY;
		SINA_APP_SECRET = sINA_APP_SECRET;
		this.oauth_token = oauth_token;
		this.oauth_secret = oauth_secret;
		this.text = text;
		this.imageUrl = imageUrl;
		this.imageFile = getHttpImage();
		weibo = new SinaWeiboSNS(SINA_APP_KEY, SINA_APP_SECRET);
		weiboRequest = new SNSRequest(2, text, this.imageFile, this.oauth_token, this.oauth_secret);
		this.userStatusList = weibo.getRecentWeibo(sINA_APP_KEY, sINA_APP_SECRET, weiboRequest, 20);
	}


	
	@Override
	public String toString() {
		return "WeiboMaker [SINA_APP_KEY=" + SINA_APP_KEY
				+ ", SINA_APP_SECRET=" + SINA_APP_SECRET + ", oauth_token="
				+ oauth_token + ", oauth_secret=" + oauth_secret + ", text="
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
		
		if(HttpDownload.downloadFile(this.imageUrl, this.LocalPath+imageTempName))
		{
			imageFile = new File(this.LocalPath+imageTempName);
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
		weibo.publishWeibo(SINA_APP_KEY, SINA_APP_SECRET, weiboRequest);
		if(this.imageFile != null)
		this.imageFile.delete();
		return true;
	}

}
