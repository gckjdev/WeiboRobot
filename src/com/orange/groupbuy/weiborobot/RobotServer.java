package com.orange.groupbuy.weiborobot;

import java.text.ParseException;
import java.util.Timer;

import com.orange.common.snsrequest.SNSRequest;
import com.orange.common.snsrequest.SinaWeiboSNS;

public class RobotServer {
	
    public static void main(final String[] args) throws ParseException {
    	
    	Timer weiboTaskTimer = new Timer();
    	WeiboSenderTimerTask morningWeiboSender = new WeiboSenderTimerTask();
    	WeiboSenderTimerTask noonWeiboSender = new WeiboSenderTimerTask();
    	WeiboSenderTimerTask niteWeiboSender = new WeiboSenderTimerTask();
    	
    	// TODO use log4j
    	System.out.println("timer start\n");

    	// TODO need to align the Chinese Time, in server the time is UTC instead of Chinese Standard Time
    	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("M/dd/yyyy hh:mm:ss a",java.util.Locale.US); 
    	java.util.Date dateMorning = formatter.parse("9/11/2011 8:30:00 AM");  
    	java.util.Date dateNoon = formatter.parse("9/11/2011 11:59:59 AM");
    	java.util.Date dateNite = formatter.parse("9/11/2011 6:00:00 PM");

    	weiboTaskTimer.schedule(morningWeiboSender, dateMorning, 1000*60*60*24);
    	weiboTaskTimer.schedule(noonWeiboSender, dateNoon, 1000*60*60*24);
    	weiboTaskTimer.schedule(niteWeiboSender, dateNite, 1000*60*60*24);
    	
    	
    }
    
}
