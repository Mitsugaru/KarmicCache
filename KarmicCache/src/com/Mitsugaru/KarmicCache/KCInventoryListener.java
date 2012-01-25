package com.Mitsugaru.KarmicCache;

import org.getspout.spoutapi.event.inventory.InventoryCloseEvent;
import org.getspout.spoutapi.event.inventory.InventoryListener;

public class KCInventoryListener extends InventoryListener {

	public void onInventoryCloseEvent(InventoryCloseEvent event)
	{
		//TODO Check if they are in the locked hashmap and remove them from it
		//TODO remove chest from lock list
	}
}
