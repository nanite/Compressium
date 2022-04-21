package me.dinnerbeef.compressium.generators;

import me.dinnerbeef.compressium.Compressium;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraft.tags.BlockTags;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class CompressiumBlockTagProvider extends BlockTagsProvider {

    public CompressiumBlockTagProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Compressium.MODID, exFileHelper);
    }

    @Override
    protected void addTags() {
        // I mean it works right?
        enum RequiresWood {
            ANDESITE, COAL, CLAY, COBBLESTONE, DIORITE, DIRT, ENDSTONE, GRANITE, GRAVEL, REDSTONE, NETHERRACK, SAND, SNOW, SOULSAND, STONE, QUARTZ,
        }
        enum RequiresStone {
            COPPER, IRON, LAPIS,
        }
        enum RequiresIron {
            DIAMOND, EMERALD, GOLD,
        }
        enum RequiresDiamond {
            OBSIDIAN, NETHERITE
        }
        enum RequiresPick {
            ANDESITE, COAL, COBBLESTONE, COPPER, DIAMOND, DIORITE, EMERALD, ENDSTONE, GOLD, GRANITE, IRON, LAPIS, NETHERITE, NETHERRACK, OBSIDIAN, QUARTZ, REDSTONE, STONE
        }
        enum RequiresShovel {
            CLAY, DIORITE, GRAVEL, SAND, SNOW, SOULSAND
        }
        enum BeaconBase {
            IRON, GOLD, DIAMOND, EMERALD, NETHERITE, COPPER
        }

        addTagFromList(BeaconBase.values(), BlockTags.BEACON_BASE_BLOCKS);
        addTagFromList(RequiresWood.values(), Tags.Blocks.NEEDS_WOOD_TOOL);
        addTagFromList(RequiresStone.values(), BlockTags.NEEDS_STONE_TOOL);
        addTagFromList(RequiresIron.values(), BlockTags.NEEDS_IRON_TOOL);
        addTagFromList(RequiresDiamond.values(), BlockTags.NEEDS_DIAMOND_TOOL);
        addTagFromList(RequiresPick.values(), BlockTags.MINEABLE_WITH_PICKAXE);
        addTagFromList(RequiresShovel.values(), BlockTags.MINEABLE_WITH_SHOVEL);
    }

    private <T extends Enum<?>> void addTagFromList(T[] values, TagKey<Block> tag) {
        for (T value : values) {
            // Find the type from the registered blocks based on the name of the compressed block type
            List<Supplier<Block>> blockList = Compressium.REGISTERED_BLOCKS.entrySet().stream().filter(e -> e.getKey().name().equalsIgnoreCase(value.name())).findFirst().map(Map.Entry::getValue).orElse(List.of());
            blockList.forEach(e -> tag(tag).add(e.get()));
        }
    }
}
