package jp.seiya0818;

import java.util.ArrayList;
import java.util.List;

import jp.seiya0818.KouekiPost.PostSetup;
import jp.seiya0818.KouekiPriceFluctuation.PriceSystem;
import jp.seiya0818.KouekiShop.ShopProcess;
import jp.seiya0818.KouekiShop.ShopSetup;
import jp.seiya0818.KouekiSystem.KouekiHorse;
import jp.seiya0818.KouekiSystem.KouekiSystem;
import jp.seiya0818.KouekiSystem.KouekiTools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class KouekiGUI implements Listener
{
	public final static String KGUIMeta = "KouekiGUIMeta";
	public final static String StGUIMeta = "KouekiStartGUIMeta";
	public final static String ShGUIMeta = "KouekiShopGUIMeta";

	public KouekiGUI()
	{
		Process.getMain().getServer().getPluginManager().registerEvents(this, Process.getMain());
	}

	public static void OpenKGUI(Player player, PostSetup p)
	{
		Inventory inv;
		inv = Bukkit.createInventory(null, 9, ChatColor.BLUE + "" + ChatColor.BOLD + "交易メニュー");
		List<String> StLore = new ArrayList<String>();
		List<String> QLore = new ArrayList<String>();
		List<String> ShLore = new ArrayList<String>();

		//スタート
		ItemStack Start = new ItemStack(Material.STAINED_CLAY);
		ItemMeta StartMeta = Start.getItemMeta();
		StartMeta.setDisplayName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Start");
		StLore.add(ChatColor.GOLD + p.getSID() + ChatColor.DARK_PURPLE + " の交易開始メニュー");
		StartMeta.setLore(StLore);
		Start.setItemMeta(StartMeta);
		Start.setDurability((short) 5);

		//クイット
		ItemStack Quit = new ItemStack(Material.STAINED_CLAY);
		ItemMeta QuitMeta = Quit.getItemMeta();
		QuitMeta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Quit");
		QLore.add(ChatColor.GOLD + p.getSID() + ChatColor.DARK_PURPLE + " で交易を終了");
		QuitMeta.setLore(QLore);
		Quit.setItemMeta(QuitMeta);
		Quit.setDurability((short) 14);

		//マーケット
		ItemStack Shop = new ItemStack(Material.STAINED_CLAY);
		ItemMeta ShopMeta = Shop.getItemMeta();
		ShopMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Shop");
		ShLore.add(ChatColor.GOLD + p.getSID() + ChatColor.DARK_PURPLE + " のショップメニュー");
		ShopMeta.setLore(ShLore);
		Shop.setItemMeta(ShopMeta);
		Shop.setDurability((short) 3);

		inv.setItem(1, Start);
		inv.setItem(4, Quit);
		inv.setItem(7, Shop);

		player.openInventory(inv);
		player.setMetadata(KGUIMeta, new FixedMetadataValue(Koueki.instance, p));
	}

	public void StartSelected(Player player)
	{
		PostSetup p = null;
		List<String> GLore = new ArrayList<String>();
		int y = -1;
		Inventory inv;
		inv = Bukkit.createInventory(null, 27, ChatColor.GREEN + "" + ChatColor.BOLD + "Select Goods");
		for(MetadataValue meta : player.getMetadata(KGUIMeta))
		{
			p = (PostSetup) meta.value();
			player.setMetadata(StGUIMeta, new FixedMetadataValue(Koueki.instance, p));
		}
		if(p == null)
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "交易所の取得に失敗しました。");
			player.removeMetadata(KGUIMeta, Koueki.instance);
			player.closeInventory();
			return;
		}
		for(int i = 0; i < Process.getgoodslist().size(); i++)
		{
			KouekiSetup g = Process.getgoodslist().get(i);
			if(Process.getgoodslist().size() == 0)
			{
				player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "一つも交易品が作成されていません。");
				player.removeMetadata(KGUIMeta, Koueki.instance);
				player.closeInventory();
				break;
			}
			if(p.getSID().equals(g.getpost()))
			{
				String[] args = g.getGUIItem().split(":", 0);
				Material material = Material.getMaterial(args[0].toUpperCase());
				int intdur = Integer.valueOf(args[1]);
				short dur = (short) intdur;
				ItemStack item = new ItemStack(material, 1);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(g.getSID());
				GLore.add(PriceSystem.getPrice(g));
				meta.setLore(GLore);
				GLore.clear();
				item.setItemMeta(meta);
				item.setDurability(dur);
				y = y + 1;
				if(y > 17)
				{
					player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "交易所あたりの最大交易品の数を超えました。");
					break;
				}
				inv.setItem(y, item);
			}
		}

		ItemStack Close = new ItemStack(Material.STAINED_GLASS_PANE);
		ItemMeta CloseMeta = Close.getItemMeta();
		CloseMeta.setDisplayName(ChatColor.DARK_RED + "Close");
		Close.setItemMeta(CloseMeta);
		Close.setDurability((short) 14);

		inv.setItem(22, Close);

		player.removeMetadata(KGUIMeta, Koueki.instance);
		player.openInventory(inv);
	}

	public void QuitSelected(Player player)
	{
		if(!player.hasMetadata(KouekiSystem.KouekiMeta) || !player.hasMetadata(PostSetup.PostMeta)
				|| !player.hasMetadata(KGUIMeta))
		{
			KouekiMessages.isnotinKouekiMsg(player);
			return;
		}
		else if(!player.hasPermission("koueki.quit"))
		{
			KouekiMessages.HasnotPermissionMsg(player);
			return;
		}
		else
		{
			for(MetadataValue pmeta : player.getMetadata(KGUIMeta))
			{
				PostSetup p = (PostSetup) pmeta.value();
				for(MetadataValue gmeta : player.getMetadata(KouekiSystem.KouekiMeta))
				{
					KouekiSetup g = (KouekiSetup) gmeta.value();
					KouekiSystem.QuitKoueki(player, g, p);
				}
			}
			player.removeMetadata(KGUIMeta, Koueki.instance);
		}
	}

	public void ShopSelected(Player player)
	{
		PostSetup p = null;
		List<String> SLore = new ArrayList<String>();
		int y = 18;
		Inventory inv;
		inv = Bukkit.createInventory(null, 54, ChatColor.GREEN + "" + ChatColor.BOLD + "Item Shop");
		for(MetadataValue meta : player.getMetadata(KGUIMeta))
		{
			p = (PostSetup) meta.value();
			player.setMetadata(ShGUIMeta, new FixedMetadataValue(Koueki.instance, p));
		}
		if(p == null)
		{
			player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "交易所の取得に失敗しました。");
			player.removeMetadata(KGUIMeta, Koueki.instance);
			player.closeInventory();
			return;
		}
		for(int i = 0; i < ShopProcess.getshoplist().size(); i++)
		{
			ShopSetup s = ShopProcess.getshoplist().get(i);
			FileConfiguration conf = s.getConfig();
			if(ShopProcess.getshoplist().size() == 0)
			{
				player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "一つも商品が作成されていません。");
				player.removeMetadata(KGUIMeta, Koueki.instance);
				player.closeInventory();
				break;
			}
			if(p.getSID().equals(s.getpost()))
			{
				String[] args = s.getitem().split(":", 0);
				Material material = Material.getMaterial(args[0].toUpperCase());
				int intdur = Integer.valueOf(args[1]);
				short dur = (short) intdur;
				int amount = Integer.valueOf(args[2]);
				ItemStack item = new ItemStack(material, amount);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(s.getSID());
				if(conf.getString("Description") != null)
				{
					SLore.add(s.getConfig().getString("Description"));
					meta.setLore(SLore);
					SLore.clear();
				}
				item.setItemMeta(meta);
				item.setDurability(dur);
				y = y + 1;
				if(y > 45)
				{
					player.sendMessage(Koueki.PlayerPrefix + ChatColor.RED + "交易所あたりの最大商品の数を超えました。");
					break;
				}
				inv.setItem(y, item);
			}
		}

		ItemStack boots = KouekiTools.getWingShoose();
		ItemStack clothes = KouekiTools.getKouekiClothes();
		ItemStack egg = KouekiHorse.getKouekiHorseEgg();

		inv.setItem(0, boots);
		inv.setItem(1, clothes);
		inv.setItem(2, egg);

		ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE);
		int x = 18;
		for(int i = 9; i < x; i++)
		{
			inv.setItem(i, pane);
		}

		ItemStack Close = new ItemStack(Material.STAINED_GLASS_PANE);
		ItemMeta CloseMeta = Close.getItemMeta();
		CloseMeta.setDisplayName(ChatColor.DARK_RED + "Close");
		Close.setItemMeta(CloseMeta);
		Close.setDurability((short) 14);

		inv.setItem(49, Close);

		player.removeMetadata(KGUIMeta, Koueki.instance);
		player.openInventory(inv);
	}

	@EventHandler
	public void onClickKouekiGUI(InventoryClickEvent e)
	{
		Player player = (Player) e.getWhoClicked();
		if(ChatColor.stripColor(e.getInventory().getName()).equalsIgnoreCase("交易メニュー"))
		{
			e.setCancelled(true);
			if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR
					|| !e.getCurrentItem().hasItemMeta())
			{
				return;
			}
			ItemStack item = e.getCurrentItem();
			if(item.getType().equals(Material.STAINED_CLAY))
			{
				if(item.getDurability() == (short) 5)
				{
					this.StartSelected(player);
				}
				else if(item.getDurability() == (short) 14)
				{
					this.QuitSelected(player);
				}
				else if(item.getDurability() == (short) 3)
				{
					this.ShopSelected(player);
				}
			}
		}
		if(ChatColor.stripColor(e.getInventory().getName()).equalsIgnoreCase("Select Goods"))
		{
			e.setCancelled(true);
			if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR
					|| !e.getCurrentItem().hasItemMeta())
			{
				return;
			}
			ItemStack item = e.getCurrentItem();
			KouekiSetup g = Process.getgoods(item.getItemMeta().getDisplayName());
			if(!player.hasPermission("koueki.start"))
			{
				KouekiMessages.HasnotPermissionMsg(player);
				return;
			}
			else if(item.getType().equals(Material.STAINED_GLASS_PANE) && item.getDurability() == (short) 14
					&& item.getItemMeta().getDisplayName().equals(ChatColor.DARK_RED + "Close"))
			{
				player.removeMetadata(StGUIMeta, Koueki.instance);
				player.closeInventory();
			}
			else
			{
				KouekiSystem.StartKoueki(player, g);
				player.removeMetadata(StGUIMeta, Koueki.instance);
				player.closeInventory();
			}
		}
		if(ChatColor.stripColor(e.getInventory().getName()).equalsIgnoreCase("Item Shop"))
		{
			e.setCancelled(true);
			if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR
					|| !e.getCurrentItem().hasItemMeta())
			{
				return;
			}
			ItemStack item = e.getCurrentItem();
			if(item.getType().equals(Material.LEATHER_BOOTS) && item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "ウィングシューズ"))
			{
				KouekiTools.buyWingShoose(player);
			}
			else if(item.getType().equals(Material.LEATHER_CHESTPLATE) && item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "交易服"))
			{
				KouekiTools.buyKouekiClothes(player);
			}
			else if(item.getType().equals(Material.MONSTER_EGG) && item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "馬"))
			{
				KouekiHorse.buyKouekiHorse(player);
			}
			else if(item.getType().equals(Material.STAINED_GLASS_PANE) && item.getDurability() == (short) 14
					&& item.getItemMeta().getDisplayName().equals(ChatColor.DARK_RED + "Close"))
			{
				player.removeMetadata(StGUIMeta, Koueki.instance);
				player.closeInventory();
			}
		}
	}

	@EventHandler
	public void onCloseEnventory(InventoryCloseEvent e)
	{
		Player player = (Player) e.getPlayer();
		if(player.hasMetadata(KGUIMeta))
		{
			player.removeMetadata(KGUIMeta, Koueki.instance);
		}
		if(player.hasMetadata(StGUIMeta))
		{
			player.removeMetadata(StGUIMeta, Koueki.instance);
		}
	}
}
