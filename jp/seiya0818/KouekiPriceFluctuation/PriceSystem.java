package jp.seiya0818.KouekiPriceFluctuation;

import jp.seiya0818.Koueki;
import jp.seiya0818.KouekiSetup;
import jp.seiya0818.Process;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PriceSystem
{
	public static void showPriceAll()
	{

	}

	public static void showPrice(CommandSender sender, KouekiSetup g)
	{
		sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN  + "取引情報を表示します。");
		String code = KouekiPriceFluctuation.code.get(g);
		sender.sendMessage(ChatColor.BLUE + g.getSID() + " の現在の取引情報: " +
		ChatColor.GOLD + code);
	}

	public static void showPrice(Player player, KouekiSetup g)
	{
		player.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN  + "取引情報を表示します。");
		String code = KouekiPriceFluctuation.code.get(g);
		player.sendMessage(ChatColor.BLUE + g.getSID() + " の現在の取引情報: " +
		ChatColor.GOLD + code);
	}

	public static void showPriceP(CommandSender sender, KouekiSetup g)
	{
		sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN  + "取引情報を表示します。");
		String unit = Process.getMain().conf.getString("Money-Unit");
		String price = KouekiPriceFluctuation.price.get(g);
		int sprice = Integer.parseInt(price);
		sender.sendMessage(ChatColor.BLUE + g.getSID() + "の現在の取引価格: " +
		ChatColor.GOLD + sprice + unit);
	}

	public static void showAllPrice(CommandSender sender)
	{
		sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN  + "すべての取引情報を表示します。");
		for(KouekiSetup key : KouekiPriceFluctuation.number.keySet())
		{
			if(key instanceof KouekiSetup)
			{
				KouekiSetup g = (KouekiSetup) key;
				String code = KouekiPriceFluctuation.code.get(g);
				sender.sendMessage(ChatColor.BLUE + g.getSID() + "の現在の取引価格: " +
				ChatColor.GOLD + code);
			}
		}
	}

	public static void showAllPriceP(CommandSender sender)
	{
		sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN  + "すべての取引情報を表示します。");
		String unit = Process.getMain().conf.getString("Money-Unit");
		for(KouekiSetup key : KouekiPriceFluctuation.number.keySet())
		{
			if(key instanceof KouekiSetup)
			{
				KouekiSetup g = (KouekiSetup) key;
				String price = KouekiPriceFluctuation.price.get(g);
				sender.sendMessage(ChatColor.BLUE + g.getSID() + "の現在の取引価格: " +
				ChatColor.GOLD + price + unit);
			}
		}
	}

	public static String getPrice(KouekiSetup g)
	{
		return KouekiPriceFluctuation.code.get(g);
	}
}
