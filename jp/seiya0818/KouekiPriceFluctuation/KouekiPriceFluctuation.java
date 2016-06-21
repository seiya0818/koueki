package jp.seiya0818.KouekiPriceFluctuation;

import java.util.HashMap;

import jp.seiya0818.KouekiSetup;
import net.md_5.bungee.api.ChatColor;


public class KouekiPriceFluctuation
{
	public static HashMap <KouekiSetup, String> number = new HashMap<KouekiSetup, String>();
	public static HashMap <KouekiSetup, String> code = new HashMap<KouekiSetup, String>();
	public static HashMap <KouekiSetup, String> price = new HashMap<KouekiSetup, String>();
	public static final String m = ChatColor.BLUE + "の取引相場: ";

	public static void updatePrice()
	{
		for(KouekiSetup key : number.keySet())
		{
			if(key instanceof KouekiSetup)
			{
				KouekiSetup g = (KouekiSetup) key;
				String str = number.get(key);
				int figure = Integer.parseInt(str);
				if(figure == 1)
				{
					Case1(g);
				}
				if(figure == 2)
				{
					Case2(g);
				}
				if(figure == 3)
				{
					Case3(g);
				}
				if(figure == 4)
				{
					Case4(g);
				}
				if(figure == 5)
				{
					Case5(g);
				}
				if(figure == 6)
				{
					Case6(g);
				}
				if(figure == 7)
				{
					Case7(g);
				}
				if(figure == 8)
				{
					Case8(g);
				}
				if(figure == 9)
				{
					Case9(g);
				}
				if(figure == 10)
				{
					Case10(g);
				}
				if(figure == 11)
				{
					Case11(g);
				}
				if(figure == 12)
				{
					Case12(g);
				}
				if(figure == 13)
				{
					Case13(g);
				}
				if(figure == 14)
				{
					Case14(g);
				}
				if(figure == 15)
				{
					Case15(g);
				}
				if(figure == 16)
				{
					Case16(g);
				}
				if(figure == 17)
				{
					Case17(g);
				}
				if(figure == 18)
				{
					Case18(g);
				}
				if(figure == 19)
				{
					Case19(g);
				}
				if(figure == 20)
				{
					Case20(g);
				}
			}
			else
			{
			}
		}
	}

	public static void Case1(KouekiSetup g)
	{
		int sprice = KouekiPriceCaseMinus.Case5(g);
		String str = String.valueOf(sprice);
		String message = KouekiPriceCaseMinus.Case5M();
		KouekiPriceFluctuation.price.put(g, str);
		KouekiPriceFluctuation.code.put(g, message);
	}
	public static void Case2(KouekiSetup g)
	{
		int sprice = KouekiPriceCaseMinus.Case4(g);
		String str = String.valueOf(sprice);
		String message = KouekiPriceCaseMinus.Case4M();
		price.put(g, str);
		code.put(g, message);
	}
	public static void Case3(KouekiSetup g)
	{
		int sprice = KouekiPriceCaseMinus.Case3(g);
		String str = String.valueOf(sprice);
		String message = KouekiPriceCaseMinus.Case3M();
		price.put(g, str);
		code.put(g, message);
	}
	public static void Case4(KouekiSetup g)
	{
		int sprice = KouekiPriceCaseMinus.Case2(g);
		String str = String.valueOf(sprice);
		String message = KouekiPriceCaseMinus.Case2M();
		price.put(g, str);
		code.put(g, message);
	}
	public static void Case5(KouekiSetup g)
	{
		int sprice = KouekiPriceCaseMinus.Case1(g);
		String str = String.valueOf(sprice);
		String message = KouekiPriceCaseMinus.Case1M();
		price.put(g, str);
		code.put(g, message);
	}
	public static void Case6(KouekiSetup g)
	{
		int sprice = KouekiPriceCasePlus.Case7(g);
		String str = String.valueOf(sprice);
		String message = KouekiPriceCasePlus.Case7M();
		price.put(g, str);
		code.put(g, message);
	}
	public static void Case7(KouekiSetup g)
	{
		int sprice = KouekiPriceCasePlus.Case8(g);
		String str = String.valueOf(sprice);
		String message = KouekiPriceCasePlus.Case8M();
		price.put(g, str);
		code.put(g, message);
	}
	public static void Case8(KouekiSetup g)
	{
		int sprice = KouekiPriceCasePlus.Case8(g);
		String str = String.valueOf(sprice);
		String message = KouekiPriceCasePlus.Case8M();
		price.put(g, str);
		code.put(g, message);
	}
	public static void Case9(KouekiSetup g)
	{
		int sprice = KouekiPriceCasePlus.Case9(g);
		String str = String.valueOf(sprice);
		String message = KouekiPriceCasePlus.Case9M();
		price.put(g, str);
		code.put(g, message);
	}
	public static void Case10(KouekiSetup g)
	{
		int sprice = KouekiPriceCasePlus.Case10(g);
		String str = String.valueOf(sprice);
		String message = KouekiPriceCasePlus.Case10M();
		price.put(g, str);
		code.put(g, message);
	}
	public static void Case11(KouekiSetup g)
	{
		int sprice = KouekiPriceCasePlus.Case11(g);
		String str = String.valueOf(sprice);
		String message = KouekiPriceCasePlus.Case11M();
		price.put(g, str);
		code.put(g, message);
	}
	public static void Case12(KouekiSetup g)
	{
		int sprice = KouekiPriceCasePlus.Case12(g);
		String str = String.valueOf(sprice);
		String message = KouekiPriceCasePlus.Case12M();
		price.put(g, str);
		code.put(g, message);
	}
	public static void Case13(KouekiSetup g)
	{
		int sprice = KouekiPriceCasePlus.Case13(g);
		String str = String.valueOf(sprice);
		String message = KouekiPriceCasePlus.Case13M();
		price.put(g, str);
		code.put(g, message);
	}
	public static void Case14(KouekiSetup g)
	{
		int sprice = KouekiPriceCasePlus.Case14(g);
		String str = String.valueOf(sprice);
		String message = KouekiPriceCasePlus.Case14M();
		price.put(g, str);
		code.put(g, message);
	}
	public static void Case15(KouekiSetup g)
	{
		int sprice = KouekiPriceCasePlus.Case15(g);
		String str = String.valueOf(sprice);
		String message = KouekiPriceCasePlus.Case15M();
		price.put(g, str);
		code.put(g, message);
	}
	public static void Case16(KouekiSetup g)
	{
		int sprice = KouekiPriceCasePlus.Case16(g);
		String str = String.valueOf(sprice);
		String message = KouekiPriceCasePlus.Case16M();
		price.put(g, str);
		code.put(g, message);
	}
	public static void Case17(KouekiSetup g)
	{
		int sprice = KouekiPriceCasePlus.Case17(g);
		String str = String.valueOf(sprice);
		String message = KouekiPriceCasePlus.Case17M();
		price.put(g, str);
		code.put(g, message);
	}
	public static void Case18(KouekiSetup g)
	{
		int sprice = KouekiPriceCasePlus.Case18(g);
		String str = String.valueOf(sprice);
		String message = KouekiPriceCasePlus.Case18M();
		price.put(g, str);
		code.put(g, message);
	}
	public static void Case19(KouekiSetup g)
	{
		int sprice = KouekiPriceCasePlus.Case19(g);
		String str = String.valueOf(sprice);
		String message = KouekiPriceCasePlus.Case19M();
		price.put(g, str);
		code.put(g, message);
	}
	public static void Case20(KouekiSetup g)
	{
		int sprice = KouekiPriceCasePlus.Case20(g);
		String str = String.valueOf(sprice);
		String message = KouekiPriceCasePlus.Case20M();
		price.put(g, str);
		code.put(g, message);
	}
}
