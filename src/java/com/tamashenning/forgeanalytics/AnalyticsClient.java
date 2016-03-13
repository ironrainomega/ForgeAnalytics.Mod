package com.tamashenning.forgeanalytics;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.tamashenning.forgeanalytics.client.ForgeAnalyticsConstants;
import com.tamashenning.forgeanalytics.client.ForgeAnalyticsSingleton;
import com.tamashenning.forgeanalytics.events.AnalyticsEvent;
import com.tamashenning.forgeanalytics.models.AnalyticsModel;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

public class AnalyticsClient {

	public boolean UploadModel(AnalyticsModel model, boolean isClient) throws Exception {

		if (!FireEvent(isClient)) {
			return false;
		}

		model.Properties.putAll(ForgeAnalyticsConstants.CustomProperties);

		// Code to send to Tamas backend...

		JsonObject data = new JsonObject();
		data.add("PartitionKey", new JsonPrimitive(model.PartitionKey));
		data.add("ClientDateTimeEpoch", new JsonPrimitive(model.ClientDateTimeEpoch));
		data.add("Table", new JsonPrimitive(model.Table));

		JsonObject propertiesMap = new JsonObject();

		for (Map.Entry<String, String> entry : model.Properties.entrySet()) {
			// if the user opted out of the property, respect it...
			if (!ForgeAnalyticsConstants.dataConfig
					.get(Configuration.CATEGORY_GENERAL, entry.getKey() + "_OptOut", false).getBoolean()) {

				propertiesMap.add(entry.getKey(), new JsonPrimitive(entry.getValue()));
			}
		}

		data.add("Properties", propertiesMap);

		String json = data.toString();

		// Code to send to Lex' backend

		JsonObject dataForge = new JsonObject();
		dataForge.add("cmd", new JsonPrimitive(model.PartitionKey));

		for (Map.Entry<String, String> entry : model.Properties.entrySet()) {
			// if the user opted out of the property, respect it...
			if (!ForgeAnalyticsConstants.dataConfig
					.get(Configuration.CATEGORY_GENERAL, entry.getKey() + "_OptOut", false).getBoolean()) {

				dataForge.add(entry.getKey(), new JsonPrimitive(entry.getValue()));
			}
		}

		this.UploadForge(dataForge.toString());
		return this.UploadModel(json, isClient);
	}

	public AnalyticsModel CreateClientStartupPing() {
		AnalyticsModel am = new AnalyticsModel();
		am.Table = ForgeAnalyticsConstants.pingClientTable;
		am.Properties = new HashMap<String, String>();
		am.PartitionKey = ForgeAnalyticsConstants.pingClientStartCommand;
		am.ClientDateTimeEpoch = System.currentTimeMillis() / 1000L;
		am.Properties.putAll(this.getCommonValues());

		return am;
	}

	public AnalyticsModel CreateServerStartupPing() {
		AnalyticsModel am = new AnalyticsModel();
		am.Table = ForgeAnalyticsConstants.pingServerTable;
		am.Properties = new HashMap<String, String>();
		am.PartitionKey = ForgeAnalyticsConstants.pingServerStartCommand;
		am.ClientDateTimeEpoch = System.currentTimeMillis() / 1000L;
		am.Properties.putAll(this.getCommonValues());
		// am.Properties.put("ServerDifficulty",
		// MinecraftServer.getServer().getDifficulty().toString());

		/*
		 * MinecraftServer server = MinecraftServer.getServer();
		 * 
		 * if (MinecraftServer.getServer().isDedicatedServer()) { // Running
		 * dedicated... try { am.Properties.put("ServerHostHash",
		 * this.Anonymize(server.getHostname())); } catch
		 * (NoSuchAlgorithmException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } } else { // Running internal...
		 * am.Properties.put("ServerHostHash", "localhost"); }
		 */

		return am;
	}

	public AnalyticsModel CreateServerStoppedPing() {
		AnalyticsModel am = new AnalyticsModel();
		am.Table = ForgeAnalyticsConstants.pingServerTable;
		am.Properties = new HashMap<String, String>();
		am.PartitionKey = ForgeAnalyticsConstants.pingServerStopCommand;
		am.ClientDateTimeEpoch = System.currentTimeMillis() / 1000L;
		am.Properties.putAll(this.getCommonValues());
		// am.Properties.put("ServerDifficulty",
		// MinecraftServer.getServer().getDifficulty().toString());

		/*
		 * MinecraftServer server = MinecraftServer.getServer();
		 * 
		 * if (MinecraftServer.getServer().isDedicatedServer()) { // Running
		 * dedicated... try { am.Properties.put("ServerHostHash",
		 * this.Anonymize(server.getHostname())); } catch
		 * (NoSuchAlgorithmException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } } else { // Running internal...
		 * am.Properties.put("ServerHostHash", "localhost"); }
		 */

		return am;
	}

	public AnalyticsModel CreateClientKeepAlivePing() {
		AnalyticsModel am = new AnalyticsModel();
		am.Table = ForgeAnalyticsConstants.pingClientTable;
		am.Properties = new HashMap<String, String>();
		am.PartitionKey = ForgeAnalyticsConstants.pingClientKeepAlive;
		am.ClientDateTimeEpoch = System.currentTimeMillis() / 1000L;
		am.Properties.putAll(this.getCommonValues());

		return am;
	}

	public AnalyticsModel CreateServerKeepAlivePing() {
		AnalyticsModel am = new AnalyticsModel();
		am.Table = ForgeAnalyticsConstants.pingServerTable;
		am.Properties = new HashMap<String, String>();
		am.PartitionKey = ForgeAnalyticsConstants.pingServerKeepAlive;
		am.ClientDateTimeEpoch = System.currentTimeMillis() / 1000L;
		am.Properties.putAll(this.getCommonValues());
		// am.Properties.put("ServerDifficulty",
		// MinecraftServer.getServer().getDifficulty().toString());

		/*
		 * Removing this part for now... MinecraftServer server =
		 * MinecraftServer.getServer();
		 * 
		 * if (MinecraftServer.getServer().isDedicatedServer()) { // Running
		 * dedicated... try { if (server != null) {
		 * am.Properties.put("ServerHostHash",
		 * this.Anonymize(server.getHostname())); } } catch
		 * (NoSuchAlgorithmException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } am.Properties.put("ConnectedUsers",
		 * Integer.toString(server.getCurrentPlayerCount())); } else { //
		 * Running internal... am.Properties.put("ServerHostHash", "localhost");
		 * }
		 */

		return am;
	}

	public boolean FireEvent(boolean isClient) {
		if (isClient) {
			// Respect snooper settings...
			if (!Minecraft.getMinecraft().isSnooperEnabled()) {
				return false;
			}
			MinecraftForge.EVENT_BUS.post(new AnalyticsEvent(net.minecraftforge.fml.relauncher.Side.CLIENT));

		} else {
			// Respect snooper settings...
			if (!MinecraftServer.getServer().isSnooperEnabled()) {
				return false;
			}

			MinecraftForge.EVENT_BUS.post(new AnalyticsEvent(net.minecraftforge.fml.relauncher.Side.SERVER));
		}
		return true;
	}

	private Map<String, String> getCommonValues() {
		Map<String, String> commonValues = new HashMap<String, String>();
		
		String activeModListCount = Integer.toString(net.minecraftforge.fml.common.Loader.instance().getActiveModList().size());

		String modListCount = Integer.toString(net.minecraftforge.fml.common.Loader.instance().getModList().size());
		// String modList = "";

		commonValues.put("JavaVersion", System.getProperty("java.version"));
		commonValues.put("JavaMaxRAM", Long.toString(Runtime.getRuntime().maxMemory()));
		commonValues.put("JavaAllocatedRAM", Long.toString(Runtime.getRuntime().totalMemory()));
		// TODO: Remove once fully switched over.
		commonValues.put("SessionID", ForgeAnalyticsSingleton.getInstance().SessionID);
		commonValues.put("AdID", ForgeAnalyticsConstants.AdID);
		commonValues.put("session_id", ForgeAnalyticsSingleton.getInstance().SessionUUID.toString());
		commonValues.put("instance_id", ForgeAnalyticsConstants.InstanceUUID.toString());
		commonValues.put("MinecraftVersion", net.minecraftforge.fml.common.Loader.instance().getMCVersionString());
		commonValues.put("ForgeVersion", ForgeVersion.getVersion());
		commonValues.put("MCPVersion", net.minecraftforge.fml.common.Loader.instance().getMCPVersionString());
		commonValues.put("ActiveModCount", activeModListCount);
		commonValues.put("ModCount", modListCount);
		commonValues.put("ModPack", ForgeAnalyticsConstants.modPack);
		/*
		 * for (ModContainer mod :
		 * cpw.mods.fml.common.Loader.instance().getModList()) { modList +=
		 * mod.getModId() + "@" + mod.getVersion() + ";"; }
		 * 
		 * commonValues.put("ModList", modList);
		 */

		return commonValues;
	}

	private boolean UploadModel(String json, boolean isClient) throws Exception {

		HttpClient httpClient = HttpClientBuilder.create().build();

		try {
			HttpPost request = new HttpPost(ForgeAnalyticsConstants.serverUrl);

			StringEntity params = new StringEntity(json);
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);
			

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return true;
	}

	private void UploadForge(String json) throws Exception {

		HttpClient httpClient = HttpClientBuilder.create().build();

		try {
			HttpPost request = new HttpPost(ForgeAnalyticsConstants.forgeServerUrl);

			List<NameValuePair> nvp = new ArrayList<NameValuePair>();
			nvp.add(new BasicNameValuePair("stat", json));

			request.setEntity(new UrlEncodedFormEntity(nvp));
			HttpResponse response = httpClient.execute(request);
			
		} catch (Exception ex) {
			// handle exception here
			ex.printStackTrace();
		}
	}

	public String Anonymize(String data) throws NoSuchAlgorithmException {
		MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
		byte[] dataBytes = data.getBytes();
		for (int i = 0; i < ForgeAnalyticsConstants.HASHCOUNT; i++) {
			dataBytes = sha256.digest(dataBytes);
		}

		return this.bytesToHex(dataBytes);
	}

	final protected char[] hexArray = "0123456789abcdef".toCharArray();

	private String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
}
