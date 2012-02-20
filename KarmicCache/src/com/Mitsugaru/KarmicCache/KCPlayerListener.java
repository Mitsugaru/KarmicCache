package com.Mitsugaru.KarmicCache;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

public class KCPlayerListener implements Listener {
	private KarmicCache plugin;

	public KCPlayerListener(KarmicCache karmicCache) {
		plugin = karmicCache;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		// TODO Check if they are in the locked hashmap and remove them from it
		// TODO remove chest from lock list
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		// Grab player
		final Player player = event.getPlayer();
		// Handle right click air
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR))
		{
			// Check if they are searching for a cache
			if (plugin.searchCache.containsKey(player.getName()))
			{
				// Check if its the right tool
				if (event.getItem().getTypeId() == plugin.config.searchTool)
				{
					// Grab cache that they are searching
					final GeoCache cache = plugin.searchCache.get(player
							.getName());
					// Calculate distance
					final double distance = Math
							.floor((player.getLocation().toVector()
									.distance(cache.getLocation().toVector()) * 100) / 100);
					player.setCompassTarget(cache.getLocation());
					player.sendMessage(ChatColor.AQUA + KarmicCache.prefix
							+ " Distance to " + ChatColor.GOLD
							+ cache.getName() + ChatColor.AQUA + " :: "
							+ ChatColor.GREEN + distance);
				}
			}
		}

		else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{

			// Handle right click block
			if (event.getClickedBlock().getType().equals(Material.CHEST))
			{
				// TODO Check if its one of our chests
				for (GeoCache cache : plugin.cacheList)
				{
					if (event.getClickedBlock().getLocation()
							.equals(cache.getLocation()))
					{
						// TODO Check if chest is locked by another player
						// TODO check if they have already found this before
						plugin.lockedCache.put(player.getName(),
								cache.getName());
						// TODO populate chest
						if (plugin.hasEco)
						{
							// Pay player
							double major = KarmicCache.r
									.nextInt(plugin.config.econHigh);
							if (major < plugin.config.econLow)
							{
								// Fix to lower bounds
								major += plugin.config.econLow;
							}
							// TODO partial?
							final EconomyResponse response = plugin.eco
									.depositPlayer(player.getName(), major);
							if (response.type == EconomyResponse.ResponseType.SUCCESS)
							{
								// TODO add cache name
								player.sendMessage(ChatColor.GREEN
										+ KarmicCache.prefix + " Received "
										+ ChatColor.GOLD + major
										+ ChatColor.GREEN + " for finding");
							}
							else
							{
								// Could not pay player
								player.sendMessage(ChatColor.RED
										+ KarmicCache.prefix
										+ " Could not pay player!");
							}
						}
						else
						{
							// TODO message player on finding cache
						}
						// Reset compass target
						player.setCompassTarget(player.getLocation().getWorld()
								.getSpawnLocation());
						// TODO add chest id to their found list
						break;
					}
				}

			}
		}
	}
}
