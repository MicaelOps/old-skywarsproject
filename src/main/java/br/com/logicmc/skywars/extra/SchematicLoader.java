package br.com.logicmc.skywars.extra;

import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class SchematicLoader {

    private String name;
    private short[] blocks;
    private short width;
    private short length;
    private short height;
    private byte[] data;

    public SchematicLoader(String name, short[] blocks, byte[] data, short width, short length, short height) {
        this.name = name;
        this.blocks = blocks;
        this.data = data;
        this.width = width;
        this.length = length;
        this.height = height;
    }

    public SchematicLoader() {

    }
    public SchematicLoader read(File file) {
        return readSchematic(file,null);
    }
    public SchematicLoader read(InputStream stream){
        return readSchematic(null,stream);
    }
    private SchematicLoader readSchematic(File file, InputStream stream) {
        try {
            if(stream == null) {
                if(file != null && file.exists())
                    stream = new FileInputStream(file);
                else
                    return null;
            }
            
            NBTTagCompound nbtdata = NBTCompressedStreamTools.a(stream);

            short width = nbtdata.getShort("Width");
            short height = nbtdata.getShort("Height");
            short length = nbtdata.getShort("Length");

            byte[] blocks = nbtdata.getByteArray("Blocks");
            byte[] data = nbtdata.getByteArray("Data");

            byte[] addId = new byte[0];

            if (nbtdata.hasKey("AddBlocks")) {
                addId = nbtdata.getByteArray("AddBlocks");
            }

            short[] sblocks = new short[blocks.length];
            for (int index = 0; index < blocks.length; index++) {
                if ((index >> 1) >= addId.length) {
                    sblocks[index] = (short) (blocks[index] & 0xFF);
                } else {
                    if ((index & 1) == 0) {
                        sblocks[index] = (short) (((addId[index >> 1] & 0x0F) << 8) + (blocks[index] & 0xFF));
                    } else {
                        sblocks[index] = (short) (((addId[index >> 1] & 0xF0) << 4) + (blocks[index] & 0xFF));
                    }
                }
            }

            stream.close();
            return new SchematicLoader(name, sblocks, data, width, length, height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void paste(Location location) {
        World world = Bukkit.getWorld("world");
        for(int x = 0; x < width; ++x){
            for(int y = 0; y < height; ++y){
                for(int z = 0; z < length; ++z){
                    int index = y * width * length + z * width + x;
                    Material material = Material.getMaterial(blocks[index]);
                    if(material == null) {
                        System.out.println("null material " + blocks[index]);
                    } else {
                        Location pastelocation = new Location(world, x,y,z).add(location);
                        world.getBlockAt(pastelocation).setType(material);
                    }
                }
            }
        }
    }
}
