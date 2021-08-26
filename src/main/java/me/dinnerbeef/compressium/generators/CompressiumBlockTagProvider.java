package me.dinnerbeef.compressium.generators;

import me.dinnerbeef.compressium.Compressium;
import me.dinnerbeef.compressium.CompressiumType;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraft.tags.BlockTags;

public class CompressiumBlockTagProvider extends BlockTagsProvider {

    public CompressiumBlockTagProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen,Compressium.MODID, exFileHelper);
    }

    @Override
    protected void addTags() {
        // I mean it works right?
        enum requiresWood{
            ANDESITE,
            COAL,
            CLAY,
            COBBLESTONE,
            DIORITE,
            DIRT,
            ENDSTONE,
            GRANITE,
            GRAVEL,
            REDSTONE,
            NETHERRACK,
            SAND,
            SNOW,
            SOULSAND,
            STONE,
            QUARTZ,
        }
        enum requiresStone{
            COPPER,
            IRON,
            LAPIS,
        }
        enum requiresIron{
            DIAMOND,
            EMERALD,
            GOLD,
        }
        enum requiresDiamond{
            OBSIDIAN,
            NETHERITE
        }
        enum requiresPick{
            ANDESITE,
            COAL,
            COBBLESTONE,
            COPPER,
            DIAMOND,
            DIORITE,
            EMERALD,
            ENDSTONE,
            GOLD,
            GRANITE,
            IRON,
            LAPIS,
            NETHERITE,
            NETHERRACK,
            OBSIDIAN,
            QUARTZ,
            REDSTONE,
            STONE
        }
        enum requiresShovel{
            CLAY,
            DIORITE,
            GRAVEL,
            SAND,
            SNOW,
            SOULSAND
        }
        enum beaconBase{
            IRON,
            GOLD,
            DIAMOND,
            EMERALD,
            NETHERITE,
            COPPER
        }

        for (beaconBase beaconBase : beaconBase.values()) {
            CompressiumType type = CompressiumType.valueOf(beaconBase.toString());
            Block[] blockList = Compressium.BLOCKS.get(type.name);
            for (int i = 1; i < 10; i++) {
                tag(BlockTags.BEACON_BASE_BLOCKS).add(blockList[i - 1]);
            }
        }
        for (requiresWood requiresWood : requiresWood.values()) {
            CompressiumType type = CompressiumType.valueOf(requiresWood.toString());
            Block[] blockList = Compressium.BLOCKS.get(type.name);
            for (int i = 1; i < 10; i++) {
                tag(Tags.Blocks.NEEDS_WOOD_TOOL).add(blockList[i - 1]);
            }
        }
        for (requiresStone requiresStone : requiresStone.values()) {
            CompressiumType type = CompressiumType.valueOf(requiresStone.toString());
            Block[] blockList = Compressium.BLOCKS.get(type.name);
            for (int i = 1; i < 10; i++) {
                tag(BlockTags.NEEDS_STONE_TOOL).add(blockList[i - 1]);
            }
        }
        for (requiresIron requiresIron : requiresIron.values()) {
            CompressiumType type = CompressiumType.valueOf(requiresIron.toString());
            Block[] blockList = Compressium.BLOCKS.get(type.name);
            for (int i = 1; i < 10; i++) {
                tag(BlockTags.NEEDS_IRON_TOOL).add(blockList[i - 1]);
            }
        }
        for (requiresDiamond requiresDiamond : requiresDiamond.values()) {
            CompressiumType type = CompressiumType.valueOf(requiresDiamond.toString());
            Block[] blockList = Compressium.BLOCKS.get(type.name);
            for (int i = 1; i < 10; i++) {
                tag(BlockTags.NEEDS_DIAMOND_TOOL).add(blockList[i - 1]);
            }
        }
        for (requiresPick requiresPick : requiresPick.values()) {
            CompressiumType type = CompressiumType.valueOf(requiresPick.toString());
            Block[] blockList = Compressium.BLOCKS.get(type.name);
            for (int i = 1; i < 10; i++) {
                tag(BlockTags.MINEABLE_WITH_PICKAXE).add(blockList[i - 1]);
            }
        }
        for (requiresShovel requiresShovel : requiresShovel.values()) {
            CompressiumType type = CompressiumType.valueOf(requiresShovel.toString());
            Block[] blockList = Compressium.BLOCKS.get(type.name);
            for (int i = 1; i < 10; i++) {
                tag(BlockTags.MINEABLE_WITH_SHOVEL).add(blockList[i - 1]);
            }
        }
    }
}
