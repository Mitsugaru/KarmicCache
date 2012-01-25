package com.Mitsugaru.KarmicCache;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;

public class Config {
	private KarmicCache plugin;
	public boolean debug, useMySQL, importSQL, econUse, autogen;
	public String host, port, database, user, password, tablePrefix;
	public int searchTool, autoCacheChance, econLow, econHigh;

	public Config(KarmicCache karmicCache) {
		plugin = karmicCache;
		final ConfigurationSection config = plugin.getConfig();
		// Hashmap of defaults
		final Map<String, Object> defaults = new HashMap<String, Object>();
		defaults.put("version", plugin.getDescription().getVersion());
		defaults.put("mysql.use", false);
		defaults.put("mysql.host", "localhost");
		defaults.put("mysql.port", 3306);
		defaults.put("mysql.database", "minecraft");
		defaults.put("mysql.user", "username");
		defaults.put("mysql.password", "pass");
		defaults.put("mysql.tablePrefix", "ks_");
		defaults.put("mysql.import", false);
		defaults.put("economy.use", true);
		defaults.put("economy.low", 1);
		defaults.put("economy.high", 1000);
		defaults.put("searchtool", 345);
		defaults.put("autogen.use", true);
		defaults.put("autogen.chance", 33);
		// Insert defaults into config file if they're not present
		for (final Entry<String, Object> e : defaults.entrySet())
		{
			if (!config.contains(e.getKey()))
			{
				config.set(e.getKey(), e.getValue());
			}
		}
		// Save config
		plugin.saveConfig();
		// Load variables from config
		useMySQL = config.getBoolean("mysql.use", false);
		host = config.getString("mysql.host", "localhost");
		port = config.getString("mysql.port", "3306");
		database = config.getString("mysql.database", "minecraft");
		user = config.getString("mysql.user", "user");
		password = config.getString("mysql.password", "password");
		tablePrefix = config.getString("mysql.prefix", "ks_");
		importSQL = config.getBoolean("mysql.import", false);
		econUse = config.getBoolean("economy.use", true);
		econLow = config.getInt("economy.low", 1);
		econHigh = config.getInt("economy.high", 1000);
		searchTool = config.getInt("searchtool", 345);
		debug = config.getBoolean("debug", false);
		autogen = config.getBoolean("autogen.use", true);
		autoCacheChance = config.getInt("autogen.chance", 33);
		//Bounds check
		checkBounds();
	}

	/**
	 * Reloads info from yaml file(s)
	 */
	public void reloadConfig() {
		// Initial relaod
		plugin.reloadConfig();
		final ConfigurationSection config = plugin.getConfig();
		econUse = config.getBoolean("economy.use", true);
		econLow = config.getInt("economy.low", 1);
		econHigh = config.getInt("economy.high", 1000);
		debug = config.getBoolean("debug", false);
		autogen = config.getBoolean("autogeneratecache", true);
		autoCacheChance = config.getInt("autogen.chance", 50);
		checkBounds();
		plugin.log.info(KarmicCache.prefix + " Config reloaded");
	}

	public void set(String path, Object o) {
		final ConfigurationSection config = plugin.getConfig();
		config.set(path, o);
		plugin.saveConfig();
	}

	public void checkBounds()
	{
		//TODO check if valid search tool
		if(searchTool < 0)
		{
			plugin.log.warning(KarmicCache.prefix + " Config: searchtool has negative value. Setting to default.");
			searchTool = 345;
		}
		if(econLow < 0)
		{
			plugin.log.warning(KarmicCache.prefix + " Config: economy.low has negative value. Setting to default.");
			econLow = 1;
		}
		if(econHigh < econLow)
		{
			plugin.log.warning(KarmicCache.prefix + " Config: economy.high has invalid range. Changing to default/valid.");
			if(econLow > 1000)
			{
				econHigh = econLow + 1;
			}
			else
			{
				econHigh =  1000;
			}
		}
		if(autoCacheChance < 0 || autoCacheChance > 100)
		{
			plugin.log.warning(KarmicCache.prefix + " Config: autogen.chance out of bounds. Setting to default.");
			autoCacheChance = 33;
		}
	}

	/**
	 * Check if updates are necessary
	 */
	public void checkUpdate() {
		// Check if need to update
		ConfigurationSection config = plugin.getConfig();
		if (Double.parseDouble(plugin.getDescription().getVersion()) > Double
				.parseDouble(config.getString("version")))
		{
			// Update to latest version
			plugin.log.info(KarmicCache.prefix + " Updating to v"
					+ plugin.getDescription().getVersion());
			this.update();
		}
	}

	/**
	 * This method is called to make the appropriate changes, most likely only
	 * necessary for database schema modification, for a proper update.
	 */
	private void update() {
		// Grab current version
		@SuppressWarnings ("unused")
		final double ver = Double.parseDouble(plugin.getConfig().getString(
				"version"));
		// Update version number in config.yml
		plugin.getConfig().set("version", plugin.getDescription().getVersion());
		plugin.saveConfig();
		plugin.log.info(KarmicCache.prefix + " Upgrade complete");
	}
}
