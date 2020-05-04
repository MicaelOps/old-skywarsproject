package br.com.logicmc.skywars.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.logicmc.core.account.addons.Group;
import br.com.logicmc.core.addons.enums.Messages;
import br.com.logicmc.skywars.SkyMain;

public class Tempo implements CommandExecutor{

	private final SkyMain plugin;
	
	public Tempo(SkyMain plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
	
		if(sender instanceof Player) {
			if(!plugin.playermanager.hasPermission(((Player) sender).getUniqueId(), Group.MOD)) {
				plugin.messagehandler.sendMessage(sender, Messages.NO_PERM);
				return false;
			}
		}
		
		if(args.length == 0) 
			sender.sendMessage("§cPara mudar tempo use /tempo <sec>");
		else {
			try {
				int tempo = Integer.parseInt(args[0]);
				
				if(tempo > 2000)
					tempo= 2000;
				
				if(tempo <=0)
					tempo = 1;
				
				plugin.getGamelogic().getPhasecontroller().setTime(tempo);
				sender.sendMessage("§aTempo modificado com sucesso");
			} catch (Exception e) {
				sender.sendMessage("§cUm erro ocorreu. Tente de novo");
			}
		}
		
		return false;
	}

}
