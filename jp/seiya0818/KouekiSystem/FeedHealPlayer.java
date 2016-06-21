package jp.seiya0818.KouekiSystem;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class FeedHealPlayer
{
	public static void fhp(Player player)
	{
		player.setFoodLevel(20);
		player.setHealth(((CraftPlayer)player).getMaxHealth());
	}
}
