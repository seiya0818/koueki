package jp.seiya0818;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
			//startやquit、listの時にメッセージを表示する
			if(args[0].equalsIgnoreCase("start") || args[0].equalsIgnoreCase("quit"))
			{
				sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "後に交易品の名前を付けてください。");
				return true;
			}
			else if(args[0].equalsIgnoreCase("list"))
			{
				sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "ページ数を入力してください。");
			}
			//交易品リロードコマンド
			else if(args[0].equalsIgnoreCase("reload"))
			{
				Process.loadglist();
				sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "交易品をリロードしました。");
				return true;
			}
			//ポイント表示コマンド
			if(args[0].equalsIgnoreCase("point"))
			{
				if(!sender.hasPermission("koueki.displaypoint"))
				{
					KouekiSystem.HasnotPermissionMsg(sender);
				}
				if(!(sender instanceof Player))
				{
					KouekiSystem.ConsoleMessage(sender);
				}
				else
				{
					Player player = (Player) sender;
					KouekiSystem.DisplayPoint(player);
				}
			}
			//それ以外ならhelp表示
			else
			{
				KouekiSystem.PlayerCommands(sender);
				if(sender.hasPermission("koueki.adminhelp"));
				{
					KouekiSystem.AdminCommands(sender);
				}
				return true;
			}
			break;
			
		case 2:
			//交易情報表示コマンド
			if(args[0].equalsIgnoreCase("info"))
			{
				KouekiSetup g = Process.getgoods(args[1]);
				if(g != null)
				{
					//パー(ry
					if(!sender.hasPermission("koueki.info"))
					{
						KouekiSystem.HasnotPermissionMsg(sender);
					}
					else if(sender.hasPermission("koueki.info"))
					{
						KouekiSystem.KouekiInfo(sender, g);
					}
				}
				else
				{
					sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "指定された交易品が見つかりませでした。");
				}
				return true;
			}
			
			//交易品作成コマンド
			if(args[0].equalsIgnoreCase("create"))
			{
				if(Process.getgoods(args[1]) == null)
				{
					if(!sender.hasPermission("koueki.create"))
					{
						KouekiSystem.HasnotPermissionMsg(sender);
					}
					if(sender.hasPermission("koueki.create"))
					{
						Process.putgoods(args[1], new KouekiSetup(args[1]));
						sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "交易品"
						+ ChatColor.GOLD + args[1] + ChatColor.GREEN + "を作成しました。");
					}
				}
				else
				{
					sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "交易品"
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
						KouekiSystem.HasnotPermissionMsg(sender);
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
			if (args[0].equalsIgnoreCase("setkit"))
			{
				KouekiSetup g = Process.getgoods(args[1]);
				if(!sender.hasPermission("koueki.setitem"))
				{
					KouekiSystem.HasnotPermissionMsg(sender);
				}
				else if(!(sender instanceof Player))
				{
					KouekiSystem.ConsoleMessage(sender);
				}
				else if(g == null)
				{
					sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + args[1] +"は存在しません。");
				}
				else
				{
					Player player = (Player) sender;
					g.saveKouekiInventory(player);
					player.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "交易インベントリを設定しました。");
				}
				return true;
			}
			//他のプレイヤーのポイント表示
			if(args[0].equalsIgnoreCase("point"))
			{
				if(!sender.hasPermission("koueki.displaypoint.others"))
				{
					KouekiSystem.HasnotPermissionMsg(sender);
				}
				if(!(sender instanceof Player))
				{
					KouekiSystem.ConsoleMessage(sender);
				}
				else
				{
					Player target = getPlayer(args[2]);
					KouekiSystem.DisplayPoint(target);
				}
			}
			//交易品一覧表示コマンド
			if (args[0].equalsIgnoreCase("list"))
			{
				if(!sender.hasPermission("koueki.list"))
				{
					KouekiSystem.HasnotPermissionMsg(sender);
				}
				else
				{
					try
					{
						int page = Integer.parseInt(args[1]);
						int maxpage = Process.getgoodslist().size() / 9;
						if (Process.getgoodslist().size() % 9 != 0)
						{
							maxpage++;
						}
						if ((page < 1) || (page > maxpage))
						{
							sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED +
									"指定されたページ数が見つかりませんでした。");
							return true;
						}
						if (Process.getgoodslist().size() > 0)
						{
							sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GOLD + "交易品の一覧を表示します。");
							for (int i = 9 * page - 9; i < page * 9; i++)
							{
								if (Process.getgoodslist().size() > i)
								{
									sender.sendMessage(ChatColor.RED +"[ID] " + ChatColor.RESET +
											((KouekiSetup)Process.getgoodslist().get(i)).getSID()
											+ ChatColor.YELLOW +" [交易品名] " + ChatColor.RESET +
											((KouekiSetup)Process.getgoodslist().get(i)).getName());
								}
							}
						}
						else
						{
							sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "交易品が一つも作成されていません。");
						}
					}
					catch (Exception e)
					{
						sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "エラーが発生しました。");
					}
				}
				return true;
			}
			//交易開始コマンド
			if(args[0].equalsIgnoreCase("start"))
			{
				if(!(sender instanceof Player))
				{
					KouekiSystem.ConsoleMessage(sender);
				}
				if(sender instanceof Player)
				{
					Player player = (Player) sender;
					KouekiSetup g = Process.getgoods(args[1]);
					if(g != null)
					{
						//パー(ry
						if(!sender.hasPermission("koueki.start"))
						{
							KouekiSystem.HasnotPermissionMsg(sender);
						}
						else if(sender.hasPermission("koueki.start"))
						{
							KouekiSystem.StartKoueki(player, g);
						}
					}
					else
					{
						sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "指定された交易品が見つかりませでした。");
					}
				}
				return true;
			}
			//交易終了コマンド
			else if(args[0].equalsIgnoreCase("quit"))
			{
				if(!(sender instanceof Player))
				{
					KouekiSystem.ConsoleMessage(sender);
				}
				if(sender instanceof Player)
				{
					Player player = (Player) sender;
					KouekiSetup g = Process.getgoods(args[1]);
					if(g != null)
					{
						//パー(ry
						if(!sender.hasPermission("koueki.quit"))
						{
							KouekiSystem.HasnotPermissionMsg(sender);
						}
						else if(sender.hasPermission("koueki.quit"))
						{
							KouekiSystem.QuitKoueki(player, g);
						}
					}
				}
				return true;
			}
			//それ以外ならhelp表示
			else
			{
				KouekiSystem.PlayerCommands(sender);
				if(sender.hasPermission("koueki.adminhelp"));
				{
					KouekiSystem.AdminCommands(sender);
				}
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
					KouekiSystem.HasnotPermissionMsg(sender);
				}
				else if(sender.hasPermission("koueki.start.others"))
				{
					KouekiSystem.StartKoueki(player, g);
					sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN +
							args[2] +"を交易モードに設定しました。");
				}
			}
			//他のプレイヤーの交易モード開始
			else if(args[0].equalsIgnoreCase("quit"))
			{
				KouekiSetup g = Process.getgoods(args[1]);
				Player player = getPlayer(args[2]);
				//パ(ry
				if(!sender.hasPermission("koueki.quit.others"))
				{
					KouekiSystem.HasnotPermissionMsg(sender);
				}
				else if(sender.hasPermission("koueki.quit.others"))
				{
					KouekiSystem.QuitKoueki(player, g);
					sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN +
							args[2] +"を交易モードから終了させました。");
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("setname"))
			{
				KouekiSetup g = Process.getgoods(args[1]);
				if(!sender.hasPermission("koueki.setname"))
				{
					KouekiSystem.HasnotPermissionMsg(sender);
				}
				else if(g == null)
				{
					sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + args[1] +"は存在しません。");
				}
				else
				{
					String sid = g.getSID();
					g.setName(sid, args[2]);
					sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "名前を変更しました。");
				}
				return true;
			}
			//それ以外ならhelp表示
			else
			{
				if(sender.hasPermission("koueki.help"))
				{
					KouekiSystem.PlayerCommands(sender);
				}
				return true;
			}
		//それ以外のパラメータ数ならhelp表示
		default:
			if(sender.hasPermission("koueki.help"))
			{
				KouekiSystem.PlayerCommands(sender);
				if(sender.hasPermission("koueki.adminhelp"));
				{
					KouekiSystem.AdminCommands(sender);
				}
				return true;
			}
		}
		return true;
	}
	
	private Player getPlayer(String playername)
	{
		return Bukkit.getPlayerExact(playername);
	}
}