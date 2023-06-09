package me.dinnerbeef.compressium.generators;

import com.google.common.collect.ImmutableList;
import me.dinnerbeef.compressium.Compressium;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CompressiumLootTableProvider extends LootTableProvider {
    public CompressiumLootTableProvider(PackOutput output) {
        super(output, Set.of(), ImmutableList.of(
                new SubProviderEntry(CompressiumBlockLoot::new, LootContextParamSets.BLOCK)
        ));
    }

    private static class CompressiumBlockLoot extends BlockLootSubProvider {

        protected  CompressiumBlockLoot() {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            Compressium.REGISTERED_BLOCKS.forEach((k, v) -> v.forEach(e -> this.dropSelf(e.get())));
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            List<Block> blocks = new ArrayList<>();
            Compressium.REGISTERED_BLOCKS.forEach((k, v) -> v.forEach(e -> blocks.add(e.get())));
            return blocks;
        }

    }
}

