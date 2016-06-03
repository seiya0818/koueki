package jp.seiya0818;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
//import org.bukkit.metadata.FixedMetadataValue;


//交易モードを開始
public class KouekiSystem
{
	//private final static String KouekiMeta = "交易";
	//private static Koueki instance;
	private static Inventory tempInventory;
	private static Inventory tempArmors ;
	private static int tempLevel;
	private static float tempExp;
	public final static String KouekiMeta = "KouekiMeta";


	public static void StartKoueki(Player player, KouekiSetup g)
	{
		String price = g.getprice();
		Double money = Double.parseDouble(price);
		EconomyResponse r = Process.getEconomy().withdrawPlayer(player, money);
		if(r.transactionSuccess())
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.LIGHT_PURPLE + money +"sei 支払いました");
		}
		//お金が足りなかった場合交易させない。
		else
		{
			player.sendMessage(Koueki.PlayerPrefix + "お金が足りません。");
			return;
		}
		//お金が足りた場合
		if(player.hasMetadata(KouekiMeta))
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "既に交易中です");
			return;
		}
		player.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "交易モードを開始しました");
		player.setGameMode(GameMode.ADVENTURE);
		player.setMetadata(KouekiMeta, new FixedMetadataValue(Koueki.instance, g));
		SaveRestoreInventory.savePlayerInventory(player);
		g.giveKouekiInventory(player);
	}


	public static void QuitKoueki(Player player, KouekiSetup g)
	{
		String price = g.getmaxp();
		Double money = Double.parseDouble(price);
		EconomyResponse r = Process.getEconomy().depositPlayer(player, money);
		if(r.transactionSuccess())
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.LIGHT_PURPLE + money +"sei 受け取りました。");
		}
		//お金が足りなかった場合交易させない。
		else
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "エラーが発生しました。管理人に報告してください。");
			return;
		}
		//その後の処理
		if(!player.hasMetadata(KouekiMeta))
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "交易中ではありません");
			return;
		}
		else if(player.hasMetadata(KouekiMeta))
		{
			player.getInventory().clear();
			player.removeMetadata("KouekiMeta", Koueki.instance);
			player.setGameMode(GameMode.SURVIVAL);
			SaveRestoreInventory.restorePlayerInventory(player);
			int point = g.getpoint();
			KouekiPoint.addPoint(player, point);
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "交易" +
					"を終了しました");
		}
	}
	
	public static void StopKoueki(Player player)
	{
		if(!player.hasMetadata(KouekiMeta))
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "交易品の取得に失敗しました");
			return;
		}
		else if(player.hasMetadata(KouekiMeta))
		{
			player.getInventory().clear();
			player.removeMetadata("KouekiMeta", Koueki.instance);
			
			//アイテムの消去
			player.getInventory().clear();
			player.getInventory().setArmorContents(new ItemStack[]
			{
					new ItemStack(Material.AIR),
					new ItemStack(Material.AIR),
					new ItemStack(Material.AIR),
					new ItemStack(Material.AIR),
			});
			player.setGameMode(GameMode.SURVIVAL);
			SaveRestoreInventory.restorePlayerInventory(player);
		}
	}
	
	public static void DisplayPoint(Player player)
	{
		int points = KouekiPoint.getPoint(player);
		KouekiPoint.getPoint(player);
		player.sendMessage(Koueki.PlayerPrefix + ChatColor.AQUA + "あなたのポイント: " + ChatColor.LIGHT_PURPLE
		+ points + Process.getMain().conf.getString("Point-Unit") + ChatColor.AQUA + " です。");
	}

	public static void PlayerCommands(CommandSender sender)
	{
		sender.sendMessage(ChatColor.DARK_GREEN + "-----------" + ChatColor.GOLD + "交易 ヘルプ" +
	                       ChatColor.DARK_GREEN + "-----------");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.AQUA +  "  /koueki start <交易品名>");
		sender.sendMessage(ChatColor.GOLD +  "   交易モードに設定します。");
		sender.sendMessage(ChatColor.AQUA +  "  /koueki quit <交易品名>");
		sender.sendMessage(ChatColor.GOLD +  "   交易モードを終了します。");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.DARK_GREEN + "----------------------------------");
	}
	
	public static void AdminCommands(CommandSender sender)
	{
		sender.sendMessage(ChatColor.DARK_GREEN + "-----------" + ChatColor.GOLD + "交易 運営用ヘルプ" +
	                       ChatColor.DARK_GREEN + "-----------");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.AQUA +  "   /koueki create <交易品名>");
		sender.sendMessage(ChatColor.GOLD +  "    交易品を作成します。");
		sender.sendMessage(ChatColor.AQUA +  "   /koueki delete <交易品名>");
		sender.sendMessage(ChatColor.GOLD +  "    交易品を削除します。");
		sender.sendMessage(ChatColor.AQUA +  "   /koueki start <交易品名> <プレイヤー名>");
		sender.sendMessage(ChatColor.GOLD +  "    交易モードに設定します。");
		sender.sendMessage(ChatColor.AQUA +  "   /koueki quit <交易品名> <プレイヤー名>");
		sender.sendMessage(ChatColor.GOLD +  "    交易モードを終了します。");
		sender.sendMessage(ChatColor.AQUA +  "   /koueki info <交易品名>");
		sender.sendMessage(ChatColor.GOLD +  "    交易品の情報を表示します。");
		sender.sendMessage(ChatColor.AQUA +  "   /koueki list <交易品名> <ページ数>");
		sender.sendMessage(ChatColor.GOLD +  "    交易品一覧を表示します。");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.DARK_GREEN + "----------------------------------------");
	}
	
	public static void KouekiInfo(CommandSender sender, KouekiSetup g)
	{
		sender.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "交易品 " +
		ChatColor.GOLD + g.getSID() + ChatColor.GREEN + " の情報を表示します");
		sender.sendMessage(ChatColor.AQUA + "アイテム名： " + ChatColor.DARK_PURPLE + g.getName());
		sender.sendMessage(ChatColor.AQUA + "アイテム販売価格： " + ChatColor.DARK_PURPLE + g.getprice());
		sender.sendMessage(ChatColor.AQUA + "アイテム売却最高価格： " + ChatColor.DARK_PURPLE + g.getmaxp());
		sender.sendMessage(ChatColor.AQUA + "アイテム売却最低価格： " + ChatColor.DARK_PURPLE + g.getminp());
		sender.sendMessage(ChatColor.AQUA + "ポイント： " + ChatColor.DARK_PURPLE + g.getpoint() +
				Process.getMain().conf.getString("Point-Unit"));
	}

	public static void HasnotPermissionMsg(CommandSender sender)
	{
		sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "権限がありません。");
	}

	public static void ConsoleMessage(CommandSender sender)
	{
		sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "このコマンドはプレイヤーのみ使用可能です");
	}

	@SuppressWarnings("unused")
	private static void saveInventory(Player player)
	{
		//インベントリの保存
		tempInventory = Bukkit.createInventory(player, 5 * 9);
		for(ItemStack item : player.getInventory().getContents())
		{
			if(item != null)
			{
				tempInventory.addItem(item);
			}
		}
		//防具の保存
		tempArmors = Bukkit.createInventory(player, 9);
		for(int index=0; index<4; index++)
		{
			ItemStack armor = player.getInventory().getArmorContents()[index];
			if(armor != null)
			{
				tempArmors.setItem(index, armor);
			}
		}
		//インベントリの消去とアップデート
		player.getInventory().clear();
		player.getInventory().setArmorContents(new ItemStack[]
				{
				new ItemStack(Material.AIR),
				new ItemStack(Material.AIR),
				new ItemStack(Material.AIR),
				new ItemStack(Material.AIR),
				});
		player.updateInventory();

		//レベルと経験値
		tempLevel = player.getLevel();
		tempExp = player.getExp();
		player.setLevel(0);
		player.setExp(0);

		// インベントリの消去
		player.getInventory().clear();
		player.getInventory().setArmorContents(new ItemStack[]
		{
				new ItemStack(Material.AIR),
				new ItemStack(Material.AIR),
				new ItemStack(Material.AIR),
				new ItemStack(Material.AIR),
		});
	}

	@SuppressWarnings("unused")
	private static void loadInventory(Player player)
	{
		// インベントリと防具の復帰、更新
		for ( ItemStack item : tempInventory.getContents() )
		{
			if ( item != null )
			{
				player.getInventory().addItem(item);
			}
		}
		ItemStack[] armorCont = new ItemStack[4];
		for ( int index=0; index<4; index++ ) {
			ItemStack armor = tempArmors.getItem(index);
			if ( armor != null )
			{
				armorCont[index] = armor;
			} else {
				armorCont[index] = new ItemStack(Material.AIR);
			}
			player.getInventory().setArmorContents(armorCont);
		}
		player.updateInventory();

		// 保存領域を消去
		tempInventory.clear();
		tempInventory = null;
		tempArmors.clear();
		tempArmors = null;

		// レベルと経験値の復帰
		player.setLevel(tempLevel);
		player.setExp(tempExp);
	}
}
