package com.tamashenning.forgeanalytics.proxies;

import java.util.HashMap;

import com.tamashenning.forgeanalytics.AnalyticsClient;
import com.tamashenning.forgeanalytics.models.AnalyticsModel;

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
        
        AnalyticsClient ac = new AnalyticsClient();
        AnalyticsModel am = new AnalyticsModel();
        am.ClientDateTimeEpoch = 5;
        am.PartitionKey = "PING";
        am.Table = "ClientAnalytics";
        am.Properties = new HashMap<String, String>();
        am.Properties.put("test", "test value");
        
        try {
			ac.UploadModel(am);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
}
