package jp.seiya0818;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class KouekiSetup
{
	private String sid;
	private String name;
	private String id;
	private String figure;
	private String price;
	private String minp;
	private String maxp;
	public static final char COLOR_KEY = '&';
	
	public KouekiSetup(String sid)
	{
		setSID(sid);
		setName("新規交易品");
		setID("1");
		setfigure("1");
		setprice("80");
		setminprice("100");
		setmaxprice("200");
		saveConfig();
	}
	
	public KouekiSetup(String sid, FileConfiguration conf)
	{
		setSID(sid);
		loadConfig(conf);
	}
	
	public void saveConfig()
	{
		FileConfiguration conf = getConfig();
		conf.set("Name", this.name);
		conf.set("ID", this.id);
		conf.set("Figure", this.figure);
		conf.set("Price", this.price);
		conf.set("Min-price", this.minp);
		conf.set("Max-price", this.maxp);
		try
		{
			conf.save(getFile());
		}
		catch(IOException e)
		{
			Process.getMain().getLogger().warning(Koueki.LoggerPrefix + "コンフィグの保存に失敗しました。" + this.sid);
		}
	}
	
	public void delete()
	{
		getFile().delete();
		Process.removegoods(this.sid);
	}
	
	  public File getFile()
	  {
	    File dafl = Process.getMain().getDataFolder();
	    if (!dafl.exists())
	    {
	      dafl.mkdir();
	    }
	    File gdsfl = new File(dafl, "goods");
	    if (!gdsfl.exists())
	    {
	      gdsfl.mkdir();
	    }
	    return new File(gdsfl, this.sid + ".yml");
	  }
	
	public FileConfiguration getConfig()
	{
	    return YamlConfiguration.loadConfiguration(getFile());
	}
	private void loadConfig(FileConfiguration conf)
	{
		setName(conf.getString("Name"));
		setID(conf.getString("ID"));
		setfigure(conf.getString("Figure"));
		setprice(conf.getString("Price"));
		setminprice(conf.getString("Min-price"));
		setmaxprice(conf.getString("Max-price"));
	}
	
	public void setSID(String sid)
	{
		this.sid = sid;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setID(String id)
	{
		this.id = id;
	}
	
	public void setfigure(String figure)
	{
		this.figure = figure;
	}
	
	public void setprice(String price)
	{
		this.price = price;
	}
	
	public void setminprice(String minp)
	{
		this.minp = minp;
	}
	
	public void setmaxprice(String maxp)
	{
		this.maxp = maxp;
	}
	
	public String getSID()
	{
		return this.sid;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getID()
	{
		return this.id;
	}
	
	public String getFigure()
	{
		return this.figure;
	}
	
	public String getprice()
	{
		return this.price;
	}
	
	public String getminp()
	{
		return this.minp;
	}
	
	public String getmaxp()
	{
		return this.maxp;
	}
	
	public static void setupgoods (Player player, String koueki)
	{

	}
}
