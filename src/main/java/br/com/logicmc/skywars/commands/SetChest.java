package br.com.logicmc.skywars.commands;

import br.com.logicmc.core.account.addons.Group;
import br.com.logicmc.core.addons.enums.Messages;
import br.com.logicmc.skywars.SkyMain;
import br.com.logicmc.skywars.chest.ChestManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetChest implements CommandExecutor {

    private final SkyMain plugin;

    public SetChest(SkyMain plugin) {
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
                sender.sendMessage(ChatColor.RED + "Fique em cima de um bau e digite /setchest <normal/feast>");
            else {
                Location location = ((Player)sender).getLocation();
                Block chestblock = (Block)location.subtract(0,1,0).getBlock().getRelative(BlockFace.DOWN);
                if(chestblock.getType() == Material.CHEST) {

                    if(strings[0].toLowerCase().equalsIgnoreCase("normal")) {
                        ChestManager.getInstance().addChest(0, chestblock.getLocation());
                        sender.sendMessage(ChatColor.GREEN+"Bau adicionado com sucesso");
                    } else if(strings[0].toLowerCase().equalsIgnoreCase("feast")) {
                        ChestManager.getInstance().addChest(1, chestblock.getLocation());
                        sender.sendMessage(ChatColor.GREEN+"Bau adicionado com sucesso");
                    } else
                        sender.sendMessage(ChatColor.RED + "Tipo de bau errado");
                } else
                    sender.sendMessage(ChatColor.RED + "Fique em cima de um bau e digite /setchest <normal/feast>");
            }
        }
        return false;
    }
}
