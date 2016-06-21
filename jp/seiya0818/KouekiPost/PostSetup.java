package jp.seiya0818.KouekiPost;

import java.io.File;
import java.io.IOException;

import jp.seiya0818.Koueki;
import jp.seiya0818.Process;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PostSetup
{
	private String sid;
	private String name;
	public static final char COLOR_KEY = '&';
	public final static String PostMeta = "PostMeta";

	public PostSetup(String sid)
	{
		setSID(sid);
		setname("新規交易所");

		saveConfig();
	}

	public PostSetup(String sid, FileConfiguration conf)
	{
		setSID(sid);
		loadConfig(conf);
	}

	public void saveConfig()
	{
		FileConfiguration conf = getConfig();
		conf.set("Name", this.name);
		save(conf);
	}

	public void delete()
	{
		getFile().delete();
		PostProcess.removepost(this.sid);
	}

	  public File getFile()
	  {
	    File dafl = Process.getMain().getDataFolder();
	    if (!dafl.exists())
	    {
	      dafl.mkdir();
	    }
	    File gdsfl = new File(dafl, "posts");
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
		setname(conf.getString("Name"));
	}

	public static void refreshPostscreate(CommandSender sender, PostSetup p)
	{
		String psid = p.getSID();
		{
			for(int i = 0; i < PostProcess.getpostlist().size(); i++)
			{
				PostSetup post = PostProcess.getpostlist().get(i);
				String postsid = post.getSID();
				if(psid == postsid && PostProcess.getpostlist().size() != 1)
				{
					FileConfiguration conf = p.getConfig();
					for(int x = 0; x < PostProcess.getpostlist().size(); x++)
					{
						String postname = PostProcess.getpostlist().get(x).getSID();
						if(psid != postname)
						{
							conf.set(postname, 1.0);
						}
						p.save(conf);
					}
				}
				else if(psid != postsid)
				{
					FileConfiguration conf = post.getConfig();
					conf.set(psid, 1.0);
					post.save(conf);
				}
			}
		}
	}

	public static void refreshPostsdelete(CommandSender sender, PostSetup p)
	{
		int posts = PostProcess.getpostlist().size();
		if (posts == 0)
		{
			return;
		}
		if (posts > 0)
		{
			for(PostSetup post: PostProcess.getpostlist())
			{
				if(post != p)
				{
					FileConfiguration conf =  post.getConfig();
					conf.set(p.getSID(), null);
					post.save(conf);
				}
			}
		}
	}

	public void setSID(String sid)
	{
		this.sid = sid;
	}

	public void setname(String name)
	{
		this.name = name;
	}

	public String getSID()
	{
		return this.sid;
	}

	public String getName()
	{
		return this.name;
	}

	private void save(FileConfiguration conf)
	{
		try
		{
			conf.save(getFile());
		}
		catch(IOException e)
		{
			Process.getMain().getLogger().warning(Koueki.LoggerPrefix + "コンフィグの保存に失敗しました。" + this.sid);
		}
	}
}
