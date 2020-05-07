package br.com.logicmc.skywars.commands;

import br.com.logicmc.core.account.addons.Group;
import br.com.logicmc.core.addons.enums.Messages;
import br.com.logicmc.skywars.SkyMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawn implements CommandExecutor {

    private final SkyMain plugin;

    public SetSpawn(SkyMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(sender instanceof Player) {
            if(!plugin.playermanager.hasPermission(((Player) sender).getUniqueId(), Group.HELPER)) {
                plugin.messagehandler.sendMessage(sender, Messages.NO_PERM);
                return false;
            }

            if(strings.length == 0)
                sender.sendMessage(ChatColor.RED+"Para usar este comando use /setspawn <lobby/ilha>");
            else if(strings[0].toLowerCase().equalsIgnoreCase("lobby")){
                plugin.getConfigfile().setLocation("spawn", (((Player)sender).getLocation()));
                plugin.getGamelogic().getIslands().add((((Player)sender).getLocation()));
                sender.sendMessage(ChatColor.GREEN+"O spawn do lobby foi setado com sucesso");
            } else if(strings[0].toLowerCase().equalsIgnoreCase("ilha")) {
                plugin.getConfigfile().setLocation("ilhas.ilha"+plugin.getGamelogic().getIslands().size(), (((Player)sender).getLocation()));
                sender.sendMessage(ChatColor.GREEN+"Ilha foi adicionada com sucesso");
            } else
                sender.sendMessage(ChatColor.RED+"Para usar este comando use /setspawn <lobby/ilha>");
        }
        return false;
    }
}
