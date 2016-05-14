package jp.seiya0818;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class KouekiCommandExecutor implements CommandExecutor
{

	private Player player;
	private Inventory tempInventory;
	private Inventory tempArmors ;
	private int tempLevel;
	private float tempExp;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		switch(args.length)
		{
		case 1:
			if(args[0].equalsIgnoreCase("start"))
			{
				if(!(sender instanceof Player))
				{
					sender.sendMessage(ChatColor.RED + "このコマンドはプレイヤーのみ使用可能です");
					return true;
				}

				if(!sender.hasPermission("koueki.start"))
				{
					sender.sendMessage(ChatColor.RED + "権限がありません");
					return true;
				}
				if(sender.hasPermission("koueki.start"))
				{
					sender.sendMessage(ChatColor.GREEN + "交易モードを開始しました");
					Player player = (Player) sender;
					player.setGameMode(GameMode.ADVENTURE);
					moveToTempInventory();
					return true;
				}
			}
			if(args[0].equalsIgnoreCase("quit"))
			{
				if(!(sender instanceof Player))
				{
					sender.sendMessage(ChatColor.RED + "このコマンドはプレイヤーのみ使用可能です");
					return true;
				}
				if(!sender.hasPermission("koueki.quit"))
				{
					sender.sendMessage(ChatColor.RED + "権限がありません");
					return true;
				}
				if(sender.hasPermission("koueki.quit"))
				{
					sender.sendMessage(ChatColor.GREEN + "交易モードを終了しました");
					Player player = (Player) sender;
					player.setGameMode(GameMode.SURVIVAL);
					restoreInventory();
					return true;
				}
			}
			break;
		case 2:
			if(args[0].equalsIgnoreCase("start"))
			{
				if(!(sender.hasPermission("koueki.start.others")))
				{
					sender.sendMessage(ChatColor.RED + "権限がありません");
					return true;
				}
				if(sender.hasPermission("koueki.start.others"))
				{
					Player target = getPlayer(args[1]);
					if(target == null)
					{
						sender.sendMessage(ChatColor.RED + "プレイヤーはオフラインです。");
						return true;
					}
					target.setGameMode(GameMode.ADVENTURE);
					moveToTempInventory();
					return true;
				}
			}
			if(args[0].equalsIgnoreCase("start"))
			{
				if(!(sender.hasPermission("koueki.start.others")))
				{
					sender.sendMessage(ChatColor.RED + "権限がありません");
					return true;
				}
				if(sender.hasPermission("koueki.admin"))
				{
					Player target = getPlayer(args[1]);
					if(target == null)
					{
						sender.sendMessage(ChatColor.RED + "プレイヤーはオフラインです。");
						return true;
					}
					target.setGameMode(GameMode.SURVIVAL);
					restoreInventory();
					return true;
				}
			}
			break;
		default:
			sender.sendMessage(ChatColor.GOLD +  "======【/koueki start】=====");
			sender.sendMessage(ChatColor.GOLD +  "====交易モードに設定します===");
			sender.sendMessage(ChatColor.GOLD +  "======【/koueki quit】=====");
			sender.sendMessage(ChatColor.GOLD +  "====交易モードを終了します===");
			break;
		}
		return false;
	}

	private void moveToTempInventory()
	{
		//インベントリの保存
		tempInventory = Bukkit.createInventory(player, 5*9);
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
		updateInventory(player);
		
		//レベルと経験値
		tempLevel = player.getLevel();
		tempExp = player.getExp();
		player.setLevel(0);
		player.setExp(0);
	}
	//インベントリ復帰
    private void restoreInventory()
    {

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
        updateInventory(player);

        // 保存領域を消去
        tempInventory.clear();
        tempInventory = null;
        tempArmors.clear();
        tempArmors = null;

        // レベルと経験値の復帰
        player.setLevel(tempLevel);
        player.setExp(tempExp);
    }

	private void updateInventory(Player player)
	{
		player.updateInventory();
	}
	public static Player getPlayer(String name)
	{
		return Bukkit.getPlayerExact(name);
	}
}