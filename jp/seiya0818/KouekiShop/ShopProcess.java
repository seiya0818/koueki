package jp.seiya0818.KouekiShop;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jp.seiya0818.Koueki;
import jp.seiya0818.Process;
import jp.seiya0818.KouekiPost.PostProcess;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ShopProcess
{
	private static Map<String, ShopSetup> slist = new HashMap<String, ShopSetup>();

	public static void loadslist()
	{
		slist.clear();
		if(!Process.getMain().getDataFolder().exists())
		{
			Process.getMain().getDataFolder().mkdir();
		}
		File file = new File(Process.getMain().getDataFolder(), "shops");
		if(!file.exists())
		{
			file.mkdir();
		}
		File[] AOF;
		int j = (AOF = file.listFiles()).length;
		for(int i = 0; i < j; i++)
		{
			File f = AOF[i];
			if(f.getName().contains(".yml"))
			{
				String sid = f.getName().replace(".yml", "");
				FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
				slist.put(sid, new ShopSetup(sid, conf));
			}
		}
	}

	public static ShopSetup getshop(String sid)
	{
		return (ShopSetup)slist.get(sid);
	}

	public static ArrayList<ShopSetup> getshoplist()
	{
		ArrayList<ShopSetup> list = new ArrayList<ShopSetup>();
		ShopSetup ss;
		for (Iterator<ShopSetup> localIterator = slist.values().iterator(); localIterator.hasNext(); list.add(ss))
		{
			ss = (ShopSetup)localIterator.next();
		}
		return list;
	}

	public static void ShopList(CommandSender sender, String args)
	{
		try
		{
			int page = Integer.parseInt(args);
			int maxpage = getshoplist().size() / 9;
			if (getshoplist().size() % 9 != 0)
			{
				maxpage++;
			}
			if ((page < 1) || (page > maxpage))
			{
				sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED +
						"指定されたページ数が見つかりませんでした。");
				return;
			}
			if (getshoplist().size() > 0)
			{
				sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GOLD + "商品の一覧を表示します。");
				for (int i = 9 * page - 9; i < page * 9; i++)
				{
					if (PostProcess.getpostlist().size() > i)
					{
						sender.sendMessage(ChatColor.RED +"[ID] " + ChatColor.RESET +
								((ShopSetup)getshoplist().get(i)).getSID());
					}
				}
			}
			else
			{
				sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "商品が一つも作成されていません。");
			}
		}
		catch (Exception e)
		{
			sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "エラーが発生しました。");
		}
	}

	public static void putshop(String sid, ShopSetup shop)
	{
		slist.put(sid, shop);
	}

	public static void removeshop(String sid)
	{
		slist.remove(sid);
	}
}
