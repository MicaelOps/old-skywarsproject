package br.com.logicmc.skywars.utils;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.logicmc.core.message.MessageHandler;
import br.com.logicmc.skywars.messages.SkyMessages;

public enum FixedItems {


	GOKU(Material.ENDER_PEARL, SkyMessages.KIT_GOKU),

	SPECTATE_PLAYERS(Material.ENDER_PEARL, SkyMessages.SPECTATE_PLAYERS),
	SPECTATE_JOINLOBBY(Material.REDSTONE, SkyMessages.SPECTATE_JOINLOBBY),
	SPECTATE_JOINNEXT(Material.GLOWSTONE_DUST, SkyMessages.SPECTATE_JOINNEXT),
	KIT_MENU(Material.CHEST, SkyMessages.KITS_ITEM),
	ABILITY_MENU(Material.ENDER_CHEST, SkyMessages.ABILITY_ITEM);
	
	private final Material material;
	private final SkyMessages name;
	private final String[] lore;
	private final ItemStack layout;

	FixedItems(Material material, SkyMessages name, String... lore) {
		this.material= material;
		this.name = name;
		this.lore = lore;
		this.layout = buildStack();
	}
	
	public ItemStack buildStack() {
		ItemStack stack = new ItemStack(material);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name.name());
		if(lore != null && lore.length != 0)
			meta.setLore(Arrays.asList(lore));
		stack.setItemMeta(meta);
		return stack;
	}
	public ItemStack getBuild(MessageHandler handler, String lang) {
		ItemStack stack = this.layout.clone();
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(handler.getMessage(name, lang));
		stack.setItemMeta(meta);
		return stack;
	}
}
