package com.java.jumbotail;
 import java.security.*;
 import java.math.*;

public class GenerateID {

	public static String execute(String data)throws Exception
    {
        MessageDigest m=MessageDigest.getInstance("MD5");
        m.update(data.getBytes(),0,data.length());
        System.out.println("MD5: "+new BigInteger(1,m.digest()).toString(16));
        return new BigInteger(1,m.digest()).toString(16);
    }
	
}
