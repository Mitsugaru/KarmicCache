package com.Mitsugaru.KarmicCache;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commander implements CommandExecutor {
	private KarmicCache plugin;
	private PermissionsHandler perm;
	//TODO player cache hashmap of player and the chest they are locked onto

	public Commander(KarmicCache karmicCache) {
		plugin = karmicCache;
		perm = plugin.getPermissionsHandler();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		// TODO Auto-generated method stub
		if(args.length == 0)
		{
			//No arguments given
		}
		else
		{
			//Grab command
			final String cmd = args[0].toLowerCase();
			if(cmd.equals("create"))
			{
				if(!perm.has(sender, "KarmicCache.create"))
				{
					sender.sendMessage(ChatColor.RED + KarmicCache.prefix + " Lack Permission: KarmicCache.create");
					return true;
				}
				final Player player = (Player) sender;
				if(player == null)
				{
					sender.sendMessage(ChatColor.RED + KarmicCache.prefix + " Can only use command as a player.");
					return true;
				}
				//TODO check if they have the force create method, which bypasses existing cache check
				//TODO create cache;
			}
			if(cmd.equals("help") || cmd.equals("?"))
			{
				//Show help
			}
			//TODO edit command
			//edit message, hint, description, owner
		}
		return false;
	}

	public void showHelp(CommandSender sender)
	{
		//Header
		sender.sendMessage(ChatColor.GREEN + "=====" + ChatColor.AQUA
				+ "KarmicShare" + ChatColor.GREEN + "=====");
		sender.sendMessage(ChatColor.WHITE + "/geo find [name]" + ChatColor.YELLOW
				+ " : Search for a nearby cache, or specified cache");
		sender.sendMessage(ChatColor.WHITE + "/geo info [name]" + ChatColor.YELLOW
				+ " : Get info on nearby cache, or specified cache");
		sender.sendMessage(ChatColor.WHITE + "/geo hint" + ChatColor.YELLOW
				+ " : Get hint on current cache");
		sender.sendMessage(ChatColor.WHITE + "/geo create [name] [description]" + ChatColor.YELLOW
				+ " : Create a new cache");
		sender.sendMessage(ChatColor.WHITE + "/geo edit <name> hint [hint]" + ChatColor.YELLOW
				+ " : Edits a cache's hint");
		sender.sendMessage(ChatColor.WHITE + "/geo edit <name> desc [description]" + ChatColor.YELLOW
				+ " : Edits a cache's description");
		sender.sendMessage(ChatColor.WHITE + "/geo delete [name]" + ChatColor.YELLOW
				+ " : Delete all caches in current region, or specified cache");
	}

}
