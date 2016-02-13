package com.tamashenning.forgeanalyitcs.client;

import java.util.HashMap;
import java.util.Map;

public class ForgeAnalyticsConstants {
	public static String pingClientTable = "ClientTable";
	public static String pingClientStartCommand = "PING";
	public static String pingServerTable = "ServerTable";
	public static String pingServerStartCommand = "PING_START";
	public static String pingServerStopCommand = "PING_STOP";
	
	public static String pingClientKeepAlive = "PING";
	public static String pingServerKeepAlive = "PING";
	
	public static String serverUrl = "http://forgeanalytics.azurewebsites.net/api/Analytics";
	
	public static String AdID = "";
	
	public static int HASHCOUNT = 5;
	public static int KEEPALIVETIME = 5 * 60 * 1000;

	public static Map<String, String> CustomProperties = new HashMap<String, String>();
	
}
