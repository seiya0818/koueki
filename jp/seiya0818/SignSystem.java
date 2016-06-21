package jp.seiya0818;

import jp.seiya0818.KouekiPost.PostProcess;
import jp.seiya0818.KouekiPost.PostSetup;
import jp.seiya0818.KouekiPriceFluctuation.PriceSystem;
import jp.seiya0818.KouekiSystem.KouekiSystem;
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
import org.bukkit.metadata.MetadataValue;

public class SignSystem implements Listener
{

	public SignSystem()
	{
		Process.getMain().getServer().getPluginManager().registerEvents(this, Process.getMain());
	}

	String signt = ChatColor.GOLD + "[" + ChatColor.RED + "交易" + ChatColor.GOLD + "]";
	String start = ChatColor.GREEN + "" + ChatColor.BOLD + "START";
	String quit = ChatColor.RED + "" + ChatColor.BOLD + "Quit";
	String menu = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Menu";
	String market = ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "Market";

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
		if(!lines[0].equalsIgnoreCase(signt))
			return;
		{
			if(lines[1].equalsIgnoreCase(start))
			{
				KouekiSetup g = Process.getgoods(lines[2]);
				KouekiSystem.StartKoueki(player, g);
			}
			else if(lines[1].equalsIgnoreCase(quit))
			{
				if(!player.hasMetadata(KouekiSystem.KouekiMeta))
				{
					player.sendMessage("");
					player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "交易中ではありません");
					player.sendMessage("");
				}
				else
				{
					for(MetadataValue meta : player.getMetadata(KouekiSystem.KouekiMeta))
					{
						KouekiSetup gds = (KouekiSetup) meta.value();
						PostSetup p = PostProcess.getpost(lines[2]);
						KouekiSystem.QuitKoueki(player, gds, p);
					}
				}
			}
			else if(lines[1].equalsIgnoreCase(menu))
			{
				PostSetup p = PostProcess.getpost(lines[2]);
				KouekiGUI.OpenKGUI(player, p);
			}
			else if(lines[1].equalsIgnoreCase(market))
			{
				if(!player.hasMetadata(KouekiSystem.KouekiMeta))
				{
					PriceSystem.showAllPrice(player);
				}
				if(player.hasMetadata(KouekiSystem.KouekiMeta))
				{
					for(MetadataValue meta : player.getMetadata(KouekiSystem.KouekiMeta))
					{
						KouekiSetup g = (KouekiSetup) meta.value();
						PriceSystem.showPrice(player, g);
					}
				}
			}
		}
	}

	@EventHandler
	public void onSignCreate(SignChangeEvent e)
	{
		if (!"[Koueki]".equalsIgnoreCase(e.getLine(0)))
		{
			return;
		}
		else
		{
			Player player = e.getPlayer();
			if(!player.hasPermission("koueki.edit"))
			{
				KouekiMessages.HasnotPermissionMsg(player);
			}
			else
			{
				if("start".equals(e.getLine(1)))
				{
					KouekiSetup g = Process.getgoods(e.getLine(2));
					if(g == null)
					{
						KouekiMessages.NoGoodsMsg(player, e.getLine(2));
						e.setCancelled(true);
						e.getBlock().breakNaturally();
					}
					else
					{
						if (player.hasPermission("koueki.sign"))
						{
							e.setLine(0, signt);
							e.setLine(1, start);
							e.setLine(2, g.getSID());
							e.setLine(3, ChatColor.DARK_BLUE + "値段: " + g.getprice());
							player.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "開始看板を作成しました。右クリックで開始します。");
						}
					}
				}
				else if("menu".equals(e.getLine(1)))
				{
					PostSetup p = PostProcess.getpost(e.getLine(2));
					if(p == null)
					{
						KouekiMessages.NoGoodsMsg(player, e.getLine(2));
						e.setCancelled(true);
						e.getBlock().breakNaturally();
					}
					else
					{
						if (player.hasPermission("koueki.sign"))
						{
							e.setLine(0, signt);
							e.setLine(1, menu);
							e.setLine(2, p.getSID());
							e.setLine(3, ChatColor.BLUE + "");
							player.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "メニュー看板を作成しました。右クリックでGUIを開きます。");
						}
					}
				}
				else if("quit".equals(e.getLine(1)))
				{
					PostSetup p = PostProcess.getpost(e.getLine(2));
					if(p == null)
					{
						KouekiMessages.NoGoodsMsg(player, e.getLine(2));
						e.setCancelled(true);
						e.getBlock().breakNaturally();
					}
					else
					{
						e.setLine(0, signt);
						e.setLine(1, quit);
						e.setLine(3, ChatColor.DARK_BLUE + "右クリックで交易終了");
						player.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "終了看板を作成しました。右クリックで終了します。");
					}
				}
				else if("market".equals(e.getLine(1)))
				{
					e.setLine(0, signt);
					e.setLine(1, market);
					e.setLine(3, ChatColor.DARK_BLUE + "右クリックで取引情報表示");
					player.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "価格看板を作成しました。右クリックで表示します。");
				}
				else
				{
					player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "行動を指定してください。");
					e.setCancelled(true);
					e.getBlock().breakNaturally();
					return;
				}
			}
		}
	}
}