package io.github.qhenckel.Tpa;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Tpa extends JavaPlugin{
	
	ArrayList<String> tp = new ArrayList<String>();
	double grace;
	//      TO      From
	HashMap<Player, Player> teleports = new HashMap<Player, Player>();
	
	public void onEnable() {
		saveDefaultConfig();
		grace = getConfig().getDouble("graceDistance");
	}
	
	public void onDisable() {
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player){
			
			Player p = (Player) sender;
			
			if(cmd.getName().equalsIgnoreCase("tpr")) {
				if(Bukkit.getPlayer(args[0]) != null){
					
					Player to = Bukkit.getPlayer(args[0]);
					Player from = p;
					
					//           to from
					teleports.put(to, from);
					
					from.sendMessage("Telport request sent to: " + to.getName());
					to.sendMessage(from.getName() + " Wants to teleport to you.");
					to.sendMessage("/tpa to accept the request.");
					to.sendMessage("/tpd to deny the request.");
					return true;
				} else {
					p.sendMessage("That player doesn't exsist!");
					return true;
				}
			}
			
			if(cmd.getName().equalsIgnoreCase("tpa")) {
				Player to = p;
				if(!teleports.containsKey(to)){
					to.sendMessage("You don't have any pending requests.");
					return true;
				}
				Player from = teleports.get(to);
				
//				long delay = getConfig().getLong("tpDelay");
//				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Teleport(to, from, from.getLocation(), grace), delay * 20);
				from.teleport(to);
				to.sendMessage("Teleporting " + from.getName());
				from.sendMessage(to.getName() + " Accepted your request.");
				return true;
			}
			
			if(cmd.getName().equalsIgnoreCase("tpd")) {
				Player to = p;
				if(!teleports.containsKey(to)){
					to.sendMessage("You don't have any pending requests.");
					return true;
				}
				Player from = teleports.get(to);
				
				teleports.remove(to);
				
				to.sendMessage("Request denied.");
				from.sendMessage(to.getName() + " denied your Teleport request.");
			}
		} else {
			sender.sendMessage("you must be a player");
		}
		return false;
	}
	
	public class Teleport implements Runnable {
    	
    	double grace;
    	Player to;
    	Player from;
    	Location fLoc;
    	
    	public Teleport(Player t, Player f, Location l, double g){
    		to = t;
    		from = f;
    		fLoc = l;
    		grace = g;
    	}
    	
		public void run() {
			if(from.getLocation().distance(fLoc) > grace){
				from.teleport(to);
			} else {
				from.sendMessage("You moved! Teleport failed!");
			}
		}
	    
	}
}
