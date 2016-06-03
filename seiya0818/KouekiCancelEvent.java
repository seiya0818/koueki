package jp.seiya0818;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class KouekiCancelEvent implements Listener
{
	public KouekiCancelEvent()
	{
		Process.getMain().getServer().getPluginManager().registerEvents(this, Process.getMain());
	}
	
	@EventHandler
	public void onClickInventory(InventoryClickEvent e)
	{
		Player player = (Player) e.getWhoClicked();
		if(player.hasMetadata(KouekiSystem.KouekiMeta))
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e)
	{
		Player player = (Player) e.getPlayer();
		if(player.hasMetadata(KouekiSystem.KouekiMeta))
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "交易中はテレポートできません");
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEnterBed(PlayerBedEnterEvent e)
	{
		Player player = (Player) e.getPlayer();
		if(player.hasMetadata(KouekiSystem.KouekiMeta))
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "交易中は寝ることができません");
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onChangeFoodLevel(FoodLevelChangeEvent e)
	{
		Player player = (Player) e.getEntity();
		if(player.hasMetadata(KouekiSystem.KouekiMeta))
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDropItem(PlayerDropItemEvent e)
	{
		Player player = (Player) e.getPlayer();
		if(player.hasMetadata(KouekiSystem.KouekiMeta))
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "交易品を投げないでください");
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onConsumeItem(PlayerItemConsumeEvent e)
	{
		Player player = (Player) e.getPlayer();
		if(player.hasMetadata(KouekiSystem.KouekiMeta))
		{
			player.sendMessage(Koueki.PlayerPrefix +
		    ChatColor.RED + "交易品を食べないでください、( ﾟ,_ゝﾟ)");
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onMoveAnotherWorld(PlayerChangedWorldEvent e)
	{
		Player player = (Player) e.getPlayer();
		if(player.hasMetadata(KouekiSystem.KouekiMeta))
		{
			KouekiSystem.StopKoueki(player);
		}
	}
	
	@EventHandler
	public void onChangeFlyMode(PlayerToggleFlightEvent e)
	{
		Player player = (Player) e.getPlayer();
		if(player.hasMetadata(KouekiSystem.KouekiMeta))
		{
			player.sendMessage(Koueki.PlayerPrefix +
			ChatColor.RED + "Flyモード変更されたため交易を強制終了しました");
			KouekiSystem.StopKoueki(player);
		}
	}
	
	@EventHandler
	public void onChangeGameMode(PlayerGameModeChangeEvent e)
	{
		Boolean GameMode = Process.getMain().conf.getBoolean("Anti-Change-GameMode");
		if(GameMode == false)
			return;
		Player player = (Player) e.getPlayer();
		if(player.hasMetadata(KouekiSystem.KouekiMeta))
		{
			player.sendMessage(Koueki.PlayerPrefix +
			ChatColor.RED + "ゲームモードが変更されたため交易を強制終了しました");
			KouekiSystem.StopKoueki(player);
		}
	}
	
	@EventHandler
	public void onLogout(PlayerQuitEvent e)
	{
		Player player = (Player) e.getPlayer();
		if(player.hasMetadata(KouekiSystem.KouekiMeta))
		{
			KouekiSystem.StopKoueki(player);
		}
	}
	
	@EventHandler
	public void afterDeath(PlayerRespawnEvent e)
	{
		Player player = (Player) e.getPlayer();
		if(player.hasMetadata(KouekiSystem.KouekiMeta))
		{
			KouekiSystem.StopKoueki(player);
		}
	}
}
