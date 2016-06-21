package jp.seiya0818.KouekiSystem;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class SaveRestoreInventory
{
	public static Map<String, Map<Integer, ItemStack>> BeforeInv = new HashMap<>();
	private static Map<String, ItemStack> Helmet = new HashMap<>();
	private static Map<String, ItemStack> Chestplate = new HashMap<>();
	private static Map<String, ItemStack> Leggings = new HashMap<>();
	private static Map<String, ItemStack> Boots = new HashMap<>();

	private static int tempLevel;
	private static float tempExp;

	public static void savePlayerInventory(Player player)
	{
		String name = player.getName();
		Map<Integer, ItemStack> inv = new HashMap<>();
		for(int i = 0; i <= 35; i++)
		{
			ItemStack item = player.getInventory().getItem(i);
			if(item == null)
				item = new ItemStack(Material.AIR);
			inv.put(i, item);
		}
		BeforeInv.put(name, inv);
		Helmet.put(name, player.getInventory().getHelmet());
		Chestplate.put(name, player.getInventory().getChestplate());
		Leggings.put(name, player.getInventory().getLeggings());
		Boots.put(name, player.getInventory().getBoots());

		tempLevel = player.getLevel();
		tempExp = player.getExp();
		player.setLevel(0);
		player.setExp(0);

		clearInventory(player);
	}

	public static boolean restorePlayerInventory(Player player)
	{
		PlayerInventory inv = player.getInventory();
		if(BeforeInv.containsKey(player.getName())
				&& Helmet.containsKey(player.getName())
				&& Chestplate.containsKey(player.getName())
				&& Leggings.containsKey(player.getName())
				&& Boots.containsKey(player.getName()))
		{
			for(int i = 0; i <= 35; i++)
				inv.setItem(i, BeforeInv.get(player.getName()).get(i));
			inv.setHelmet(Helmet.get(player.getName()));
			inv.setChestplate(Chestplate.get(player.getName()));
			inv.setLeggings(Leggings.get(player.getName()));
			inv.setBoots(Boots.get(player.getName()));

			BeforeInv.remove(player.getName());
			Helmet.remove(player.getName());
			Chestplate.remove(player.getName());
			Leggings.remove(player.getName());
			Boots.remove(player.getName());

			player.setLevel(tempLevel);
			player.setExp(tempExp);

			return true;
		}
		return false;
	}

	public static void clearInventory(Player player)
	{
		player.getInventory().clear();
		player.getInventory().setArmorContents(new ItemStack[]{
				new ItemStack(Material.AIR),
				new ItemStack(Material.AIR),
				new ItemStack(Material.AIR),
				new ItemStack(Material.AIR),
		});
		updateInventory(player);
	}

	private static void updateInventory(Player player)
	{
		player.updateInventory();
	}
}
