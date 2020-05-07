package br.com.logicmc.skywars.kit;

import br.com.logicmc.skywars.utils.FixedItems;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public enum Kit {

    GOKU("goku", FixedItems.GOKU, new ItemStack(Material.ENDER_PEARL, 2));

    private final String name;
    private final ItemStack menudisplay;
    private final ItemStack[] items;
    Kit(String name, FixedItems menudisplay, ItemStack... applykit) {
        this.name=name;
        this.menudisplay=menudisplay.buildStack();
        this.items=applykit;
    }

    public String getName() {
        return name;
    }

    public ItemStack getMenudisplay() {
        return menudisplay;
    }

    public void apply(Player player) {
        player.getInventory().addItem(items);
    }
}
