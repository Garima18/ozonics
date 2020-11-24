package com.ozonics.api;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.verify.v2.Service;
import com.twilio.type.PhoneNumber;

public class TwilioSms {
//
//    public static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
//    public static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
    
	
	 public static final String ACCOUNT_SID =
	            "ACfef6ff34d5e2429bef2afa0f6aeaa4d5";
	    public static final String AUTH_TOKEN =
	            "8643e78ed43752503e47b7318f7dbe03";

    public String sendMsgViaTwilio(String otp, String phone_num) {
    	System.out.println("Twilio Running");
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    try {
    	String text ="Your Ozonics Account login OTP is "+otp;
    	System.out.println("message:"+text);
    Message message = Message
            .creator(new PhoneNumber("+"+phone_num), // to
                    new PhoneNumber("+17147865716"), // from
                    text)
            .create();
    
    System.out.println(message.getSid());
    return "1";
    }
    catch(Exception e) {
    	e.printStackTrace();
    	return "0";
    }
//    Service service = Service.creator("My First Verify Service").create();
//    System.out.println(service.getSid());

    
    }
    
}
