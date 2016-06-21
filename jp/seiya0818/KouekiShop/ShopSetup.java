package jp.seiya0818.KouekiShop;

import java.io.File;
import java.io.IOException;

import jp.seiya0818.Koueki;
import jp.seiya0818.Process;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShopSetup
{
	private String sid;
	private String post;
	private String item;

	public ShopSetup(String sid)
	{
		setSID(sid);

		saveConfig();
	}

	public ShopSetup(String sid, FileConfiguration conf)
	{
		setSID(sid);
		loadConfig(conf);
	}

	public void saveConfig()
	{
		FileConfiguration conf = getConfig();
		conf.set("Post", this.post);
		conf.set("Item", this.item);
		save(conf);
	}

	public void delete()
	{
		getFile().delete();
		ShopProcess.removeshop(this.sid);
	}

	public File getFile()
	{
		File dafl = Process.getMain().getDataFolder();
		if (!dafl.exists())
		{
			dafl.mkdir();
		}
		File spsfl = new File(dafl, "shops");
		if (!spsfl.exists())
		{
			spsfl.mkdir();
		}
		return new File(spsfl, this.sid + ".yml");
	}

	public FileConfiguration getConfig()
	{
		return YamlConfiguration.loadConfiguration(getFile());
	}

	private void loadConfig(FileConfiguration conf)
	{
		setpost(conf.getString("Post"));
		setitem(conf.getString("Item"));
	}

	public static void saveItem(Player player, ShopSetup s)
	{
		FileConfiguration conf = s.getConfig();
		if(player.getItemInHand() != null)
		{
			if(!player.getItemInHand().getType().equals(Material.AIR))
			{
				ItemStack item = player.getItemInHand();
				String name = item.getType().toString();
				int dur = item.getDurability();
				int amount = item.getAmount();
				String string = name + ":" + dur + amount;
				conf.set("Item", string);
				s.save(conf);
				s.setitem(string);
				return;
			}
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "アイテムを手に持ってください。");
			return;
		}
		player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "アイテムを手に持ってください。");
		return;
	}

	public void setSID(String sid)
	{
		this.sid = sid;
	}

	public void setpost(String post)
	{
		this.post = post;
	}

	public void setitem(String item)
	{
		this.item = item;
	}

	public String getSID()
	{
		return this.sid;
	}

	public String getpost()
	{
		return this.post;
	}

	public String getitem()
	{
		return this.item;
	}

	private void save(FileConfiguration conf)
	{
		try
		{
			conf.save(getFile());
		}
		catch(IOException e)
		{
			Process.getMain().getLogger().warning(Koueki.LoggerPrefix + "コンフィグの保存に失敗しました。" + this.sid);
		}
	}
}
