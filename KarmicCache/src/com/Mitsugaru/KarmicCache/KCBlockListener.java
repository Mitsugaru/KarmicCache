package com.Mitsugaru.KarmicCache;

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

public class KCBlockListener extends BlockListener {
	private KarmicCache plugin;

	public KCBlockListener(KarmicCache karmicCache) {
		plugin = karmicCache;
	}

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
