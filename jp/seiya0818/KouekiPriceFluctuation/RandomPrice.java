package jp.seiya0818.KouekiPriceFluctuation;


import jp.seiya0818.Koueki;
import jp.seiya0818.Process;

import org.bukkit.scheduler.BukkitRunnable;

public class RandomPrice
{
	static Koueki plugin;

	public RandomPrice(Koueki plugin)
	{
		RandomPrice.plugin = plugin;
	}

	public static void startTask()
	{
		BukkitRunnable timer = new BukkitRunnable()
		{
			public void run()
			{
				putnumber();
				return;
			}
		};
		timer.runTaskTimer(plugin, 0L, 20 * 300);
		plugin.timers.put("startTask", timer);
	}
	public static void delayTask(int delay)
	{
		BukkitRunnable timer = new BukkitRunnable()
		{
			public void run()
			{
				if(RandomPrice.plugin.timers.containsKey("delayTask"))
				{
					((BukkitRunnable)RandomPrice.plugin.timers.get("delayTask")).cancel();
					RandomPrice.plugin.timers.remove("delayTask");
				}
				RandomPrice.startTask();
			}
		};
		timer.runTaskLater(plugin, 20 * delay);
		plugin.timers.put("delayTask", timer);
	}

	public static void putnumber()
	{
		int figure = Process.getgoodslist().size();
		KouekiPriceFluctuation.number.clear();
		KouekiPriceFluctuation.code.clear();
		KouekiPriceFluctuation.price.clear();
		for(int i = 0; i < figure; i++)
		{
			int number;
			number = (int)(Math.random() * 7)+ 4;
			String str = String.valueOf(number);
			KouekiPriceFluctuation.number.put(Process.getgoodslist().get(i), str);
;		}
		KouekiPriceFluctuation.updatePrice();
	}
}
