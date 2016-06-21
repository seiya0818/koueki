package jp.seiya0818;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class KouekiSetup
{
	private String sid;
	private String name;
	private String post;
	private int price;
	private int point;
	private String GUIitem;
	public static final char COLOR_KEY = '&';

	public KouekiSetup(String sid)
	{
		setSID(sid);
		setname("新規交易品");
		setprice(1000);
		setpoint(10);

		saveConfig();
	}

	public KouekiSetup(String sid, FileConfiguration conf)
	{
		setSID(sid);
		loadConfig(conf);
	}

	public void saveConfig()
	{
		FileConfiguration conf = getConfig();
		conf.set("Name", this.name);
		conf.set("Price", this.price);
		conf.set("Point", this.point);
		conf.set("Post", this.post);
		try
		{
			conf.save(getFile());
		}
		catch(IOException e)
		{
			Process.getMain().getLogger().warning(Koueki.LoggerPrefix + "コンフィグの保存に失敗しました。" + this.sid);
		}
	}

	public void delete()
	{
		getFile().delete();
		Process.removegoods(this.sid);
	}

	  public File getFile()
	  {
	    File dafl = Process.getMain().getDataFolder();
	    if (!dafl.exists())
	    {
	      dafl.mkdir();
	    }
	    File gdsfl = new File(dafl, "goods");
	    if (!gdsfl.exists())
	    {
	      gdsfl.mkdir();
	    }
	    return new File(gdsfl, this.sid + ".yml");
	  }

	public FileConfiguration getConfig()
	{
	    return YamlConfiguration.loadConfiguration(getFile());
	}

	private void loadConfig(FileConfiguration conf)
	{
		setname(conf.getString("Name"));
		setprice(conf.getInt("Price"));
		setpoint(conf.getInt("Point"));
		setpost(conf.getString("Post"));
		setGUIItem(conf.getString("GUIItem"));
	}

	public static void saveItem(Player player, KouekiSetup g)
	{
		FileConfiguration conf = g.getConfig();
		if(player.getItemInHand() != null)
		{
			if(!player.getItemInHand().getType().equals(Material.AIR))
			{
				ItemStack item = player.getItemInHand();
				String name = item.getType().toString();
				int dur = item.getDurability();
				String string = name + ":" + dur;
				conf.set("GUIItem", string);
				g.save(conf);
				g.setGUIItem(string);
				return;
			}
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "アイテムを手に持ってください。");
			return;
		}
		player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "アイテムを手に持ってください。");
		return;
	}

	public static boolean checkItemInHand(Player player)
	{
		if(player.getItemInHand() == null)
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "アイテムを手に持ってください。");
			return false;
		}
		if(player.getItemInHand().getType().equals(Material.AIR))
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "アイテムを手に持ってください。");
			return false;
		}
		else
		{
			return true;
		}
	}

	public void saveKouekiInventory(Player player)
	{
		FileConfiguration conf = getConfig();
		PlayerInventory inv = player.getInventory();
		for(int i = 0; i < 36; i++)
		{
			ItemStack is = inv.getItem(i);
			if(is == null || is.getType() == Material.AIR)
				continue;

			String slot = "items." + i;
			conf.set(slot + ".type", is.getType().toString().toLowerCase());
			conf.set(slot + ".amount", is.getAmount());
			if(!is.hasItemMeta())
				continue;
			if(is.getItemMeta().hasDisplayName())
				conf.set(slot + ".name", is.getItemMeta().getDisplayName());
			if(is.getItemMeta().hasLore())
				conf.set(slot + ".lore", is.getItemMeta().getLore());
			if(is.getItemMeta().hasEnchants())
			{
				Map<Enchantment, Integer> enchants = is.getEnchantments();
				List<String> enchantList = new ArrayList<String>();
				for (Enchantment e : is.getEnchantments().keySet())
				{
					int level = enchants.get(e);
					enchantList.add(e.getName().toLowerCase() + ":" + level);
				}
				conf.set(slot + ".enchants", enchantList);
			}
			continue;
		}
		conf.set("armor.helmet", inv.getHelmet() != null ?
				inv.getHelmet().getType().toString().toLowerCase() : "air");
		conf.set("armor.chestplate", inv.getChestplate() != null ?
				inv.getChestplate().getType().toString().toLowerCase() : "air");

		conf.set("armor.leggings", inv.getLeggings() != null ?
				inv.getLeggings().getType().toString().toLowerCase() : "air");

		conf.set("armor.boots", inv.getBoots() != null ?
				inv.getBoots().getType().toString().toLowerCase() : "air");
		save(conf);
	}

	public void giveKouekiInventory(Player player)
	{
		FileConfiguration conf = getConfig();
		ConfigurationSection s = conf.getConfigurationSection("items");
		for(String str : s.getKeys(false))
		{
			int slot = Integer.parseInt(str);
			if(0 > slot && slot > 36)
				return;
			String string = "items." + slot + ".";
			String type = conf.getString(string + "type");
			String name = conf.getString(string + "name");
			List<String> lore = conf.getStringList(string + "lore");
			List<String> enchants = conf.getStringList(string + "enchants");
			int amount = conf.getInt(string + "amount");
			ItemStack is = new ItemStack(Material.matchMaterial(type.toUpperCase()), amount);
			ItemMeta im = is.getItemMeta();

			if(im == null)
				continue;
			if(name != null)
				im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
			if(lore != null)
				im.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lore.toString())));
			if(enchants != null)
			{
				for (String s1 : enchants)
				{
					String[] indiEnchants = s1.split(":");
					im.addEnchant(Enchantment.getByName(indiEnchants[0].toUpperCase()),
							Integer.parseInt(indiEnchants[1]), true);
				}
			}
			is.setItemMeta(im);
			player.getInventory().setItem(slot, is);
		}
		String helmet = conf.getString("armor.helmet").toUpperCase();
		String chestplate = conf.getString("armor.chestplate").toUpperCase();
		String leggings = conf.getString("armor.leggings").toUpperCase();
		String boots = conf.getString("armor.boots").toUpperCase();
		player.getInventory().setHelmet(new ItemStack(helmet != null ? Material.matchMaterial(helmet) : Material.AIR));
		player.getInventory().setChestplate(new ItemStack(chestplate != null ? Material.matchMaterial(chestplate) : Material.AIR));
		player.getInventory().setLeggings(new ItemStack(leggings != null ? Material.matchMaterial(leggings) : Material.AIR));
		player.getInventory().setBoots(new ItemStack(boots != null ? Material.matchMaterial(boots) : Material.AIR));
		player.updateInventory();
	}

	public void setName(String name)
	{
		FileConfiguration conf = getConfig();
		conf.set("Name", name);
		save(conf);
	}

	public void setPost(String post)
	{
		FileConfiguration conf = getConfig();
		conf.set("Post", post);
		setpost(post);
		save(conf);
	}

	public void setSID(String sid)
	{
		this.sid = sid;
	}

	public void setname(String name)
	{
		this.name = name;
	}

	public void setprice(int price)
	{
		this.price = price;
	}

	public void setpoint(int point)
	{
		this.point = point;
	}

	public void setpost(String post)
	{
		this.post = post;
	}

	public void setGUIItem(String item)
	{
		this.GUIitem = item;
	}

	public String getSID()
	{
		return this.sid;
	}

	public String getName()
	{
		return this.name;
	}


	public int getprice()
	{
		return this.price;
	}

	public int getpoint()
	{
		return this.point;
	}

	public String getpost()
	{
		return this.post;
	}

	public String getGUIItem()
	{
		return this.GUIitem;
	}

	private void save(FileConfiguration conf)
	{
		try
		{
			conf.save(getFile());
		}
		catch(IOException e)
		{
			Process.getMain().getLogger().warning(Koueki.LoggerPrefix + "コンフィグファイルの保存に失敗しました。" + this.sid);
		}
	}
}
