package me.dinnerbeef.compressium;

import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.List;

public enum DefaultCompressiumBlocks {
    COBBLESTONE("cobblestone", "cobblestone", "block/cobblestone", "block/cobblestone", false),
    STONE("stone", "stone", "block/stone", "block/stone", false),
    SAND("sand", "sand", "block/sand", "block/sand", CompressibleType.FALLING, false),
    GRAVEL("gravel", "gravel", "block/gravel", "block/gravel",  CompressibleType.FALLING, false),
    NETHERRACK("netherrack", "netherrack", "block/netherrack", "block/netherrack", CompressibleType.NETHER_RACK, false),
    SNOW("snow", "snow_block", "block/snow", "block/snow_block", true),
    SOULSAND("soulsand", "soul_sand", "block/soul_sand", "block/soul_sand", CompressibleType.SOUL_SAND, false),
    IRON("iron", "iron_block", "block/iron_block", "block/iron_block"),
    GOLD("gold", "gold_block", "block/gold_block", "block/gold_block"),
    DIAMOND("diamond", "diamond_block", "block/diamond_block", "block/diamond_block"),
    EMERALD("emerald", "emerald_block", "block/emerald_block", "block/emerald_block"),
    CLAY("clay", "clay", "block/clay", "block/clay", false),
    NETHERITE("netherite", "netherite_block", "block/netherite_block", "block/netherite_block"),
    DIRT("dirt", "dirt", "block/dirt", "block/dirt"),
    COAL("coal", "coal_block", "block/coal_block", "block/coal_block"),
    REDSAND("redsand", "red_sand", "block/red_sand", "block/red_sand", CompressibleType.FALLING, true),
    ENDSTONE("endstone", "end_stone", "block/end_stone", "block/end_stone"),
    OBSIDIAN("obsidian", "obsidian", "block/obsidian", "block/obsidian"),
    LAPIS("lapis", "lapis_block", "block/lapis_block", "block/lapis_block"),
    QUARTZ("quartz", "quartz_block", "block/quartz_block_side", "block/quartz_block"),
    REDSTONE("redstone", "redstone_block", "block/redstone_block", "block/redstone_block", CompressibleType.POWERED, true),
    ANDESITE("andesite", "andesite", "block/andesite", "block/andesite"),
    DIORITE("diorite", "diorite", "block/diorite", "block/diorite"),
    COPPER("copper", "copper_block", "block/copper_block", "block/copper_block"),
    GRANITE("granite", "granite", "block/granite", "block/granite");

    public static final List<DefaultCompressiumBlocks> VALUES = Arrays.asList(values());

    CompressibleBlock block;

    DefaultCompressiumBlocks(String n, String baseResourceLocation, String particlePath, String baseBlockModel, boolean isBlockOf) {
        this(n, baseResourceLocation, particlePath, baseBlockModel, CompressibleType.BLOCK, isBlockOf);
    }

    DefaultCompressiumBlocks(String n, String baseResourceLocation, String particlePath, String baseBlockModel) {
        this(n, baseResourceLocation, particlePath, baseBlockModel, CompressibleType.BLOCK, true);
    }

    DefaultCompressiumBlocks(String n, String baseResourceLocation, String particlePath, String baseBlockModel, CompressibleType type, boolean isBlockOf) {
        this.block = new CompressibleBlock(n, new ResourceLocation("minecraft", baseResourceLocation), new ResourceLocation("minecraft", particlePath), new ResourceLocation("minecraft", baseBlockModel), type, 9, isBlockOf);
    }
}
