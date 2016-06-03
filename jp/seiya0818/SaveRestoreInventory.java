package jp.seiya0818;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class SaveRestoreInventory
{
	private static int tempLevel;
	private static float tempExp;

	public static File getInventoryFile(Player player)
	{
		File dafl = Process.getMain().getDataFolder();
		if (!dafl.exists())
		{
			dafl.mkdir();
		}
		File plyfl = new File(dafl, "inventory");
		if (!plyfl.exists())
		{
			plyfl.mkdir();
			setupInventoryConfig(player);
		}
		return new File(plyfl, player.getName() + ".yml");
	}

	public static FileConfiguration getInventoryConfig(Player player)
	{
		return YamlConfiguration.loadConfiguration(getInventoryFile(player));
	}

	public static void setupInventoryConfig(Player player)
	{
		FileConfiguration conf = getInventoryConfig(player);
		conf.set("Point", 0);
		try
		{
			conf.save(getInventoryFile(player));
		}
		catch(IOException e)
		{
			Process.getMain().getLogger().warning(Koueki.LoggerPrefix + "コンフィグの保存に失敗しました。" + player);
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED +
					"エラーが発生しました。管理人に報告してください。");
		}
	}

	public static void savePlayerInventory(Player player)
	{
		//アイテムをconfigに保存
		FileConfiguration conf = getInventoryConfig(player);
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
		//インベントリ・防具の消去
		player.getInventory().clear();
		player.getInventory().setArmorContents(new ItemStack[]
				{
				new ItemStack(Material.AIR),
				new ItemStack(Material.AIR),
				new ItemStack(Material.AIR),
				new ItemStack(Material.AIR),
				});

		//経験値の保存・消去
		tempLevel = player.getLevel();
		tempExp = player.getExp();
		player.setLevel(0);
		player.setExp(0);
		try
		{
			conf.save(getInventoryFile(player));
		}
		catch(IOException e)
		{
			Process.getMain().getLogger().warning(Koueki.LoggerPrefix + "コンフィグファイルの保存に失敗しました。" +
					player.getName());
		}
	}
	
	public static void restorePlayerInventory(Player player)
	{
		FileConfiguration conf = getInventoryConfig(player);
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
		
		// レベルと経験値の復帰
		player.setLevel(tempLevel);
		player.setExp(tempExp);
		
		player.updateInventory();
		
		getInventoryFile(player).delete();
	}
}
