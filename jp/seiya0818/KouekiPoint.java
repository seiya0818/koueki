package jp.seiya0818;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class KouekiPoint
{
	public static File getPointFile(Player player)
	{
	    File dafl = Process.getMain().getDataFolder();
	    if (!dafl.exists())
	    {
	      dafl.mkdir();
	    }
	    File plyfl = new File(dafl, "playerdata");
	    if (!plyfl.exists())
	    {
	      plyfl.mkdir();
	      setupPlayerConfig(player);
	    }
	    return new File(plyfl, player.getName() + ".yml");
	}
	
	public static FileConfiguration getPlayerConfig(Player player)
	{
	    return YamlConfiguration.loadConfiguration(getPointFile(player));
	}
	
	public static void setupPlayerConfig(Player player)
	{
		FileConfiguration conf = getPlayerConfig(player);
		conf.set("Point", 0);
		try
		{
			conf.save(getPointFile(player));
		}
		catch(IOException e)
		{
			Process.getMain().getLogger().warning(Koueki.LoggerPrefix + "コンフィグの保存に失敗しました。" + player);
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED +
					"エラーが発生しました。管理人に報告してください。");
		}
	}
	
	public static void addPoint(Player player, int point)
	{
		FileConfiguration conf = getPlayerConfig(player);
		int points = conf.getInt("Point");
		int totalpoint = point + points;
		conf.set("Point", totalpoint);
		player.sendMessage(Koueki.PlayerPrefix
		+ ChatColor.LIGHT_PURPLE + point + Process.getMain().conf.getString("Point-Unit") +
		ChatColor.AQUA + " 追加しました。");
		player.sendMessage(Koueki.PlayerPrefix + ChatColor.AQUA +
		"合計 " + ChatColor.LIGHT_PURPLE + totalpoint + Process.getMain().conf.getString("Point-Unit") +
		ChatColor.AQUA + " です。");
		try
		{
			conf.save(getPointFile(player));
		}
		catch(IOException e)
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED +
					"エラーが発生しました。管理人に報告してください。");
		}
	}
	
	public static int getPoint(Player player)
	{
		FileConfiguration conf = getPlayerConfig(player);
		int points = conf.getInt("Point");
		return points;
	}
}
