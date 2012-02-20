package com.Mitsugaru.KarmicCache;

import org.bukkit.event.Listener;
import org.getspout.spoutapi.event.inventory.InventoryCloseEvent;

public class KCInventoryListener implements Listener {

	public void onInventoryCloseEvent(InventoryCloseEvent event)
	{
		//TODO Check if they are in the locked hashmap and remove them from it
		//TODO remove chest from lock list
	}
}
