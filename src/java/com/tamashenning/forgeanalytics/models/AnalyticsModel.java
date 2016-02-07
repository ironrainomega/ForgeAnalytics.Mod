package com.tamashenning.forgeanalytics.models;

import java.util.Map;

public class AnalyticsModel {
	public String PartitionKey = "";
	
	public long ClientDateTimeEpoch = 0;
	
	public String Table = "";
	
	public Map<String, String> Properties;
}
