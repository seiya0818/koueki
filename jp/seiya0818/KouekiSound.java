package jp.seiya0818;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class KouekiSound
{
	public static void playStartKouekiSound(Player player)
	{
		player.getWorld().playSound(player.getLocation(),Sound.NOTE_PIANO, 1, 1);
	}

	public static void playquitKouekiSound(Player player)
	{
		player.getWorld().playSound(player.getLocation(),Sound.LEVEL_UP, 1, 1);
	}

	public static void playstopKouekiSound(Player player)
	{
		player.getWorld().playSound(player.getLocation(),Sound.WOLF_WHINE, 1, 1);
	}

	public static void playHelpSound(Player player)
	{
		player.getWorld().playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 5, 1);
	}
}
