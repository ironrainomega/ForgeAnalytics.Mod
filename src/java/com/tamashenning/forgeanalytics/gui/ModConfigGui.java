package com.tamashenning.forgeanalytics.gui;

import java.util.List;
import java.util.Map;

import com.tamashenning.forgeanalytics.ForgeAnalyticsMod;
import com.tamashenning.forgeanalytics.client.ForgeAnalyticsConstants;
import com.tamashenning.forgeanalytics.models.AnalyticsModel;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ModConfigGui extends GuiConfig {

	public ModConfigGui(GuiScreen guiScreen) {
		super(guiScreen, ModConfigGui.getConfigElements(), ForgeAnalyticsMod.MODID, false, false,
				"Analytics event data");
	}

	public static List<IConfigElement> getConfigElements() {
		// new
		// ConfigElement(ForgeAnalyticsConstants.dataConfig.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements()

		AnalyticsModel am = ForgeAnalyticsMod.proxiedClient.CreateKeepAlivePing();
		if (!ForgeAnalyticsMod.proxiedClient.FireEvent()) {
			// Snooper is off...
			ForgeAnalyticsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, "Snooper", "Off");
			return new ConfigElement(ForgeAnalyticsConstants.dataConfig.getCategory(Configuration.CATEGORY_GENERAL))
					.getChildElements();
		}
		am.Properties.putAll(ForgeAnalyticsConstants.CustomProperties);

		ForgeAnalyticsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, "PartitionKey", am.PartitionKey);
		ForgeAnalyticsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, "ClientDateTimeEpoch",
				am.ClientDateTimeEpoch);
		ForgeAnalyticsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, "Table", am.Table);

		for (Map.Entry<String, String> entry : am.Properties.entrySet()) {
			ForgeAnalyticsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, entry.getKey(), entry.getValue());
			ForgeAnalyticsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, entry.getKey() + "_OptOut", false);
			// ForgeAnalyticsConstants.dataConfig.getBoolean(Configuration.CATEGORY_GENERAL,
			// entry.getKey()+"_optout", false);
		}

		ForgeAnalyticsConstants.dataConfig.save();
		return new ConfigElement(ForgeAnalyticsConstants.dataConfig.getCategory(Configuration.CATEGORY_GENERAL))
				.getChildElements();
	}
}
