package jp.seiya0818.KouekiPriceFluctuation;

import jp.seiya0818.KouekiSetup;

import org.bukkit.ChatColor;

public class KouekiPriceCaseMinus
{
	public static int Case1(KouekiSetup g)
	{
		int basicp = g.getprice();
		int price = (int)Math.floor(basicp * 0.8);
		return price;
	}
	public static int Case2(KouekiSetup g)
	{
		int basicp = g.getprice();
		int price = (int)Math.floor(basicp * 0.7);
		return price;
	}
	public static int Case3(KouekiSetup g)
	{
		int basicp = g.getprice();
		int price = (int)Math.floor(basicp * 0.6);
		return price;
	}
	public static int Case4(KouekiSetup g)
	{
		int basicp = g.getprice();
		int price = (int)Math.floor(basicp * 0.5);
		return price;
	}
	public static int Case5(KouekiSetup g)
	{
		int basicp = g.getprice();
		int price = (int)Math.floor(basicp * 0.3);
		return price;
	}

	public static String Case1M()
	{
		String str = ChatColor.ITALIC + "" + ChatColor.BLUE + "▼1";
		return str;
	}
	public static String Case2M()
	{
		String str = ChatColor.ITALIC + "" + ChatColor.BLUE + "▼2";
		return str;
	}
	public static String Case3M()
	{
		String str = ChatColor.ITALIC + "" + ChatColor.BLUE + "▼3";
		return str;
	}
	public static String Case4M()
	{
		String str = ChatColor.ITALIC + "" + ChatColor.BLUE + "▼4";
		return str;
	}
	public static String Case5M()
	{
		String str = ChatColor.ITALIC + "" + ChatColor.BLUE + "▼5";
		return str;
	}
}
