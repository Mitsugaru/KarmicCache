package com.Mitsugaru.KarmicCache;

import java.sql.ResultSet;
import java.sql.SQLException;

import lib.PatPeter.SQLibrary.MySQL;
import lib.PatPeter.SQLibrary.SQLite;

public class DBHandler {
	// Class Variables
	private KarmicCache plugin;
	private Config config;
	private SQLite sqlite;
	private MySQL mysql;
	private boolean useMySQL;

	public DBHandler(KarmicCache ks, Config conf) {
		plugin = ks;
		config = conf;
		useMySQL = config.useMySQL;
		checkTables();
		if (config.importSQL)
		{
			if (useMySQL)
			{
				importSQL();
			}
			config.set("mysql.import", false);
		}
	}

	private void checkTables() {
		if (useMySQL)
		{
			// Connect to mysql database
			mysql = new MySQL(plugin.log, KarmicCache.prefix, config.host,
					config.port, config.database, config.user, config.password);
			// Check if cache table exists
			if (!mysql.checkTable(config.tablePrefix + "cache"))
			{
				plugin.log.info(KarmicCache.prefix + " Created cache table");
				mysql.createTable("CREATE TABLE "
						+ config.tablePrefix
						+ "cache (`id` INT UNSIGNED NOT NULL AUTO_INCREMENT, `name` TEXT NOT NULL, `description` TEXT, `world` TEXT NOT NULL, `message` TEXT, `hint` TEXT, `regionX` SMALLINT NOT NULL, `regionZ` SMALLINT NOT NULL, `x` INT NOT NULL, `y` INT NOT NULL, `z` INT NOT NULL, PRIMARY KEY (id));");
			}
			// Check if player table exists
			if (!mysql.checkTable(config.tablePrefix + "players"))
			{
				/*
				 * Schema: playername - name of player found - comma delimited
				 * list of caches found. based off of cache id
				 */
				plugin.log.info(KarmicCache.prefix + " Created players table");
				mysql.createTable("CREATE TABLE "
						+ config.tablePrefix
						+ "players (`playername` varchar(32) NOT NULL, `found` TEXT, UNIQUE (`playername`));");
			}
		}
		else
		{
			// Connect to sql database
			sqlite = new SQLite(plugin.log, KarmicCache.prefix, "pool", plugin
					.getDataFolder().getAbsolutePath());
			// Check if cache table exists
			//TODO duplicate from above
		}
	}

	private void importSQL() {
		// Connect to sql database
		try
		{
			StringBuilder sb = new StringBuilder();
			// Grab local SQLite database
			sqlite = new SQLite(plugin.log, KarmicCache.prefix, "pool", plugin
					.getDataFolder().getAbsolutePath());
			// Copy items
			ResultSet rs = sqlite.select("SELECT * FROM " + config.tablePrefix
					+ "items;");
			if (rs.next())
			{
				plugin.log.info(KarmicCache.prefix + " Importing cache...");
				do
				{
					//TODO import
					final String query = sb.toString();
					mysql.standardQuery(query);
					sb = new StringBuilder();
				}
				while (rs.next());
			}
			rs.close();
			sb = new StringBuilder();
			// Copy players
			rs = sqlite.select("SELECT * FROM " + config.tablePrefix
					+ "players;");
			if (rs.next())
			{
				plugin.log.info(KarmicCache.prefix + " Importing players...");
				do
				{
					//TODO import
					final String query = sb.toString();
					mysql.standardQuery(query);
					sb = new StringBuilder();
				}
				while (rs.next());
			}
			rs.close();
			plugin.log.info(KarmicCache.prefix
					+ " Done importing SQLite into MySQL");
		}
		catch (SQLException e)
		{
			plugin.log.warning(KarmicCache.prefix + " SQL Exception on Import");
			e.printStackTrace();
		}

	}

	public boolean checkConnection() {
		boolean connected = false;
		if (useMySQL)
		{
			connected = mysql.checkConnection();
		}
		else
		{
			connected = sqlite.checkConnection();
		}
		return connected;
	}

	public void close() {
		if (useMySQL)
		{
			mysql.close();
		}
		else
		{
			sqlite.close();
		}
	}

	public ResultSet select(String query) {
		if (useMySQL)
		{
			return mysql.select(query);
		}
		else
		{
			return sqlite.select(query);
		}
	}

	public void standardQuery(String query) {
		if (useMySQL)
		{
			mysql.standardQuery(query);
		}
		else
		{
			sqlite.standardQuery(query);
		}
	}

	public void createTable(String query) {
		if (useMySQL)
		{
			mysql.createTable(query);
		}
		else
		{
			sqlite.createTable(query);
		}
	}
}
