package com.tamashenning.forgeanalytics;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import com.tamashenning.forgeanalytics.models.AnalyticsModel;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.ModContainer;

public class AnalyticsClient {
	
	private String pingClientTable = "ClientTable";
	private String pingServerTable = "ServerTable";
	private final int HASHCOUNT = 5;
	
	public boolean UploadModel(AnalyticsModel model) throws Exception {
		Gson g = new Gson();
		String json = g.toJson(model);

		return this.UploadModel(json);
	}
	
	public boolean UploadModel(String json) throws Exception {
		System.out.println(json);
		HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead 

	    try {
	        HttpPost request = new HttpPost("http://forgeanalytics.azurewebsites.net/api/Analytics");
	        
	        StringEntity params =new StringEntity(json);
	        request.addHeader("content-type", "application/json");
	        request.setEntity(params);
	        HttpResponse response = httpClient.execute(request);
	        System.out.println(response.toString());
	        // handle response here...
	    } catch (Exception ex) {
	        // handle exception here
	    	ex.printStackTrace();
	    }
	    
		return true;
	}
	
	public String CreateClientStartupPing() {
		Gson g = new Gson();
		
		AnalyticsModel am = new AnalyticsModel();
		am.Table = this.pingClientTable;
		am.Properties = new HashMap<String, String>();
		am.PartitionKey = "PING";
		am.ClientDateTimeEpoch = System.currentTimeMillis() / 1000L;
		am.Properties.putAll(this.getCommonValues());
		
		/*try {
			// TODO figure this out...
			// am.Properties.put("UserHash", this.Anonymize(Minecraft.getMinecraft().thePlayer.getUniqueID().toString()));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		String json = g.toJson(am);
		return json;
	}
	
	public String CreateServerStartupPing() {
		Gson g = new Gson();
		
		AnalyticsModel am = new AnalyticsModel();
		am.Table = this.pingServerTable;
		am.Properties = new HashMap<String, String>();
		am.PartitionKey = "PING";
		am.ClientDateTimeEpoch = System.currentTimeMillis() / 1000L;
		am.Properties.putAll(this.getCommonValues());
		am.Properties.put("ServerDifficulty", MinecraftServer.getServer().getDifficulty().toString());
		
		MinecraftServer server = MinecraftServer.getServer();
		
		if (MinecraftServer.getServer().isDedicatedServer()) {
			// Running dedicated...
			try {
				am.Properties.put("ServerHostHash", this.Anonymize(server.getHostname()));
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			// Running internal...
			am.Properties.put("ServerHostHash", "localhost");
			am.Properties.put("IsDemo", Boolean.toString(server.isDemo()));
			am.Properties.put("IsLanMode", Boolean.toString(((IntegratedServer)server).getPublic()));
		}
	
		String json = g.toJson(am);
		return json;
	}
	
	
	private Map<String, String> getCommonValues() {
		Map<String, String> commonValues = new HashMap<String, String>();
		String activeModListCount = Integer.toString(net.minecraftforge.fml.common.Loader.instance().getActiveModList().size());
		String modListCount = Integer.toString(net.minecraftforge.fml.common.Loader.instance().getModList().size());
		String modList = "";
		
		commonValues.put("MinecraftVersion", Minecraft.getMinecraft().getVersion());
		commonValues.put("ForgeVersion", ForgeVersion.getVersion());
		commonValues.put("MCPVersion", ForgeVersion.mcpVersion);
		commonValues.put("ActiveModCount", activeModListCount);
		commonValues.put("ModCount", modListCount);

		for (ModContainer mod : net.minecraftforge.fml.common.Loader.instance().getModList()){
			modList += mod.getModId()+"@"+mod.getVersion()+";";
		}

		commonValues.put("ModList", modList);

		
		return commonValues;
	}
	
	
	public String Anonymize(String data) throws NoSuchAlgorithmException {
		MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
		byte[] dataBytes = data.getBytes();
		for (int i=0; i< this.HASHCOUNT; i++) {
			dataBytes = sha256.digest(dataBytes);
		}
		
		return this.bytesToHex(dataBytes);
	}
	
	final protected char[] hexArray = "0123456789abcdef".toCharArray();
	private String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
}
