package me.dinnerbeef.compressium.generators;

import com.google.common.collect.ImmutableList;
import me.dinnerbeef.compressium.Compressium;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class CompressiumLootTableProvider extends LootTableProvider {
    public CompressiumLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Set.of(), ImmutableList.of(
                new SubProviderEntry(CompressiumBlockLoot::new, LootContextParamSets.BLOCK)
        ), lookupProvider);
    }

    private static class CompressiumBlockLoot extends BlockLootSubProvider {

        protected CompressiumBlockLoot(HolderLookup.Provider lookupProvider) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
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
