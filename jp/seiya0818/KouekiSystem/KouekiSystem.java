package jp.seiya0818.KouekiSystem;

import jp.seiya0818.Koueki;
import jp.seiya0818.KouekiSetup;
import jp.seiya0818.KouekiSound;
import jp.seiya0818.Process;
import jp.seiya0818.KouekiPost.PostProcess;
import jp.seiya0818.KouekiPost.PostSetup;
import jp.seiya0818.KouekiPriceFluctuation.KouekiPriceFluctuation;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffectType;
//import org.bukkit.metadata.FixedMetadataValue;


//交易モードを開始
public class KouekiSystem
{
	//private final static String KouekiMeta = "交易";
	//private static Koueki instance;
	public final static String KouekiMeta = "KouekiMeta";


	public static void StartKoueki(Player player, KouekiSetup g)
	{
		//キットが未設定の場合
		FileConfiguration conf = g.getConfig();
		PostSetup p = PostProcess.getpost(conf.getString("Post"));
		if(player.hasMetadata(KouekiMeta))
		{
			player.sendMessage("");
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "既に交易中です");
			player.sendMessage("");
			return;
		}
		if(conf.getString("items") == null)
		{
			player.sendMessage("");
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "キットが設定されていません。");
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "管理人に報告してください。");
			player.sendMessage("");
			return;
		}
		if(g.getpost() == null || p == null)
		{
			player.sendMessage("");
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "交易所が設定されていません。");
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "管理人に報告してください。");
			player.sendMessage("");
			return;
		}
		double money = (double) g.getprice();
		EconomyResponse r = Process.getEconomy().withdrawPlayer(player, money);
		if(r.transactionSuccess())
		{
			player.sendMessage("");
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.LIGHT_PURPLE + money +" sei 支払いました");
		}
		//お金が足りなかった場合交易させない。
		else
		{
			player.sendMessage("");
			player.sendMessage(Koueki.PlayerPrefix + "お金が足りません。");
			player.sendMessage("");
			return;
		}
		//お金が足りた場合
		SaveRestoreInventory.savePlayerInventory(player);
		g.giveKouekiInventory(player);
		KouekiTools.check(player);
		KouekiHorse.check(player);
		FeedHealPlayer.fhp(player);
		player.setGameMode(GameMode.ADVENTURE);
		player.setMetadata(KouekiMeta, new FixedMetadataValue(Koueki.instance, g));
		player.setMetadata(PostSetup.PostMeta, new FixedMetadataValue(Koueki.instance, p));
		player.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "交易モードを開始しました");
		player.sendMessage("");
		KouekiSound.playStartKouekiSound(player);
	}


	public static void QuitKoueki(Player player, KouekiSetup g, PostSetup p)
	{
		double mag = 1.0;
		if(!player.hasMetadata(KouekiMeta) || !player.hasMetadata(PostSetup.PostMeta))
		{
			player.sendMessage("");
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "交易中ではありません");
			player.sendMessage("");
			return;
		}
		for(MetadataValue meta : player.getMetadata(PostSetup.PostMeta))
		{
			/*
			 * @p - 終了する交易所
			 * @pp - 開始した交易所
			 */
			PostSetup pp = (PostSetup) meta.value();
			if(pp == p)
			{
				player.sendMessage("");
				player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "同じ交易所へは販売できません。");
				player.sendMessage("");
				return;
			}
			mag = p.getConfig().getDouble(pp.getSID());
		}
		double money = Integer.parseInt(KouekiPriceFluctuation.price.get(g)) * mag;
		EconomyResponse r = Process.getEconomy().depositPlayer(player, money);
		if(r.transactionSuccess())
		{
			player.sendMessage("");
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.LIGHT_PURPLE + money +"sei 受け取りました。");
		}
		//お金が足りなかった場合交易させない。
		else
		{
			player.sendMessage("");
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "エラーが発生しました。管理人に報告してください。");
			player.sendMessage("");
			return;
		}
		//その後の処理
		player.removeMetadata(PostSetup.PostMeta, Koueki.instance);
		player.removeMetadata(KouekiMeta, Koueki.instance);
		KouekiHorse.checkd(player);
		SaveRestoreInventory.clearInventory(player);
		SaveRestoreInventory.restorePlayerInventory(player);
		player.removePotionEffect(PotionEffectType.SPEED);
		player.setGameMode(GameMode.SURVIVAL);
		int point = g.getpoint();
		KouekiPoint.addPoint(player, point);
		player.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "交易" +
				"を終了しました");
		player.sendMessage("");
		KouekiSound.playquitKouekiSound(player);
	}

	public static void StopKoueki(Player player,KouekiSetup g)
	{
		String str = KouekiPriceFluctuation.price.get(g);
		double money = Math.floor(Integer.parseInt(str) * 0.5);
		EconomyResponse r = Process.getEconomy().depositPlayer(player, money);
		if(r.transactionSuccess())
		{
			player.sendMessage("");
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.LIGHT_PURPLE + money +" sei 返金されました。");
		}
		//お金が足りなかった場合交易させない。
		else
		{
			player.sendMessage("");
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "エラーが発生しました。管理人に報告してください。");
			player.sendMessage("");
			return;
		}
		player.removeMetadata(KouekiMeta, Koueki.instance);
		KouekiHorse.checkd(player);
		SaveRestoreInventory.clearInventory(player);
		SaveRestoreInventory.restorePlayerInventory(player);
		player.removePotionEffect(PotionEffectType.SPEED);
		player.setGameMode(GameMode.SURVIVAL);
		KouekiSound.playstopKouekiSound(player);
	}
}
