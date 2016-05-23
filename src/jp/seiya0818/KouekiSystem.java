package jp.seiya0818;

import net.md_5.bungee.api.ChatColor;

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
		if(player.hasMetadata(KouekiMeta))
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "既に交易中です");
			return;
		}
		player.setMetadata(KouekiMeta, new FixedMetadataValue(Koueki.instance, player.getName()));
		player.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "交易モードを開始しました");
		player.setGameMode(GameMode.ADVENTURE);
		player.setMetadata(KouekiMeta, new FixedMetadataValue(Koueki.instance, g));
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
	}


	public static void QuitKoueki(Player player, KouekiSetup g)
	{
		if(!player.hasMetadata(KouekiMeta))
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "交易中ではありません");
			return;
		}
		if(player.hasMetadata(KouekiMeta))
		{
			player.removeMetadata("KouekiMeta", Koueki.instance);
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.GREEN + "交易モードを終了しました");
			player.setGameMode(GameMode.SURVIVAL);

			// インベントリの消去
			player.getInventory().clear();
			player.getInventory().setArmorContents(new ItemStack[]{
					new ItemStack(Material.AIR),
					new ItemStack(Material.AIR),
					new ItemStack(Material.AIR),
					new ItemStack(Material.AIR),
			});

			// インベントリと防具の復帰、更新
			for ( ItemStack item : tempInventory.getContents() ) {
				if ( item != null ) {
					player.getInventory().addItem(item);
				}
			}
			ItemStack[] armorCont = new ItemStack[4];
			for ( int index=0; index<4; index++ ) {
				ItemStack armor = tempArmors.getItem(index);
				if ( armor != null ) {
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
	
	
	public static void PlayerCommands(CommandSender sender)
	{
		sender.sendMessage(ChatColor.GREEN + "==============ヘルプ==============");
		sender.sendMessage(ChatColor.AQUA +  "-/koueki start <交易品名>");
		sender.sendMessage(ChatColor.GOLD +  "交易モードに設定します。");
		sender.sendMessage(ChatColor.AQUA +  "-/koueki quit <交易品名>");
		sender.sendMessage(ChatColor.GOLD +  "交易モードを終了します。");
	}
	public static void HasnotPermissionMsg(CommandSender sender)
	{
		sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "権限がありません。");
	}
	public static void ConsoleMessage(CommandSender sender)
	{
		sender.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "このコマンドはプレイヤーのみ使用可能です");
	}
}
