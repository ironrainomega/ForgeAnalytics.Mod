package com.tamashenning.forgeanalytics;

import java.util.logging.Logger;

import com.tamashenning.forgeanalytics.proxies.CommonProxy;
import com.tamashenning.forgeanalytics.client.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;

@Mod(modid = ForgeAnalyticsMod.MODID, name = ForgeAnalyticsMod.MODNAME, version = ForgeAnalyticsMod.VERSION, guiFactory = ForgeAnalyticsMod.GUIFACTORY)
public class ForgeAnalyticsMod {

	public static final String MODID = "forgeanalytics";
	public static final String MODNAME = "Forge Analytics";
	public static final String VERSION = "0.0.0.16";
	public static final String GUIFACTORY = "com.tamashenning.forgeanalytics.gui.GuiFactory";

	@Instance(ForgeAnalyticsMod.MODID)
	public static ForgeAnalyticsMod instance;

	@SidedProxy(clientSide = "com.tamashenning.forgeanalytics.proxies.ClientProxy", serverSide = "com.tamashenning.forgeanalytics.proxies.ServerProxy")
	public static CommonProxy proxy;

	@SidedProxy(clientSide = "com.tamashenning.forgeanalytics.client.AnalyticsClientSide", serverSide = "com.tamashenning.forgeanalytics.client.AnalyticsCommon")
	public static AnalyticsCommon proxiedClient;
	
	public static Logger logger;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		// logger = e.getModLog();
		proxy.preInit(e);
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent e) {

	}

	@EventHandler
	public void serverStarted(FMLServerStartedEvent e) {

	}

	@EventHandler
	public void serverStopped(FMLServerStoppedEvent e) {
		AnalyticsClient ac = new AnalyticsClient();
		try {
			ac.UploadModel(ac.CreateServerStoppedPing(), false);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ForgeAnalyticsSingleton.getInstance().CancelTimer();
	}
}
