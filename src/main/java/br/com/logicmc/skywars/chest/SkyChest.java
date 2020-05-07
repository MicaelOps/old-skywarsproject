package br.com.logicmc.skywars.chest;

import org.bukkit.Location;

public class SkyChest {

    private final Location location;
    private final int type;

    public SkyChest(Location location, int type) {
        this.location=location;
        this.type=type;
    }

    public Location getLocation() {
        return location;
    }

    public int getType() {
        return type;
    }
}
