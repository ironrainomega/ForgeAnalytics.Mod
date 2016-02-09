package com.tamashenning.forgeanalyitcs.client;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.tamashenning.forgeanalytics.AnalyticsClient;

public class ForgeAnalyticsSingleton {
	private static ForgeAnalyticsSingleton instance = null;
	private AnalyticsClient ac = new AnalyticsClient();
	
	public String SessionID = "";
	
	protected ForgeAnalyticsSingleton() {
		SecureRandom random = new SecureRandom();
		try {
			SessionID = ac.Anonymize(new BigInteger(130, random).toString(32));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ForgeAnalyticsSingleton getInstance() {
		if (instance == null) {
			instance = new ForgeAnalyticsSingleton();
		}
		
		return instance;
	}
	
}
