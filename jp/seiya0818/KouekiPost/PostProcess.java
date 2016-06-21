package jp.seiya0818.KouekiPost;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jp.seiya0818.Koueki;
import jp.seiya0818.Process;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PostProcess
{
	private static Map<String, PostSetup> plist = new HashMap<String, PostSetup>();

	public static void loadplist()
	{
		plist.clear();
		if(!Process.getMain().getDataFolder().exists())
		{
			Process.getMain().getDataFolder().mkdir();
		}
		File file = new File(Process.getMain().getDataFolder(), "posts");
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
				plist.put(sid, new PostSetup(sid, conf));
			}
		}
	}

	public static void setItemMeta(ItemStack item, String display, ArrayList<String> lore)
	{
		ItemMeta meta = item.getItemMeta();
		if (meta != null)
		{
			if (display != null) {
				meta.setDisplayName(display);
			}
			if (lore != null) {
				meta.setLore(lore);
			}
			item.setItemMeta(meta);
		}
	}

	public static PostSetup getpost(String sid)
	{
		return (PostSetup)plist.get(sid);
	}

	public static ArrayList<PostSetup> getpostlist()
	{
		ArrayList<PostSetup> list = new ArrayList<PostSetup>();
		PostSetup ks;
		for (Iterator<PostSetup> localIterator = plist.values().iterator(); localIterator.hasNext(); list.add(ks))
		{
			ks = (PostSetup)localIterator.next();
		}
		return list;
	}

	public static void PostList(CommandSender sender, String args)
	{
		try
		{
			int page = Integer.parseInt(args);
			int maxpage = getpostlist().size() / 9;
			if (getpostlist().size() % 9 != 0)
			{
				maxpage++;
			}
			if ((page < 1) || (page > maxpage))
			{
				sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED +
						"指定されたページ数が見つかりませんでした。");
				return;
			}
			if (getpostlist().size() > 0)
			{
				sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GOLD + "交易品の一覧を表示します。");
				for (int i = 9 * page - 9; i < page * 9; i++)
				{
					if (PostProcess.getpostlist().size() > i)
					{
						sender.sendMessage(ChatColor.RED +"[ID] " + ChatColor.RESET +
								((PostSetup)getpostlist().get(i)).getSID()
								+ ChatColor.YELLOW +" [交易所名] " + ChatColor.RESET +
								((PostSetup)getpostlist().get(i)).getName());
					}
				}
			}
			else
			{
				sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "交易所が一つも作成されていません。");
			}
		}
		catch (Exception e)
		{
			sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "エラーが発生しました。");
		}
	}

	public static void putposts(String sid, PostSetup post)
	{
		plist.put(sid, post);
	}

	public static void removepost(String sid)
	{
		plist.remove(sid);
	}
}
