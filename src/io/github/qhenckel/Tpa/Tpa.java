package io.github.qhenckel.Tpa;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Tpa extends JavaPlugin implements Listener{
	
	ArrayList<String> tp = new ArrayList<String>();
	double grace;
	//      TO      From
	HashMap<Player, Player> teleports = new HashMap<Player, Player>();
	HashMap<String, Location> history = new HashMap<String, Location>();
	
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable() {
		
	}
	
	public void sendMessage(Player to, Player from){
		from.sendMessage("Telport request sent to: " + to.getName());
		to.sendMessage(from.getName() + " Wants to teleport to you.");
		to.sendMessage("/tpa to accept the request.");
		to.sendMessage("/tpd to deny the request.");
		return;
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
					sendMessage(to, from);
					
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
				return true;
			}
			
			if(cmd.getName().equalsIgnoreCase("tpall")) {
				Player to = p;
				for(Player from : Bukkit.getOnlinePlayers()){
					teleports.put(to, from);
					sendMessage(to, from);
				}
				return true;
			}
			
			if(cmd.getName().equalsIgnoreCase("back")){
				Location fLoc = history.get(p.getPlayer().getName() + "1");
				Location sLoc = history.get(p.getPlayer().getName() + "2");
				if(sLoc == null){
					if(fLoc == null){
						p.sendMessage("No Location to return to!");
						return true;
					} else {
						p.sendMessage("Returning to last teleport.");
						p.teleport(fLoc);
						return true;
					}
				} else {
					p.sendMessage("Returning to last teleport.");
					p.teleport(sLoc);
					return true;
				}
			}
		} else {
			sender.sendMessage("you must be a player");
		}
		return false;
	}
	
	@EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
		String first = e.getPlayer().getName() + "1";
		String sec = e.getPlayer().getName() + "2";
		
		if(history.containsKey(first)){
			history.put(sec, history.get(first));
		}
		history.put(first, e.getTo());
		return;
    }
	
}
