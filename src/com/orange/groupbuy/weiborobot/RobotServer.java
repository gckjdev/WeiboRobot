package com.orange.groupbuy.weiborobot;

import java.text.ParseException;
import java.util.Timer;

import org.apache.log4j.Logger;

public class RobotServer {
	
	public static final Logger log = Logger.getLogger(RobotServer.class
			.getName());
	
	public static final String CITY = "È«¹ú";
	public static final int MAC_COUNT = 25;			
	public static final int START_OFFSET = 0;
	public static final int START_PRICE = -100;		
	public static final int END_PRICE = 10;			
	public static final int CATEGORY = -1;
	
    public static void main(final String[] args) throws ParseException {
    	
    	Timer weiboTaskTimer = new Timer();
    	WeiboSenderTimerTask morningWeiboSender = new WeiboSenderTimerTask(CITY,MAC_COUNT,START_OFFSET,START_PRICE,END_PRICE,CATEGORY);
    	WeiboSenderTimerTask noonWeiboSender = new WeiboSenderTimerTask(CITY,MAC_COUNT,START_OFFSET,START_PRICE,END_PRICE,CATEGORY);
    	WeiboSenderTimerTask niteWeiboSender = new WeiboSenderTimerTask(CITY,MAC_COUNT,START_OFFSET,START_PRICE,END_PRICE,CATEGORY);
    	

    	// TODO use log4j
    	log.info("timer start");

    	// TODO need to align the Chinese Time, in server the time is UTC instead of Chinese Standard Time
    	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("M/dd/yyyy hh:mm:ss a",java.util.Locale.US); 
    	java.util.Date dateMorning = formatter.parse("9/11/2011 8:30:00 AM +0800");  
    	java.util.Date dateNoon = formatter.parse("9/11/2011 11:59:59 AM +0800");
    	java.util.Date dateNite = formatter.parse("9/11/2011 7:00:00 PM +0800");

    	weiboTaskTimer.schedule(morningWeiboSender, dateMorning, 1000*60*60*24);
    	weiboTaskTimer.schedule(noonWeiboSender, dateNoon, 1000*60*60*24);
    	weiboTaskTimer.schedule(niteWeiboSender, dateNite, 1000*60*60*24);
    	
    	
    }
    
}
