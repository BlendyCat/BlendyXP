package me.blendycat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BlendyXP extends JavaPlugin{
	@Override
	public void onEnable(){
		getLogger().info("BlendyXP has been enabled");
		this.getConfig().addDefault("version", 1.0);
		this.getConfig().options().copyDefaults(true);
		if(!this.getConfig().contains("name")){
			this.getConfig().createSection("name");
			this.getConfig().set("name", "&8[&3Blendy&cXP&8]");
		}
		saveConfig();
	}
	@Override
	public void onDisable(){
		getLogger().info("BlendyXP has been disabled");
		saveConfig();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("xpstore") && sender instanceof Player){
			Player player = (Player) sender;
			String configName;
			String UUID = player.getUniqueId().toString();
			//initializes configName
			if(!getConfig().getString("name").isEmpty() && getConfig().isString("name")){
				configName = (String) getConfig().get("name").toString();
			}else{
				getConfig().set("name", "&8[&3Blendy&cXP&8]");
				configName = (String) getConfig().get("name").toString();
			}	
			//If player enters no arguments
			if(args.length == 0){
				player.sendMessage(ChatColor.translateAlternateColorCodes
						('&', configName+" &c/xpstore deposit <levels> &6to store XP\n"+
				configName+" &c/xpstore withdraw <levels> &6to withdraw XP\n"+
								configName+" &c/xpstore xp &6to see how much stored xp you have"));
			}else if(args.length >= 1){
				//command that shows how much xp you have in storage
				if(args[0].equalsIgnoreCase("xp")){
					if(getConfig().contains(UUID) && getConfig().getInt(UUID) != 0){
						int value = getConfig().getInt(UUID);
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', configName+" &6You have &c"+value+" &6XP levels stored!"));
					}else player.sendMessage(ChatColor.translateAlternateColorCodes('&', configName+" &cYou do not have any stored XP levels!"));
				//command that deposits xp in your xp storage
				}else if(args[0].equalsIgnoreCase("deposit")){
					if(args.length == 2){
						int value = Integer.parseInt(args[1]);
						if(player.getLevel()>=value){
							//Checks for player in config database
							if(getConfig().contains(UUID)){
								int preValue = getConfig().getInt(UUID);
								getConfig().set(UUID, preValue+value);
							}else{
								getConfig().createSection(UUID);
								getConfig().set(UUID, value);
							}
							player.giveExpLevels(-value);
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', configName+" &6Stored &c"+value+" &6XP levels!"));
						}else player.sendMessage(ChatColor.translateAlternateColorCodes('&', configName+" &cYou do not have "+value+" XP levels!"));
					}else player.sendMessage(ChatColor.translateAlternateColorCodes('&', configName+" &cInvalid arguments!"));
				//command that withdraws xp levels from storage
				}else if(args[0].equalsIgnoreCase("withdraw")){
					if(args.length==2){
						int value = Integer.parseInt(args[1]);
						if(getConfig().contains(UUID) && getConfig().getInt(UUID) >=value){
							int preValue = getConfig().getInt(UUID);
							getConfig().set(UUID, preValue-value);
							player.giveExpLevels(value);
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', configName+" &6Withdrew &c"+value+"&6 XP levels!"));
						}else player.sendMessage(ChatColor.translateAlternateColorCodes('&', configName+" &cYou do not have "+value+" XP levels stored!"));
					}else player.sendMessage(ChatColor.translateAlternateColorCodes('&', configName+" &cInvalid arguments!"));
				}else player.sendMessage(ChatColor.translateAlternateColorCodes('&', configName+" &cThat is not a valid command!"));
			}
			return true;
		}
		return false;
	}
}
