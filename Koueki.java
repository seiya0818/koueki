package jp.seiya0818;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Koueki extends JavaPlugin
{
	private KouekiCommandExecutor command;
	public static String LoggerPrefix = ChatColor.WHITE + "[" + ChatColor.RED + "koueki_plugin" + ChatColor.WHITE + "]";
	public static String PlayerPrefix = ChatColor.WHITE + "[" + ChatColor.RED + "交易" + ChatColor.WHITE + "]";
	
	public void onEnable()
	{
		//プラグイン読み込み時の処理
		getLogger().info(LoggerPrefix + ChatColor.GREEN + "Koueki Plugin正常に読み込まれました。");
		command = new KouekiCommandExecutor();
	}
	
	public void onDisable()
	{
		//プラグイン終了時の処理
		getLogger().info(LoggerPrefix + ChatColor.GREEN + "Koueki Pluginが正常に終了しました。");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		return this.command.onCommand(sender, cmd, label, args);
	}
}

