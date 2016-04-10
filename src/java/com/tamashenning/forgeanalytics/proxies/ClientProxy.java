package com.tamashenning.forgeanalytics.proxies;

import com.tamashenning.forgeanalytics.ForgeAnalyticsMod;
import com.tamashenning.forgeanalytics.client.ForgeAnalyticsSingleton;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
	}

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
		
		try {
			ForgeAnalyticsMod.proxiedClient.UploadModel(ForgeAnalyticsMod.proxiedClient.CreateKeepAlivePing(), true);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ForgeAnalyticsSingleton.getInstance().StartKeepAliveTimer(true);

	}
}
