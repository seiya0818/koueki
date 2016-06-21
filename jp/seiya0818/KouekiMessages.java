package jp.seiya0818;

import jp.seiya0818.KouekiSystem.KouekiPoint;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KouekiMessages
{
	/*
	 * 情報表示系メッセージ
	 */
	public static void DisplayPointMsg(CommandSender sender, Player player)
	{
		int points = KouekiPoint.getPoint(player);
		KouekiPoint.getPoint(player);
		sender.sendMessage(Koueki.PlayerPrefix + ChatColor.AQUA + "あなたのポイント: " + ChatColor.LIGHT_PURPLE
		+ points + Process.getMain().conf.getString("Point-Unit") + ChatColor.AQUA + " です。");
	}

	public static void KouekiInfoMsg(CommandSender sender, KouekiSetup g)
	{
		sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "交易品 " +
		ChatColor.GOLD + g.getSID() + ChatColor.GREEN + " の情報を表示します");
		sender.sendMessage(ChatColor.AQUA + "アイテム名： " + ChatColor.DARK_PURPLE + g.getName());
		sender.sendMessage(ChatColor.AQUA + "アイテム販売価格： " + ChatColor.DARK_PURPLE + g.getprice());
		sender.sendMessage(ChatColor.AQUA + "ポイント： " + ChatColor.DARK_PURPLE + g.getpoint() +
				Process.getMain().conf.getString("Point-Unit"));
	}

	public static void PlayerCommandsHelp(CommandSender sender)
	{
		sender.sendMessage(ChatColor.DARK_GREEN + "-----------" + ChatColor.GOLD + "交易 ヘルプ" +
	                       ChatColor.DARK_GREEN + "-----------");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.AQUA +  "  /koueki start <交易品名>");
		sender.sendMessage(ChatColor.GOLD +  "   交易モードに設定します。");
		sender.sendMessage(ChatColor.AQUA +  "  /koueki stop <交易品名>");
		sender.sendMessage(ChatColor.GOLD +  "   交易モードを強制終了します。");
		sender.sendMessage(ChatColor.AQUA +  "  /koueki status");
		sender.sendMessage(ChatColor.GOLD +  "   自分の交易情報表示を表示します。");
		sender.sendMessage(ChatColor.AQUA +  "  /koueki point");
		sender.sendMessage(ChatColor.GOLD +  "   交易ポイントを表示します。");
		sender.sendMessage(ChatColor.AQUA +  "  /koueki buy <ws|kc>");
		sender.sendMessage(ChatColor.GOLD +  "   ウィングシューズや交易服を購入します。");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.DARK_GREEN + "----------------------------------");
	}

	public static void AdminCommandsHelp1(CommandSender sender)
	{
		sender.sendMessage(ChatColor.DARK_GREEN + "-----------" + ChatColor.GOLD + "交易 運営用ヘルプ" +
	                       ChatColor.DARK_GREEN + "-----------");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.AQUA +  "   /koueki create <交易品名>");
		sender.sendMessage(ChatColor.GOLD +  "    交易品を作成します。");
		sender.sendMessage(ChatColor.AQUA +  "   /koueki delete <交易品名>");
		sender.sendMessage(ChatColor.GOLD +  "    交易品を削除します。");
		sender.sendMessage(ChatColor.AQUA +  "   /koueki info <交易品名>");
		sender.sendMessage(ChatColor.GOLD +  "    交易品の情報を表示します。");
		sender.sendMessage(ChatColor.AQUA +  "   /koueki list <交易品名> <ページ数>");
		sender.sendMessage(ChatColor.GOLD +  "    交易品一覧を表示します。");
		sender.sendMessage(ChatColor.AQUA +  "   /koueki point <プレイヤー名>");
		sender.sendMessage(ChatColor.GOLD +  "    プレイヤーのポイントを表示します。");
		sender.sendMessage(ChatColor.GREEN + "/koueki adminhelp 2 で2ページ目を確認してください。");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.DARK_GREEN + "----------------------------------------");
	}

	public static void AdminCommandsHelp2(CommandSender sender)
	{
		sender.sendMessage(ChatColor.DARK_GREEN + "-----------" + ChatColor.GOLD + "交易 運営用ヘルプ" +
				ChatColor.DARK_GREEN + "-----------");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.AQUA +  "   /koueki quit <交易品名>");
		sender.sendMessage(ChatColor.GOLD +  "    交易モードを終了します。");
		sender.sendMessage(ChatColor.AQUA +  "   /koueki quit <交易品名> <プレイヤー名>");
		sender.sendMessage(ChatColor.GOLD +  "    プレイヤーの交易モードを終了します。");
		sender.sendMessage(ChatColor.AQUA +  "   /koueki start <交易品名> <プレイヤー名>");
		sender.sendMessage(ChatColor.GOLD +  "    交易モードに設定します。");
		sender.sendMessage(ChatColor.AQUA +  "   /koueki setkit <交易品名>");
		sender.sendMessage(ChatColor.GOLD +  "    交易モードのインベントリを現在のインベントリに設定します。");
		sender.sendMessage(ChatColor.AQUA +  "   /koueki setpoint <ポイント>");
		sender.sendMessage(ChatColor.GOLD +  "    自分の交易ポイントを設定します。");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.DARK_GREEN + "----------------------------------------");
	}
	/*
	 * 実行成功メッセージ
	 */


	/*
	 * エラーメッセージ
	 */
	public static void UnknownCommandMsg(CommandSender sender)
	{
		sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "コマンドが見つかりませんでした。"
	    + "/koueki helpでコマンド一覧を確認してください。");
	}

	public static void isnotinKouekiMsg(Player player)
	{
		player.sendMessage("");
		player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "交易中ではありません。");
		player.sendMessage("");
	}

	public static void NoGoodsMsg(CommandSender sender, String args)
	{
		sender.sendMessage("");
		sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GOLD + args + ChatColor.RED + " は存在しません。");
		sender.sendMessage("");
	}

	public static void NoPlayersMsg(CommandSender sender, String name)
	{
		sender.sendMessage("");
		sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GOLD + name + ChatColor.RED + " は見つかりませんでした。");
		sender.sendMessage("");
	}

	public static void HasnotPermissionMsg(CommandSender sender)
	{
		sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "権限がありません。");
	}

	public static void ConsoleMsg(CommandSender sender)
	{
		sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "このコマンドはプレイヤーのみ使用可能です");
	}
}
