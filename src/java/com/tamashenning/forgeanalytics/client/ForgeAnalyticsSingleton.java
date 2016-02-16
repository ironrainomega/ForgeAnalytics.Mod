package com.tamashenning.forgeanalytics.client;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Timer;
import java.util.TimerTask;

import com.tamashenning.forgeanalytics.AnalyticsClient;

import net.minecraft.client.Minecraft;

public class ForgeAnalyticsSingleton {
	private static ForgeAnalyticsSingleton instance = null;
	private AnalyticsClient ac = new AnalyticsClient();

	public String SessionID = "";

	protected ForgeAnalyticsSingleton() {
		SessionID = this.CreateID();
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
			id = ac.Anonymize(new BigInteger(130, random).toString(32));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return id;
	}

	public void StartKeepAliveTimer() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					// I'm running locally with no world loaded yet...
					if (Minecraft.getMinecraft().theWorld == null) {
						ac.UploadModel(ac.CreateClientKeepAlivePing());
					} else if (!Minecraft.getMinecraft().theWorld.isRemote) {
						ac.UploadModel(ac.CreateClientKeepAlivePing());
					} else {
						ac.UploadModel(ac.CreateServerKeepAlivePing());
					}
				} catch (Exception e) {
					//
					e.printStackTrace();
				}
				;
			}
		}, ForgeAnalyticsConstants.KEEPALIVETIME, ForgeAnalyticsConstants.KEEPALIVETIME);
	}

}
