package com.tamashenning.forgeanalytics.proxies;

import com.tamashenning.forgeanalyitcs.client.ForgeAnalyticsConstants;
import com.tamashenning.forgeanalyitcs.client.ForgeAnalyticsSingleton;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
    	Configuration config = new Configuration(e.getSuggestedConfigurationFile());
    	config.load();
    	
    	ForgeAnalyticsConstants.AdID = ForgeAnalyticsSingleton.getInstance().CreateID();
    	
    	ForgeAnalyticsConstants.pingClientTable = config.get(Configuration.CATEGORY_GENERAL, "pingClientTable", ForgeAnalyticsConstants.pingClientTable).getString();
    	ForgeAnalyticsConstants.pingClientStartCommand = config.get(Configuration.CATEGORY_GENERAL, "pingClientStartCommand", ForgeAnalyticsConstants.pingClientStartCommand).getString();
    	ForgeAnalyticsConstants.pingServerTable = config.get(Configuration.CATEGORY_GENERAL, "pingServerTable", ForgeAnalyticsConstants.pingServerTable).getString();
    	ForgeAnalyticsConstants.pingServerStartCommand = config.get(Configuration.CATEGORY_GENERAL, "pingServerStartCommand", ForgeAnalyticsConstants.pingServerStartCommand).getString();
    	ForgeAnalyticsConstants.pingServerStopCommand = config.get(Configuration.CATEGORY_GENERAL, "pingServerStopCommand", ForgeAnalyticsConstants.pingServerStopCommand).getString();
    	ForgeAnalyticsConstants.pingClientKeepAlive = config.get(Configuration.CATEGORY_GENERAL, "pingClientKeepAlive", ForgeAnalyticsConstants.pingClientKeepAlive).getString();
    	ForgeAnalyticsConstants.pingServerKeepAlive = config.get(Configuration.CATEGORY_GENERAL, "pingServerKeepAlive", ForgeAnalyticsConstants.pingServerKeepAlive).getString();
    	
    	ForgeAnalyticsConstants.serverUrl = config.get(Configuration.CATEGORY_GENERAL, "serverUrl", ForgeAnalyticsConstants.serverUrl).getString();
    	ForgeAnalyticsConstants.AdID = config.get(Configuration.CATEGORY_GENERAL, "AdID", ForgeAnalyticsConstants.AdID).getString();
    	
    	ForgeAnalyticsConstants.HASHCOUNT = config.get(Configuration.CATEGORY_GENERAL, "HASHCOUNT", ForgeAnalyticsConstants.HASHCOUNT).getInt();
    	ForgeAnalyticsConstants.KEEPALIVETIME = config.get(Configuration.CATEGORY_GENERAL, "KEEPALIVETIME", ForgeAnalyticsConstants.KEEPALIVETIME).getInt();
    	
    	config.save();
    }

    public void init(FMLInitializationEvent e) {
    	// Add custom properties to each event
    	// ForgeAnalyticsConstants.CustomProperties.put("hello", "world");
    }

    public void postInit(FMLPostInitializationEvent e) {
    	ForgeAnalyticsSingleton.getInstance().StartKeepAliveTimer();
    }
}