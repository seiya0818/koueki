package jp.seiya0818;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import jp.seiya0818.KouekiPriceFluctuation.RandomPrice;
import jp.seiya0818.KouekiSystem.KouekiCancelEvent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Koueki extends JavaPlugin
{
	public static String LoggerPrefix = ChatColor.WHITE + "[" + ChatColor.RED + "koueki_plugin" + ChatColor.WHITE + "]";
	public static String PlayerPrefix = ChatColor.WHITE + "[" + ChatColor.RED + "交易" + ChatColor.WHITE + "]";
	public static KouekiCommandExecutor commands;
	public static Koueki instance;
	public HashMap<String, BukkitRunnable> timers = new HashMap<String, BukkitRunnable>();
	public FileConfiguration conf = getConfig();
	static final private Charset CONFIG_CHAREST=StandardCharsets.UTF_8;

	public void onEnable()
	{
		//プラグイン読み込み時の処理
		instance = this;
		Bukkit.getConsoleSender().sendMessage(LoggerPrefix + ChatColor.GREEN + "プラグインが正常に読み込まれました。");

		if(Process.Init(this))
		{
			new SignSystem();
			new KouekiCancelEvent();
			new KouekiGUI();
		}

		this.getCommand("koueki").setExecutor(new KouekiCommandExecutor(this));

		setTimer();
		RandomPrice.putnumber();

		saveDefaultConfig();
		String confFilePath=getDataFolder() + File.separator + "config.yml";
		try(Reader reader=new InputStreamReader(new FileInputStream(confFilePath),CONFIG_CHAREST))
		{
			conf.load(reader);
		}
		catch(Exception e)
		{
			System.out.println(e);
			onDisable();
		}
	}

	public void onDisable()
	{
		//プラグイン終了時の処理
		Bukkit.getConsoleSender().sendMessage(LoggerPrefix + ChatColor.GREEN + "プラグインが正常に終了しました。");
	}

	private void setTimer()
	{
		new RandomPrice(this);
		RandomPrice.delayTask(300);
	}
}

