package br.com.logicmc.skywars.extra;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class VoidWorld extends WorldCreator {

    public VoidWorld(String name) {
        super(name);
    }

    @Override
    public ChunkGenerator generator() {
        return new EmptyChunkGenerator();
    }



    public static class EmptyChunkGenerator extends ChunkGenerator {

        @Override
        public byte[] generate(World world, Random random, int x, int z) {
            return new byte[32768];
        }


        @Override
        public Location getFixedSpawnLocation(World world, Random random) {
            return new Location(world, 0, 100, 0 );
        }

        @Override
        public List<BlockPopulator> getDefaultPopulators(World world) {
            return Arrays.asList(new BlockPopulator[0]);
        }

        @Override
        public boolean canSpawn(World world, int x, int z) {
            return true;
        }

    }
}
