package jp.seiya0818.KouekiSystem;

import java.io.IOException;
import java.util.ArrayList;

import jp.seiya0818.Koueki;
import jp.seiya0818.Process;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KouekiHorse
{
	public static final String HorseMeta = "HorseMeta";
	static PotionEffectType Speed = PotionEffectType.SPEED;

	public static void check(Player player)
	{
		Boolean hb = checkKouekiHorse(player);
		if(hb == true)
		{
			SpawnKouekiHorse(player);
		}
		else
		{
			return;
		}
	}

	public static void checkd(Player player)
	{
		if(player.hasMetadata(HorseMeta))
		{
			for(MetadataValue meta : player.getMetadata(HorseMeta))
			{
				Horse h = (Horse) meta.value();
				h.remove();
				player.removeMetadata(HorseMeta, Koueki.instance);
			}
		}
		else
		{
			return;
		}
	}

	public static void buyKouekiHorse(Player player)
	{
		FileConfiguration conf = KouekiPoint.getPlayerConfig(player);
		Boolean hb = conf.getBoolean("Koueki-Horse");
		int points = conf.getInt("Point");
		String unit = Process.getMain().conf.getString("Point-Unit");
		if(hb == true)
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "既に所持しています。");
			return;
		}
		if(points < 10000)
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "ポイントが足りません。");
			player.sendMessage(ChatColor.BLUE + "馬の購入に必要なポイント: " + ChatColor.GOLD + 10000 + unit);
			player.sendMessage(ChatColor.BLUE + "あなたのポイント: " + ChatColor.GOLD + points + unit);
			return;
		}
		else
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "馬を購入しました。");
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "/koueki status で確認してください。");
			int totalpoint = points - 10000;
			conf.set("Point", totalpoint);
			conf.set("Koueki-Horse", true);
			save(player, conf);
		}
	}

	public static void SpawnKouekiHorse(Player player)
	{
		Horse h = (Horse) player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
		h.setTamed(true);
		h.setOwner(player);
		h.setAdult();
		h.setStyle(Style.WHITE);
		h.getInventory().setSaddle(new ItemStack(Material.SADDLE, 1));
		player.setMetadata(HorseMeta, new FixedMetadataValue(Koueki.instance, h));
		Boolean wb = KouekiTools.checkWingShoose(player);
		Boolean cb = KouekiTools.checkKouekiClothes(player);
		if(wb == true && cb != true)
		{
			h.addPotionEffect(new PotionEffect(Speed, 360000, 1/2));
		}
		if(wb != true && cb == true)
		{
			h.addPotionEffect(new PotionEffect(Speed, 360000, 1/2));
		}
		if(wb == true && cb == true)
		{
			h.addPotionEffect(new PotionEffect(Speed, 360000, 1));
		}
		else{}
	}

	public static boolean checkKouekiHorse(Player player)
	{
		Boolean hb = KouekiPoint.getPlayerConfig(player).getBoolean("Koueki-Horse");
		if(hb == true)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static ItemStack getKouekiHorseEgg()
	{
		ItemStack Egg = new ItemStack(Material.MONSTER_EGG);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GREEN + "交易に使える馬");
		ItemMeta EggMeta = Egg.getItemMeta();
		Egg.setDurability((short) 100);
		EggMeta.setDisplayName(ChatColor.GOLD + "馬");
		EggMeta.setLore(lore);
		Egg.setItemMeta(EggMeta);
		return Egg;
	}

	private static void save(Player player, FileConfiguration conf)
	{
		try
		{
			conf.save(KouekiPoint.getPointFile(player));
		}
		catch(IOException e)
		{
			Process.getMain().getLogger().warning(Koueki.LoggerPrefix + "コンフィグの保存に失敗しました。" + player);
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED +
					"エラーが発生しました。管理人に報告してください。");
		}
	}
}
