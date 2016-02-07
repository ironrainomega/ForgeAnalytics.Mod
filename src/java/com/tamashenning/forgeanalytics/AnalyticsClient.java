package com.tamashenning.forgeanalytics;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import com.tamashenning.forgeanalytics.models.AnalyticsModel;

public class AnalyticsClient {
	
	public boolean UploadModel(AnalyticsModel model) throws Exception {
		Gson g = new Gson();
		String json = g.toJson(model);
		
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
	    }catch (Exception ex) {
	        // handle exception here
	    	ex.printStackTrace();
	    }
				
		return true;
	}
}
