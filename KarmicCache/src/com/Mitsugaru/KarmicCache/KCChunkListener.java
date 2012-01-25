package com.Mitsugaru.KarmicCache;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldListener;

public class KCChunkListener extends WorldListener {
	public KarmicCache plugin;

	public KCChunkListener(KarmicCache kc) {
		plugin = kc;
	}

	public void onChunkLoad(ChunkLoadEvent event) {
		// Check if we're auto-generating the caches
		if (plugin.config.autogen)
		{
			// Roll to see if we place a cache
			final double chance = (double) plugin.config.autoCacheChance / 100.0;
			if (KarmicCache.r.nextDouble() < chance)
			{
				// Grab chunk
				final Chunk chunk = event.getChunk();
				// Grab region
				final int regionX = (int) Math
						.floor((double) chunk.getX() / 32);
				final int regionZ = (int) Math
						.floor((double) chunk.getZ() / 32);
				// TODO verify that we don't have a cache in the region
				// TODO try generate cache somewhere in region
				Location loc = new Location(event.getWorld(), chunk.getX() * 16
						+ KarmicCache.r.nextInt(16), 0, chunk.getZ() * 16
						+ KarmicCache.r.nextInt(16));
				//Only check above sea level
				for (int y = 64; y < 127; y++)
				{
					loc.setY(y);
					if (loc.getBlock().getType() == Material.GRASS
							|| loc.getBlock().getType() == Material.SAND)
					{
						//Check if there is space above for the chest
						loc.setY(y + 1);
						if(loc.getBlock().getType().equals(Material.AIR))
						{
							// TODO Broadcast creation
							final GeoCache gc = new GeoCache(plugin, loc);
							// TODO add to cache list
							// plugin.geocaches.add(ogc);
							break;
						}
					}
					else if (loc.getBlock().getType() == Material.AIR)
					{
						// We hit air before hitting valid landing areas
						break;
					}
				}
			}
		}
	}
}
