package com.orange.groupbuy.weiborobot;

import java.util.List;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.Product;
import com.orange.groupbuy.manager.ProductManager;

public class WeiboSenderTimerTask extends TimerTask {
	
	private static final MongoDBClient mongoClient = new MongoDBClient(DBConstants.D_GROUPBUY);
	private static final Logger log = Logger.getLogger(WeiboSenderTimerTask.class.getName());

	private static final String SINA_APP_KEY = "1528146353";
	private static final String SINA_APP_SECRET  = "4815b7938e960380395e6ac1fe645a5c";

	private static final String oauth_token="2fea4cdad641ef8fcf493f52c401bb60" ;
	private static final String oauth_secret="8e7cf81d0c920da686b8df9a9baec691" ;
	
	String city;
	int maxCount;			
	int startOffset;
	int startPrice;	
	int endPrice;	
	int category;

	@Override
	public void run() {
		
		Product topProduct = null;
		String titleText = null;
		String loc = null;
		String imageUrl = null;
	
		List<Product> productList = ProductManager.getTopScoreProducts(mongoClient, city, 
				category, startOffset, maxCount, startPrice, endPrice);
		
		if(productList != null && productList.size() != 0)
		{
			topProduct = productList.get(0);		
			titleText = topProduct.getTitle();
			loc = topProduct.getLoc();
			imageUrl = topProduct.getImage();
		}
		
		WeiboMaker weibo = new WeiboMaker(SINA_APP_KEY, SINA_APP_SECRET, oauth_token, oauth_secret, titleText+loc, imageUrl);
		if(titleText == null)
			log.info("Get product failed ,weibo could not be published");	
		else if (weibo.weiboCheck(titleText))
			weibo.weibosend();
		else
			log.info("Product "+topProduct.getId()+"had sent once,don't send it again!");

			// TODO use log4j instead of System.out.println
			// TODO message can be refined
			



	}

	public WeiboSenderTimerTask(String city, int maxCount, int startOffset,
			int startPrice, int endPrice, int category) {
		super();
		this.city = city;
		this.maxCount = maxCount;
		this.startOffset = startOffset;
		this.startPrice = startPrice;
		this.endPrice = endPrice;
		this.category = category;
	}

}
