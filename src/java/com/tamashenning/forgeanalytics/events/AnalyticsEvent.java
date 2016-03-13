package com.tamashenning.forgeanalytics.events;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;

public class AnalyticsEvent extends Event {
	
	public final Side side;
	
	public AnalyticsEvent(Side s){
		super();
		side = s;
	}
}
