package jp.seiya0818;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Koueki extends JavaPlugin
{
	public static String LoggerPrefix = ChatColor.WHITE + "[" + ChatColor.RED + "koueki_plugin" + ChatColor.WHITE + "]";
	public static String PlayerPrefix = ChatColor.WHITE + "[" + ChatColor.RED + "交易" + ChatColor.WHITE + "]";
	public static KouekiCommandExecutor commands;
	public static Koueki instance;
	
	public void onEnable()
	{	
		//プラグイン読み込み時の処理
		instance = this;
		Bukkit.getConsoleSender().sendMessage(LoggerPrefix + ChatColor.GREEN + "プラグインが正常に読み込まれました。");
		
		if(Process.Init(this))
		{
			new SignSystem();
		}
		
		this.getCommand("koueki").setExecutor(new KouekiCommandExecutor(this));
		
		saveDefaultConfig();
	}
	
	public void onDisable()
	{
		//プラグイン終了時の処理
		Bukkit.getConsoleSender().sendMessage(LoggerPrefix + ChatColor.GREEN + "プラグインが正常に終了しました。");
	}
}

