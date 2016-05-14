package jp.seiya0818;


import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Koueki extends JavaPlugin implements Listener
{
	public void onEnable()
	{
		//プラグイン読み込み時の処理
		System.out.println(ChatColor.GREEN + "Koueki Plugin正常に読み込まれました。");
	}
	
	public void onDisable()
	{
		//プラグイン終了時の処理
		System.out.println(ChatColor.GREEN + "Koueki Pluginが正常に終了しました。");
	}
}
