package com.tamashenning.forgeanalytics.proxies;

import java.io.File;
import java.util.UUID;

import com.tamashenning.forgeanalytics.client.ForgeAnalyticsConstants;
import com.tamashenning.forgeanalytics.client.ForgeAnalyticsSingleton;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e) {
		Configuration config = new Configuration(e.getSuggestedConfigurationFile());
		Configuration config_local = new Configuration(
				new File(e.getModConfigurationDirectory().getParentFile(), "/local/local_forgeanalytics.cfg"));

		config.load();
		config_local.load();

		ForgeAnalyticsConstants.AdID = ForgeAnalyticsSingleton.getInstance().CreateID();
		ForgeAnalyticsConstants.InstanceUUID = UUID.randomUUID();

		ForgeAnalyticsConstants.pingClientTable = config
				.get(Configuration.CATEGORY_GENERAL, "pingClientTable", ForgeAnalyticsConstants.pingClientTable)
				.getString();
		ForgeAnalyticsConstants.pingClientStartCommand = config.get(Configuration.CATEGORY_GENERAL,
				"pingClientStartCommand", ForgeAnalyticsConstants.pingClientStartCommand).getString();
		ForgeAnalyticsConstants.pingServerTable = config
				.get(Configuration.CATEGORY_GENERAL, "pingServerTable", ForgeAnalyticsConstants.pingServerTable)
				.getString();
		ForgeAnalyticsConstants.pingServerStartCommand = config.get(Configuration.CATEGORY_GENERAL,
				"pingServerStartCommand", ForgeAnalyticsConstants.pingServerStartCommand).getString();
		ForgeAnalyticsConstants.pingServerStopCommand = config.get(Configuration.CATEGORY_GENERAL,
				"pingServerStopCommand", ForgeAnalyticsConstants.pingServerStopCommand).getString();
		ForgeAnalyticsConstants.pingClientKeepAlive = config
				.get(Configuration.CATEGORY_GENERAL, "pingClientKeepAlive", ForgeAnalyticsConstants.pingClientKeepAlive)
				.getString();
		ForgeAnalyticsConstants.pingServerKeepAlive = config
				.get(Configuration.CATEGORY_GENERAL, "pingServerKeepAlive", ForgeAnalyticsConstants.pingServerKeepAlive)
				.getString();

		ForgeAnalyticsConstants.serverUrl = config
				.get(Configuration.CATEGORY_GENERAL, "serverUrl", ForgeAnalyticsConstants.serverUrl).getString();

		ForgeAnalyticsConstants.HASHCOUNT = config
				.get(Configuration.CATEGORY_GENERAL, "HASHCOUNT", ForgeAnalyticsConstants.HASHCOUNT).getInt();
		ForgeAnalyticsConstants.KEEPALIVETIME = config
				.get(Configuration.CATEGORY_GENERAL, "KEEPALIVETIME", ForgeAnalyticsConstants.KEEPALIVETIME).getInt();

		ForgeAnalyticsConstants.modPack = config
				.get(Configuration.CATEGORY_GENERAL, "modPack", ForgeAnalyticsConstants.modPack).getString();

		/*
		 * Local configs
		 */

		ForgeAnalyticsConstants.AdID = config_local
				.get(Configuration.CATEGORY_GENERAL, "AdID", ForgeAnalyticsConstants.AdID).getString();

		ForgeAnalyticsConstants.InstanceUUID = UUID.fromString(config_local
				.get(Configuration.CATEGORY_GENERAL, "InstanceUUID", ForgeAnalyticsConstants.InstanceUUID.toString())
				.getString());

		/*
		 * Init local dummy config to show analytics values
		 */

		ForgeAnalyticsConstants.dataConfig = new Configuration(
				new File(e.getModConfigurationDirectory().getParentFile(), "/local/local_forgeanalytics_data.cfg"));

		config.save();
		config_local.save();

	}

	public void init(FMLInitializationEvent e) {
		// Add custom properties to each event
		// ForgeAnalyticsConstants.CustomProperties.put("hello", "world");
	}

	public void postInit(FMLPostInitializationEvent e) {
		
	}
}