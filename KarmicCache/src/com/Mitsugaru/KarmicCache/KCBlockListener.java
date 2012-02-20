package com.Mitsugaru.KarmicCache;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class KCBlockListener implements Listener {
	private KarmicCache plugin;

	public KCBlockListener(KarmicCache karmicCache) {
		plugin = karmicCache;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event)
	{
		//Check the block is a chest
		if(event.getBlock().getType().equals(Material.CHEST))
		{
			//TODO Check if chest location exists in cache list
			//TODO check permissions and deny if missing
		}
	}
}
