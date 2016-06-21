package jp.seiya0818;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jp.seiya0818.KouekiPost.PostProcess;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;

public class Process
{
	private static Koueki main;
	private static Map<String, KouekiSetup> glist = new HashMap<String, KouekiSetup>();
	private static Economy econ = null;

	public static boolean Init(Koueki k)
	{
		main = k;
		if (!setupEconomy())
		{
			main.getLogger().warning(Koueki.LoggerPrefix + "Vaultや経済プラグインが導入されていません。");
			main.getServer().getPluginManager().disablePlugin(main);
			return false;
		}
		new BukkitRunnable()
		{
			public void run() {}
		}.runTaskLater(main, 1L);
		loadglist();
		PostProcess.loadplist();
		return true;
	}

	private static boolean setupEconomy()
	{
		if(main.getServer().getPluginManager().getPlugin("Vault") == null)
		{
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = main.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null)
		{
			return false;
		}
		econ = (Economy)rsp.getProvider();
		return econ != null;
	}

	public static Economy getEconomy()
	{
		return econ;
	}

	public static void loadglist()
	{
		glist.clear();
		if(!main.getDataFolder().exists())
		{
			main.getDataFolder().mkdir();
		}
		File file = new File(main.getDataFolder(), "goods");
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
				glist.put(sid, new KouekiSetup(sid, conf));
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

	public static Koueki getMain()
	{
		return main;
	}

	public static KouekiSetup getgoods(String sid)
	{
		return (KouekiSetup)glist.get(sid);
	}

	public static ArrayList<KouekiSetup> getgoodslist()
	{
		ArrayList<KouekiSetup> list = new ArrayList<KouekiSetup>();
		KouekiSetup ks;
		for (Iterator<KouekiSetup> localIterator = glist.values().iterator(); localIterator.hasNext(); list.add(ks))
		{
			ks = (KouekiSetup)localIterator.next();
		}
		return list;
	}

	public static void GoodsList(CommandSender sender, String args)
	{
		try
		{
			int page = Integer.parseInt(args);
			int maxpage = Process.getgoodslist().size() / 9;
			if (Process.getgoodslist().size() % 9 != 0)
			{
				maxpage++;
			}
			if ((page < 1) || (page > maxpage))
			{
				sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED +
						"指定されたページ数が見つかりませんでした。");
				return;
			}
			if (Process.getgoodslist().size() > 0)
			{
				sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GOLD + "交易品の一覧を表示します。");
				for (int i = 9 * page - 9; i < page * 9; i++)
				{
					if (Process.getgoodslist().size() > i)
					{
						sender.sendMessage(ChatColor.RED +"[ID] " + ChatColor.RESET +
								((KouekiSetup)Process.getgoodslist().get(i)).getSID()
								+ ChatColor.YELLOW +" [交易品名] " + ChatColor.RESET +
								((KouekiSetup)Process.getgoodslist().get(i)).getName());
					}
				}
			}
			else
			{
				sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "交易品が一つも作成されていません。");
			}
		}
		catch (Exception e)
		{
			sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "エラーが発生しました。");
		}
	}

	public static void putgoods(String sid, KouekiSetup goods)
	{
		glist.put(sid, goods);
	}

	public static void removegoods(String sid)
	{
		glist.remove(sid);
	}
}
