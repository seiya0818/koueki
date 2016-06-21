package jp.seiya0818.KouekiSystem;

import jp.seiya0818.Koueki;
import jp.seiya0818.KouekiSetup;
import jp.seiya0818.Process;

import org.bukkit.ChatColor;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.metadata.MetadataValue;

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
			if(player.isInsideVehicle() == true)
			{
				if(player.getVehicle() instanceof Horse){}
			}
			else
			{
				player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "交易中はテレポートできません");
				e.setCancelled(true);
			}
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
	public void onChangeItemDamage(PlayerItemDamageEvent e)
	{
		Player player = (Player) e.getPlayer();
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
			for(MetadataValue meta : player.getMetadata(KouekiSystem.KouekiMeta))
			{
				KouekiSetup g = (KouekiSetup) meta.value();
				KouekiSystem.StopKoueki(player, g);
				player.sendMessage("");
			}
		}
	}

	@EventHandler
	public void onChangeFlyMode(PlayerToggleFlightEvent e)
	{
		Player player = (Player) e.getPlayer();
		if(player.hasMetadata(KouekiSystem.KouekiMeta))
		{
			for(MetadataValue meta : player.getMetadata(KouekiSystem.KouekiMeta))
			{
				KouekiSetup g = (KouekiSetup) meta.value();
				player.sendMessage(Koueki.PlayerPrefix +
						ChatColor.RED + "Flyモード変更されたため交易を強制終了しました");
				KouekiSystem.StopKoueki(player, g);
			}
		}
	}

	@EventHandler
	public void onChangeGameMode(PlayerGameModeChangeEvent e)
	{
		Player player = (Player) e.getPlayer();
		if(player.hasMetadata(KouekiSystem.KouekiMeta))
		{
			Boolean GameMode = Process.getMain().conf.getBoolean("Anti-Change-GameMode");
			if(GameMode == false)
				return;
			for(MetadataValue meta : player.getMetadata(KouekiSystem.KouekiMeta))
			{
				KouekiSetup g = (KouekiSetup) meta.value();
				player.sendMessage(Koueki.PlayerPrefix +
						ChatColor.RED + "ゲームモードが変更されたため交易を強制終了しました");
				KouekiSystem.StopKoueki(player, g);
			}
		}
	}

	@EventHandler
	public void onLogout(PlayerQuitEvent e)
	{
		Player player = (Player) e.getPlayer();
		if(player.hasMetadata(KouekiSystem.KouekiMeta))
		{
			for(MetadataValue meta : player.getMetadata(KouekiSystem.KouekiMeta))
			{
				KouekiSetup g = (KouekiSetup) meta.value();
				KouekiSystem.StopKoueki(player, g);
			}
		}
	}

	@EventHandler
	public void afterDeath(PlayerRespawnEvent e)
	{
		Player player = (Player) e.getPlayer();
		if(player.hasMetadata(KouekiSystem.KouekiMeta))
		{
			for(MetadataValue meta : player.getMetadata(KouekiSystem.KouekiMeta))
			{
				KouekiSetup g = (KouekiSetup) meta.value();
				KouekiSystem.StopKoueki(player, g);
			}
		}
	}

	@EventHandler
	public void PlayerDeath(PlayerDeathEvent e)
	{
		Player player = (Player) e.getEntity();
		if(player.hasMetadata(KouekiSystem.KouekiMeta))
		{
			e.getDrops().clear();
		}
	}
}
