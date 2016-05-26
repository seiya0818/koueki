package jp.seiya0818;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignSystem implements Listener
{	
	public SignSystem()
	{
		Process.getMain().getServer().getPluginManager().registerEvents(this, Process.getMain());
	}
	
	@EventHandler
	public void ClickSign(PlayerInteractEvent e)
	{
		if((e.getAction() != Action.RIGHT_CLICK_BLOCK))
			return;
		Player player = e.getPlayer();

		Block clickdblock = e.getClickedBlock();
		if ((clickdblock.getType() != Material.SIGN)
				&& (clickdblock.getType() != Material.SIGN_POST)
				&& (clickdblock.getType() != Material.WALL_SIGN))
			return;
		Sign thisSign = (Sign)clickdblock.getState();
		String[] lines = thisSign.getLines();
		if(!lines[0].equalsIgnoreCase(Koueki.PlayerPrefix))
			return;
		{
			KouekiSetup g = Process.getgoods(lines[2]);
			if(lines[1].equals("START"))
			{
				KouekiSystem.StartKoueki(player, g);
			}
			if(lines[1].equals("QUIT"))
			{
				KouekiSystem.QuitKoueki(player, g);
			}
		}
	}
	
		@EventHandler
		public void onSignCreate(SignChangeEvent event)
		{
			if (!"[Koueki]".equalsIgnoreCase(event.getLine(0)))
				return;
			Player player = event.getPlayer();
			
			if ("".equals(event.getLine(1)))
			{
				player.sendMessage(Koueki.PlayerPrefix + ChatColor.GOLD +"「start」" + ChatColor.GOLD + "「quit」" + ChatColor.RED + "を入力してください");
				event.setCancelled(true);
				return;
			}
			if (player.hasPermission("koueki.sign"))
			{
				if("start".equals(event.getLine(1)))
				{
					event.setLine(0, Koueki.PlayerPrefix);
					event.setLine(1, "START");
					event.setLine(3, ChatColor.DARK_BLUE + "右クリックで交易開始");
					player.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "開始看板を作成しました。右クリックで遊べます。");
				}
				if("quit".equals(event.getLine(1)))
				{
					event.setLine(0, Koueki.PlayerPrefix);
					event.setLine(1, "QUIT");
					event.setLine(3, ChatColor.DARK_BLUE + "右クリックで交易終了");
					player.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "終了看板を作成しました。右クリックで遊べます。");
				}
			}
		}
	}