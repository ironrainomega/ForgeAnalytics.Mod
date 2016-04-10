package com.tamashenning.forgeanalytics.client;

import java.util.HashMap;

import com.tamashenning.forgeanalytics.events.AnalyticsEvent;
import com.tamashenning.forgeanalytics.models.AnalyticsModel;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AnalyticsClientSide extends AnalyticsCommon {
	
	@Override
	public AnalyticsModel CreateKeepAlivePing(){
		AnalyticsModel am = new AnalyticsModel();
		am.Table = ForgeAnalyticsConstants.pingClientTable;
		am.Properties = new HashMap<String, String>();
		am.PartitionKey = ForgeAnalyticsConstants.pingClientKeepAlive;
		am.ClientDateTimeEpoch = System.currentTimeMillis() / 1000L;
		am.Properties.putAll(this.getCommonValues());

		return am;

	}
	
	@Override
	public boolean FireEvent() {
		if (!Minecraft.getMinecraft().isSnooperEnabled()) {
			return false;
		}
		MinecraftForge.EVENT_BUS.post(new AnalyticsEvent(net.minecraftforge.fml.relauncher.Side.CLIENT));
		
		return true;
	}
}
