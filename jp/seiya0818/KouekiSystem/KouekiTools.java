package jp.seiya0818.KouekiSystem;

import java.io.IOException;
import java.util.ArrayList;

import jp.seiya0818.Koueki;
import jp.seiya0818.Process;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KouekiTools
{
	static PotionEffectType Speed = PotionEffectType.SPEED;

	public static void check(Player player)
	{
		Boolean wb = checkWingShoose(player);
		Boolean cb = checkKouekiClothes(player);
		if(wb == true)
		{
			giveWingShoose(player);
		}
		if(cb == true)
		{
			giveKouekiClothes(player);
		}
		else
		{
			return;
		}
	}

	public static void status(Player player)
	{
		Boolean wb = checkWingShoose(player);
		Boolean cb = checkKouekiClothes(player);
		Boolean hb = KouekiHorse.checkKouekiHorse(player);
		player.sendMessage(Koueki.PlayerPrefix + ChatColor.BLUE + "あなたの情報を表示します");
		if(wb == true)
		{
			player.sendMessage(ChatColor.AQUA + "ウィングシューズ: " + ChatColor.GOLD + "所持しています。");
		}
		if(wb == false)
		{
			player.sendMessage(ChatColor.AQUA + "ウィングシューズ: " + ChatColor.GOLD + "所持していません。");
		}
		if(cb == true)
		{
			player.sendMessage(ChatColor.AQUA  + "交易服: " + ChatColor.GOLD + "所持しています。");
		}
		if(cb == false)
		{
			player.sendMessage(ChatColor.AQUA  + "交易服: " + ChatColor.GOLD + "所持していません。");
		}
		if(hb == true)
		{
			player.sendMessage(ChatColor.AQUA  + "交易馬: " + ChatColor.GOLD + "所持しています。");
		}
		if(hb == false)
		{
			player.sendMessage(ChatColor.AQUA  + "交易馬: " + ChatColor.GOLD + "所持していません。");

		}
		/*
		 * player.sendMessage(ChatColor.RED + "エラーが発生しました。管理人に報告してください。");
		 */
	}

	public static void buyWingShoose(Player player)
	{
		FileConfiguration conf = KouekiPoint.getPlayerConfig(player);
		Boolean wb = conf.getBoolean("Wing-Shoose");
		int points = conf.getInt("Point");
		String unit = Process.getMain().conf.getString("Point-Unit");
		if(wb == true)
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "既に所持しています。");
			return;
		}
		if(points < 1000)
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "ポイントが足りません。");
			player.sendMessage(ChatColor.BLUE + "ウィングシューズの購入に必要なポイント: " + ChatColor.GOLD + 1000 + unit);
			player.sendMessage(ChatColor.BLUE + "あなたのポイント: " + ChatColor.GOLD + points + unit);
			return;
		}
		else
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "ウィングシューズを購入しました。");
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "/koueki status で確認してください。");
			int totalpoint = points - 1000;
			conf.set("Point", totalpoint);
			conf.set("Wing-Shoose", true);
			save(player, conf);
		}
	}

	public static void buyKouekiClothes(Player player)
	{
		FileConfiguration conf = KouekiPoint.getPlayerConfig(player);
		Boolean cb = conf.getBoolean("Koueki-Clothes");
		int points = conf.getInt("Point");
		String unit = Process.getMain().conf.getString("Point-Unit");
		if(cb == true)
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "既に所持しています。");
			return;
		}
		if(points < 3000)
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "ポイントが足りません。");
			player.sendMessage(ChatColor.BLUE + "交易服の購入に必要なポイント: " + ChatColor.GOLD + 3000 + unit);
			player.sendMessage(ChatColor.BLUE + "あなたのポイント: " + ChatColor.GOLD + points + unit);
			return;
		}
		else
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "交易服を購入しました。");
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "/koueki status で確認してください。");
			conf.set("Koueki-Clothes", true);
			save(player, conf);
		}
	}

	public static boolean checkWingShoose(Player player)
	{
		Boolean wb = KouekiPoint.getPlayerConfig(player).getBoolean("Wing-Shoose");
		if(wb == true)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static boolean checkKouekiClothes(Player player)
	{
		Boolean wb = KouekiPoint.getPlayerConfig(player).getBoolean("Koueki-Clothes");
		if(wb == true)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private static void giveWingShoose(Player player)
	{
		PlayerInventory inv = player.getInventory();
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GREEN + "浮きながら走れる。(設定)");
		LeatherArmorMeta bootsmeta = (LeatherArmorMeta) boots.getItemMeta();
		bootsmeta.setColor(Color.RED);
		bootsmeta.setDisplayName(ChatColor.GOLD + "ウィングシューズ");
		bootsmeta.setLore(lore);
		boots.setItemMeta(bootsmeta);
		player.addPotionEffect(new PotionEffect(Speed, 360000, 1/2));
		inv.setBoots(boots);
	}

	private static void giveKouekiClothes(Player player)
	{
		Boolean cb = checkWingShoose(player);
		PlayerInventory inv = player.getInventory();
		ItemStack chestp = new ItemStack(Material.LEATHER_CHESTPLATE);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GREEN + "足が速くなる服。構造は謎。");
		LeatherArmorMeta chestpmeta = (LeatherArmorMeta) chestp.getItemMeta();
		chestpmeta.setColor(Color.BLUE);
		chestpmeta.setDisplayName(ChatColor.GOLD + "交易服");
		chestpmeta.setLore(lore);
		chestp.setItemMeta(chestpmeta);
		inv.setChestplate(new ItemStack(chestp));
		if(cb == true)
		{
			player.addPotionEffect(new PotionEffect(Speed, 360000, 1));
		}
		else
		{
			player.addPotionEffect(new PotionEffect(Speed, 360000, 1/2));
		}
	}
	public static ItemStack getWingShoose()
	{
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GREEN + "浮きながら走れる。(設定)");
		LeatherArmorMeta bootsmeta = (LeatherArmorMeta) boots.getItemMeta();
		bootsmeta.setColor(Color.RED);
		bootsmeta.setDisplayName(ChatColor.GOLD + "ウィングシューズ");
		bootsmeta.setLore(lore);
		boots.setItemMeta(bootsmeta);
		return boots;
	}

	public static ItemStack getKouekiClothes()
	{
		ItemStack chestp = new ItemStack(Material.LEATHER_CHESTPLATE);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GREEN + "足が速くなる服。構造は謎。");
		LeatherArmorMeta chestpmeta = (LeatherArmorMeta) chestp.getItemMeta();
		chestpmeta.setColor(Color.BLUE);
		chestpmeta.setDisplayName(ChatColor.GOLD + "交易服");
		chestpmeta.setLore(lore);
		chestp.setItemMeta(chestpmeta);
		return chestp;
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
