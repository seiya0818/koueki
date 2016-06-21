package jp.seiya0818;

import jp.seiya0818.KouekiPost.PostProcess;
import jp.seiya0818.KouekiPost.PostSetup;
import jp.seiya0818.KouekiPriceFluctuation.PriceSystem;
import jp.seiya0818.KouekiPriceFluctuation.RandomPrice;
import jp.seiya0818.KouekiSystem.KouekiHorse;
import jp.seiya0818.KouekiSystem.KouekiPoint;
import jp.seiya0818.KouekiSystem.KouekiSystem;
import jp.seiya0818.KouekiSystem.KouekiTools;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

public class KouekiCommandExecutor implements CommandExecutor
{
	public final Koueki plugin;

	public KouekiCommandExecutor(Koueki plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		switch(args.length)
		{
		case 1:
			//startやquitの時にメッセージを表示する
			if(args[0].equalsIgnoreCase("start"))
			{
				sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "後に交易品の名前を付けてください。");
				return true;
			}

			else if(args[0].equalsIgnoreCase("status"))
			{
				if(!(sender instanceof Player))
				{
					KouekiMessages.ConsoleMsg(sender);
				}
				else if(!sender.hasPermission("koueki.status"))
				{
					KouekiMessages.HasnotPermissionMsg(sender);
				}
				else
				{
					Player player = (Player) sender;
					KouekiTools.status(player);
				}
				return true;
			}

			//相場表示コマンド
			else if(args[0].equalsIgnoreCase("marketall"))
			{
				if(!sender.hasPermission("koueki,marketall"))
				{
					KouekiMessages.HasnotPermissionMsg(sender);
				}
				else
				{
					PriceSystem.showAllPrice(sender);
				}
				return true;
			}

			//相場(価格)の相場表示コマンド
			else if(args[0].equalsIgnoreCase("marketpall"))
			{
				if(!sender.hasPermission("koueki,marketpall"))
				{
					KouekiMessages.HasnotPermissionMsg(sender);
				}
				else
				{
					PriceSystem.showAllPriceP(sender);
				}
				return true;
			}

			//交易強制終了コマンド
			else if(args[0].equalsIgnoreCase("stop"))
			{
				if(!(sender instanceof Player))
				{
					KouekiMessages.ConsoleMsg(sender);
				}
				else if(!sender.hasPermission("koueki.stop"))
				{
					KouekiMessages.HasnotPermissionMsg(sender);
				}
				else
				{
					Player player = (Player) sender;
					if(player.hasMetadata(KouekiSystem.KouekiMeta))
					{
						KouekiMessages.isnotinKouekiMsg(player);
					}
					for(MetadataValue meta : player.getMetadata(KouekiSystem.KouekiMeta))
					{
						KouekiSetup g = (KouekiSetup) meta.value();
						if(g == null)
						{
							//近日追加
						}
						KouekiSystem.StopKoueki(player, g);
					}
				}
				return true;
			}

			//交易品一覧表示コマンド
			else if (args[0].equalsIgnoreCase("list"))
			{
				if(!sender.hasPermission("koueki.list"))
				{
					KouekiMessages.HasnotPermissionMsg(sender);
				}
				else
				{
					Process.GoodsList(sender, String.valueOf(1));
				}
				return true;
			}

			//交易所一覧表示コマンド
			else if (args[0].equalsIgnoreCase("postlist"))
			{
				if(!sender.hasPermission("koueki.list"))
				{
					KouekiMessages.HasnotPermissionMsg(sender);
				}
				else
				{
					PostProcess.PostList(sender, String.valueOf(1));
				}
				return true;
			}

			//交易品リロードコマンド
			else if(args[0].equalsIgnoreCase("reload"))
			{
				Process.loadglist();
				sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "交易品をリロードしました。");
				RandomPrice.putnumber();
				return true;
			}

			//ポイント表示コマンド
			if(args[0].equalsIgnoreCase("point"))
			{
				if(!sender.hasPermission("koueki.displaypoint"))
				{
					KouekiMessages.HasnotPermissionMsg(sender);
				}
				if(!(sender instanceof Player))
				{
					KouekiMessages.ConsoleMsg(sender);
				}
				else
				{
					Player player = (Player) sender;
					KouekiMessages.DisplayPointMsg(sender, player);
				}
			}

			//helpコマンド
			else if(args[0].equalsIgnoreCase("help"))
			{
				KouekiMessages.PlayerCommandsHelp(sender);
				if(sender instanceof Player)
				{
					Player player = (Player) sender;
					KouekiSound.playHelpSound(player);
				}
				return true;
			}

			//adminhelpコマンド
			else if(args[0].equalsIgnoreCase("adminhelp"))
			{
				if(!sender.hasPermission("koueki.adminhelp"))
				{
					KouekiMessages.HasnotPermissionMsg(sender);
				}
				else
				{
					KouekiMessages.AdminCommandsHelp1(sender);
					if(sender instanceof Player)
					{
						Player player = (Player) sender;
						KouekiSound.playHelpSound(player);
					}
				}
				return true;
			}

			//それ以外ならhelp表示
			else
			{
				KouekiMessages.UnknownCommandMsg(sender);
				return true;
			}
			break;

		case 2:
			//GUI表示コマンド
			if(args[0].equalsIgnoreCase("menu"))
			{
				PostSetup p = PostProcess.getpost(args[1]);
				if(!(sender instanceof Player))
				{
					KouekiMessages.ConsoleMsg(sender);
				}
				else if(!sender.hasPermission("koueki.GUI"))
				{
					KouekiMessages.HasnotPermissionMsg(sender);
				}
				else if(p == null)
				{
					KouekiMessages.NoGoodsMsg(sender, args[1]);
				}
				else
				{
					Player player = (Player) sender;
					KouekiGUI.OpenKGUI(player, p);
				}
				return true;
			}
			//交易終了コマンド
			else if(args[0].equalsIgnoreCase("quit"))
			{
				PostSetup p = PostProcess.getpost(args[1]);
				if(!(sender instanceof Player))
				{
					KouekiMessages.ConsoleMsg(sender);
				}
				else if(!sender.hasPermission("koueki.quit"))
				{
					KouekiMessages.HasnotPermissionMsg(sender);
				}
				else if(p == null)
				{
					KouekiMessages.NoGoodsMsg(sender, args[1]);
				}
				else
				{
					Player player = (Player) sender;
					if(!player.hasMetadata(KouekiSystem.KouekiMeta))
					{
						KouekiMessages.isnotinKouekiMsg(player);
						break;
					}
					for(MetadataValue meta : player.getMetadata(KouekiSystem.KouekiMeta))
					{
						KouekiSetup g = (KouekiSetup) meta.value();
						if(g == null)
						{
							//近日追加
						}
						KouekiSystem.QuitKoueki(player, g, p);
					}
				}
				return true;
			}

			//交易情報表示コマンド
			else if(args[0].equalsIgnoreCase("info"))
			{
				KouekiSetup g = Process.getgoods(args[1]);
				if(g != null)
				{
					//パー(ry
					if(!sender.hasPermission("koueki.info"))
					{
						KouekiMessages.HasnotPermissionMsg(sender);
					}
					else if(sender.hasPermission("koueki.info"))
					{
						KouekiMessages.KouekiInfoMsg(sender, g);
					}
				}
				else
				{
					KouekiMessages.NoGoodsMsg(sender, args[1]);
				}
				return true;
			}

			//交易品作成コマンド
			else if(args[0].equalsIgnoreCase("create"))
			{
				if(Process.getgoods(args[1]) == null)
				{
					if(!sender.hasPermission("koueki.create"))
					{
						KouekiMessages.HasnotPermissionMsg(sender);
					}
					else if(!(sender instanceof Player))
					{
						KouekiMessages.ConsoleMsg(sender);
					}
					if(sender.hasPermission("koueki.create"))
					{
						Player player = (Player) sender;
						if(KouekiSetup.checkItemInHand(player) == false)
						{
							return true;
						}
						Process.putgoods(args[1], new KouekiSetup(args[1]));
						KouekiSetup g = Process.getgoods(args[1]);
						KouekiSetup.saveItem(player, g);
						sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "交易品"
						+ ChatColor.GOLD + args[1] + ChatColor.GREEN + "を作成しました。");
						RandomPrice.putnumber();
					}
				}
				else
				{
					sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "交易品"
				    + ChatColor.GOLD + args[1] + ChatColor.GREEN + "は既に存在します。");
				}
				return true;
			}

			//交易所作成コマンド
			else if(args[0].equalsIgnoreCase("postcreate"))
			{
				if(PostProcess.getpost(args[1]) == null)
				{
					if(!sender.hasPermission("koueki.create"))
					{
						KouekiMessages.HasnotPermissionMsg(sender);
					}
					if(sender.hasPermission("koueki.create"))
					{
						PostProcess.putposts(args[1], new PostSetup(args[1]));
						PostSetup p = PostProcess.getpost(args[1]);
						PostSetup.refreshPostscreate(sender, p);
						sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "交易所"
						+ ChatColor.GOLD + args[1] + ChatColor.GREEN + "を作成しました。");
					}
				}
				else
				{
					sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "交易所"
				    + ChatColor.GOLD + args[1] + ChatColor.GREEN + "は既に存在します。");
				}
				return true;
			}

			//交易品削除コマンド
			else if(args[0].equalsIgnoreCase("delete"))
			{
				KouekiSetup g = Process.getgoods(args[1]);
				if(g != null)
				{
					if(!sender.hasPermission("koueki.delete"))
					{
						KouekiMessages.HasnotPermissionMsg(sender);
					}
					if(sender.hasPermission("koueki.delete"))
					{
						g.delete();
						sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "交易品" +
						ChatColor.GOLD + args[1] + ChatColor.GREEN + "を削除しました。");
					}
				}
				else
				{
					sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "交易品" +
				    ChatColor.GOLD + args[1] + ChatColor.GREEN + "は見つかりませんでした。");
				}
				return true;
			}

			//交易所削除コマンド
			else if(args[0].equalsIgnoreCase("postdelete"))
			{
				PostSetup p = PostProcess.getpost(args[1]);
				if(p != null)
				{
					if(!sender.hasPermission("koueki.delete"))
					{
						KouekiMessages.HasnotPermissionMsg(sender);
					}
					if(sender.hasPermission("koueki.delete"))
					{
						p.delete();
						PostSetup.refreshPostsdelete(sender, p);
						sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "交易所" +
						ChatColor.GOLD + args[1] + ChatColor.GREEN + "を削除しました。");
					}
				}
				else
				{
					sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "交易所" +
				    ChatColor.GOLD + args[1] + ChatColor.GREEN + "は見つかりませんでした。");
				}
				return true;
			}

			//キット設定コマンド
			else if (args[0].equalsIgnoreCase("setkit"))
			{
				KouekiSetup g = Process.getgoods(args[1]);
				if(!sender.hasPermission("koueki.setitem"))
				{
					KouekiMessages.HasnotPermissionMsg(sender);
				}
				else if(!(sender instanceof Player))
				{
					KouekiMessages.ConsoleMsg(sender);
				}
				else if(g == null)
				{
					KouekiMessages.NoGoodsMsg(sender, args[1]);
				}
				else
				{
					Player player = (Player) sender;
					g.saveKouekiInventory(player);
					player.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "交易インベントリを設定しました。");
				}
				return true;
			}

			//ポイント設定コマンド
			else if(args[0].equalsIgnoreCase("setpoint"))
			{
				if(!sender.hasPermission("koueki.setpoint"))
				{
					KouekiMessages.HasnotPermissionMsg(sender);
				}
				if(!(sender instanceof Player))
				{
					KouekiMessages.ConsoleMsg(sender);
				}
				else
				{
					Player player = (Player) sender;
					int point = Integer.parseInt(args[1]);
					String unit = Process.getMain().conf.getString("Point-Unit");
					KouekiPoint.setpoint(player, point);
					sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "あなたのポイントを" +
					ChatColor.GOLD + point + unit + ChatColor.GREEN + "に設定しました。");
;				}
				return true;
			}

			//ウィングシューズ購入
			else if(args[0].equalsIgnoreCase("buy"))
			{
				if(!(sender instanceof Player))
				{
					KouekiMessages.ConsoleMsg(sender);
				}
				if(!sender.hasPermission("koueki.buy"))
				{
					KouekiMessages.HasnotPermissionMsg(sender);
				}
				else
				{
					Player player = (Player) sender;
					if(args[1].equalsIgnoreCase("wingshoose") || args[1].equalsIgnoreCase("ws"))
					{
						KouekiTools.buyWingShoose(player);
					}
					else if(args[1].equalsIgnoreCase("kouekiclother") || args[1].equalsIgnoreCase("kc"))
					{
						KouekiTools.buyKouekiClothes(player);
					}
					else if(args[1].equalsIgnoreCase("kouekihorse") || args[1].equalsIgnoreCase("kh"))
					{
						KouekiHorse.buyKouekiHorse(player);
					}
					else
					{
						player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "wsかkcかkhを入力してください。");
						player.sendMessage(Koueki.PlayerPrefix + ChatColor.GOLD + "ws: ウィングシューズ");
						player.sendMessage(Koueki.PlayerPrefix + ChatColor.GOLD + "kc: 交易服");
						player.sendMessage(Koueki.PlayerPrefix + ChatColor.GOLD + "kh: 馬");
					}
				}
				return true;
			}

			//他のプレイヤーのポイント表示
			else if(args[0].equalsIgnoreCase("point"))
			{
				if(!sender.hasPermission("koueki.displaypoint.others"))
				{
					KouekiMessages.HasnotPermissionMsg(sender);
				}
				else
				{
					Player target = getPlayer(args[2]);
					if(target == null)
					{
						sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GOLD + target + ChatColor.RED
								+ "が見つかりませんでした。");
					}
					else
					{
						KouekiMessages.DisplayPointMsg(sender, target);
					}
				}
			}

			//相場表示コマンド
			else if(args[0].equalsIgnoreCase("market"))
			{
				if(!sender.hasPermission("koueki.market"))
				{
					KouekiMessages.HasnotPermissionMsg(sender);
				}
				else
				{
					KouekiSetup g = Process.getgoods(args[1]);
					if(g == null)
					{
						KouekiMessages.NoGoodsMsg(sender, args[1]);
					}
					else
					{
						PriceSystem.showPrice(sender, g);
					}
				}
				return true;
			}

			//相場(価格)表示コマンド
			else if(args[0].equalsIgnoreCase("marketp"))
			{
				if(!sender.hasPermission("koueki.marketp"))
				{
					KouekiMessages.HasnotPermissionMsg(sender);
				}
				else
				{
					KouekiSetup g = Process.getgoods(args[1]);
					if(g == null)
					{
						KouekiMessages.NoGoodsMsg(sender, args[1]);
					}
					else
					{
						PriceSystem.showPriceP(sender, g);
					}
				}
				return true;
			}

			//交易品一覧表示コマンド
			else if (args[0].equalsIgnoreCase("list"))
			{
				if(!sender.hasPermission("koueki.list"))
				{
					KouekiMessages.HasnotPermissionMsg(sender);
				}
				else
				{
					Process.GoodsList(sender, args[1]);
				}
				return true;
			}

			//交易所一覧表示コマンド
			else if (args[0].equalsIgnoreCase("postlist"))
			{
				if(!sender.hasPermission("koueki.list"))
				{
					KouekiMessages.HasnotPermissionMsg(sender);
				}
				else
				{
					PostProcess.PostList(sender, args[1]);
				}
				return true;
			}

			//交易開始コマンド
			else if(args[0].equalsIgnoreCase("start"))
			{
				if(!(sender instanceof Player))
				{
					KouekiMessages.ConsoleMsg(sender);
				}
				if(sender instanceof Player)
				{
					Player player = (Player) sender;
					KouekiSetup g = Process.getgoods(args[1]);
					if(g == null)
					{
						KouekiMessages.NoGoodsMsg(sender, args[1]);
					}
					else
					{
						//パー(ry
						if(!sender.hasPermission("koueki.start"))
						{
							KouekiMessages.HasnotPermissionMsg(sender);
						}
						else if(sender.hasPermission("koueki.start"))
						{
							KouekiSystem.StartKoueki(player, g);
						}
					}
				}
				return true;
			}

			//adminhelpコマンド
			else if(args[0].equalsIgnoreCase("adminhelp") && args[1].equalsIgnoreCase("1"))
			{
				KouekiMessages.AdminCommandsHelp1(sender);
				if(sender instanceof Player)
				{
					Player player = (Player) sender;
					KouekiSound.playHelpSound(player);
				}
				return true;
			}

			//adminhelp(2)コマンド
			else if(args[0].equalsIgnoreCase("adminhelp") && args[1].equalsIgnoreCase("2"))
			{
				KouekiMessages.AdminCommandsHelp2(sender);
				if(sender instanceof Player)
				{
					Player player = (Player) sender;
					KouekiSound.playHelpSound(player);
				}
				return true;
			}

			//それ以外ならhelp表示
			else
			{
				KouekiMessages.UnknownCommandMsg(sender);
				return true;
			}

		case 3:
			//他のプレイヤーの交易モード開始
			if(args[0].equalsIgnoreCase("start"))
			{
				KouekiSetup g = Process.getgoods(args[1]);
				Player player = getPlayer(args[2]);
				//パ(ry
				if(!sender.hasPermission("koueki.start.others"))
				{
					KouekiMessages.HasnotPermissionMsg(sender);
				}
				else if(sender.hasPermission("koueki.start.others"))
				{
					KouekiSystem.StartKoueki(player, g);
					sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN +
							args[2] +"を交易モードに設定しました。");
				}
			}

			//他のプレイヤーの交易モード終了
			else if(args[0].equalsIgnoreCase("quit"))
			{
				Player target = getPlayer(args[1]);
				PostSetup p = PostProcess.getpost(args[2]);
				if(!sender.hasPermission("koueki.quit.others"))
				{
					KouekiMessages.HasnotPermissionMsg(sender);
				}
				else if(!sender.hasPermission("koueki.quit.others"))
				{
					KouekiMessages.HasnotPermissionMsg(sender);
				}
				else if(target == null)
				{
					KouekiMessages.NoPlayersMsg(sender, args[1]);
				}
				else if(p == null)
				{
					KouekiMessages.NoGoodsMsg(sender, args[2]);
				}
				else
				{
					for(MetadataValue meta : target.getMetadata(KouekiSystem.KouekiMeta))
					{
						KouekiSetup g = (KouekiSetup) meta.value();
						if(g == null)
						{
							//近日追加
						}
						if(target == null)
						{
							sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GOLD +
									args[1] + "が見つかりませんでした");
						}
						else
						{
							KouekiSystem.QuitKoueki(target, g, p);
							sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN +
									args[1] +"を交易モードから終了させました。");
						}
					}
				}
				return true;
			}

			//交易品名前変更コマンド
			else if(args[0].equalsIgnoreCase("setname"))
			{
				KouekiSetup g = Process.getgoods(args[1]);
				if(!sender.hasPermission("koueki.setname"))
				{
					KouekiMessages.HasnotPermissionMsg(sender);
				}
				else if(g == null)
				{
					KouekiMessages.NoGoodsMsg(sender, args[1]);
				}
				else
				{
					g.setName(args[2]);
					sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "名前を変更しました。");
				}
				return true;
			}

			//交易品交易所設定コマンド
			else if(args[0].equalsIgnoreCase("setpost"))
			{
				KouekiSetup g = Process.getgoods(args[1]);
				PostSetup p = PostProcess.getpost(args[2]);
				if(!sender.hasPermission("koueki.setname"))
				{
					KouekiMessages.HasnotPermissionMsg(sender);
				}
				else if(g == null)
				{
					KouekiMessages.NoGoodsMsg(sender, args[1]);
				}
				else if(p == null)
				{
					KouekiMessages.NoGoodsMsg(sender, args[2]);
				}
				else
				{
					g.setPost(args[2]);
					sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "交易所を設定しました。");
				}
				return true;
			}

			//それ以外ならhelp表示
			else
			{
				KouekiMessages.UnknownCommandMsg(sender);
				return true;
			}

		//それ以外のパラメータ数ならhelp表示
		default:
			KouekiMessages.UnknownCommandMsg(sender);
			return true;
		}
		return true;
	}

	private Player getPlayer(String playername)
	{
		return Bukkit.getPlayerExact(playername);
	}
}