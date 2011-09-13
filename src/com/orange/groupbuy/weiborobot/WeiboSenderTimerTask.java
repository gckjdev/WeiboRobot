package com.orange.groupbuy.weiborobot;

import java.util.List;
import java.util.TimerTask;

import weibo4j.Weibo;

import com.orange.common.api.service.CommonService;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.common.snsrequest.SNSRequest;
import com.orange.common.snsrequest.SinaWeiboSNS;
import com.orange.groupbuy.api.service.FindProductByTopScoreService;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.Product;
import com.orange.groupbuy.manager.ProductManager;

public class WeiboSenderTimerTask extends TimerTask {

	@Override
	public void run() {
		
		// TODO change to static member of class  
		MongoDBClient mongoClient = new MongoDBClient(DBConstants.D_GROUPBUY);
		
		// TODO change to static member of class, also upper case
		String SINA_APP_KEY = "1528146353";
		String SINA_APP_SECRET  = "4815b7938e960380395e6ac1fe645a5c";
		
		// TODO change to static member of class, also upper case
		String oauth_token="2fea4cdad641ef8fcf493f52c401bb60" ;
		String oauth_secret="8e7cf81d0c920da686b8df9a9baec691" ;
		
		String city = "È«¹ú";
		int maxCount = 25;			
		int startOffset = 0;
		int startPrice = -100;		// TODO need some thinking on the features
		int endPrice = 10;			// TODO need some thinking on the features
		int category = -1;
		List<Product> productList = ProductManager.getTopScoreProducts(mongoClient, city, 
				category, startOffset, maxCount, startPrice, endPrice);
		
		// TODO check null pointer and size of list
		Product topProduct = productList.get(0);
		
		String titleText = topProduct.getTitle();
		String loc = topProduct.getLoc();
		String imageUrl = topProduct.getImage();
		
		WeiboMaker weibo = new WeiboMaker(SINA_APP_KEY, SINA_APP_SECRET, oauth_token, oauth_secret, titleText+loc, imageUrl);
		if (weibo.weiboCheck(titleText))
			weibo.weibosend();
		else
			// TODO use log4j instead of System.out.println
			// TODO message can be refined
			System.out.println("\n\n\n\nthis product had sent once,don't send it again!\n\n\n");


	}

}
