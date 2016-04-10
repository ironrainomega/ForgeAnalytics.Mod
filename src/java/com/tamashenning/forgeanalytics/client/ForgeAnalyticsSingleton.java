package com.tamashenning.forgeanalytics.client;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import com.tamashenning.forgeanalytics.ForgeAnalyticsMod;

public class ForgeAnalyticsSingleton {
	private static ForgeAnalyticsSingleton instance = null;
	private Timer timer = new Timer();

	public String SessionID = "";
	public UUID SessionUUID;

	protected ForgeAnalyticsSingleton() {
		SessionID = this.CreateID();
		SessionUUID = UUID.randomUUID();
	}

	public static ForgeAnalyticsSingleton getInstance() {
		if (instance == null) {
			instance = new ForgeAnalyticsSingleton();
		}

		return instance;
	}

	public String CreateID() {
		String id = "";
		SecureRandom random = new SecureRandom();
		try {
			id = ForgeAnalyticsMod.proxiedClient.Anonymize(new BigInteger(130, random).toString(32));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return id;
	}

	public void StartKeepAliveTimer(final boolean isClient) {
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				
				try {
					ForgeAnalyticsMod.proxiedClient.UploadModel(ForgeAnalyticsMod.proxiedClient.CreateKeepAlivePing(), isClient);
				} catch (Exception e) {
					//
					e.printStackTrace();
				}

			}
		}, ForgeAnalyticsConstants.KEEPALIVETIME, ForgeAnalyticsConstants.KEEPALIVETIME);
	}

	public void CancelTimer() {
		if (timer != null) {
			timer.cancel();
		}
	}
}
