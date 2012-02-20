package com.Mitsugaru.KarmicCache;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * KarmicCache plugin. GeoCaching plugin for Minecraft. Built from Thomas
 * Bucher's/ToasterKTN's GeoCacher source.
 *
 * @author Mitsugaru
 *
 */
public class KarmicCache extends JavaPlugin {
	// Class variables
	private DBHandler database;
	private PermissionsHandler perms;
	public final static Random r = new Random(System.currentTimeMillis());
	public final static String prefix = "[KarmicCache]";
	public final Logger log = Logger.getLogger("Minecraft");
	public Config config;
	public Economy eco;
	public boolean hasEco;
	public final Map<String, String> lockedCache = new HashMap<String, String>();
	public final Map<String, GeoCache> searchCache = new HashMap<String, GeoCache>();
	public final List<GeoCache> cacheList = new ArrayList<GeoCache>();

	@Override
	public void onDisable() {
		// Reload and save config
		this.reloadConfig();
		this.saveConfig();
		// TODO save caches?
		// Disconnect from sql database
		if (database.checkConnection())
		{
			// Close connection
			database.close();
			log.info(prefix + " Closed database connection.");
		}
		log.info(prefix + " v" + getDescription().getVersion()
				+ " has been disabled.");
	}

	@Override
	public void onLoad() {
		// Load config
		config = new Config(this);
		database = new DBHandler(this, config);
	}

	@Override
	public void onEnable() {
		// Setup permissions and economy
		perms = new PermissionsHandler(this);
		if(config.econUse)
		{
			setupEconomy();
		}
		else
		{
			hasEco = false;
		}

		// Hook Commander to root command
		getCommand("geo").setExecutor(new Commander(this));

		// Grab plugin manager
		final PluginManager pm = getServer().getPluginManager();
		//Register events
		pm.registerEvents(new KCPlayerListener(this), this);
		// Register a Chunk Creation, we may want to add a Cache
		pm.registerEvents(new KCBlockListener(this), this);
	    pm.registerEvents(new KCChunkListener(this), this);
		//Load/verify cache list. Prune non-existing caches
	    loadCacheList();
	    log.info(prefix + " v" + getDescription().getVersion()
				+ " has been enabled.");
	}

	private void setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = this.getServer()
				.getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null)
		{
			eco = economyProvider.getProvider();
			hasEco = true;
		}
		else
		{
			hasEco = false;
		}
	}

	public boolean cacheExistsInRegion(int x, int z)
	{
		//TODO
		return false;
	}

	public void loadCacheList()
	{
		ResultSet rs = this.getDatabaseHandler().select("SELECT * FROM cache");
	}

	public PermissionsHandler getPermissionsHandler()
	{
		return perms;
	}

	public DBHandler getDatabaseHandler()
	{
		return database;
	}
}
