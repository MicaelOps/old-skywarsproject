package br.com.logicmc.skywars.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.logicmc.core.account.addons.Group;
import br.com.logicmc.core.addons.enums.Messages;
import br.com.logicmc.skywars.SkyMain;
import br.com.logicmc.skywars.game.addons.GamePhase;

public class StartGame implements CommandExecutor{

	private final SkyMain plugin;
	
	public StartGame(SkyMain plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		
		if(sender instanceof Player) {
			if(!plugin.playermanager.hasPermission(((Player) sender).getUniqueId(), Group.YOUTUBER)) {
				plugin.messagehandler.sendMessage(sender, Messages.NO_PERM);
				return false;
			}
		}
		
		
		if(plugin.getGamelogic().getCurrentphase() == GamePhase.WAITING) {
			plugin.getGamelogic().getPhasecontroller().setTime(5);	
		} else {
			
			// TEMPORARY ONLY FOR TESTING PURPOSES
			plugin.getGamelogic().getPhasecontroller().setTime(295); // 5 seconds before refill time 	
		}
		return false;
	}
	
}