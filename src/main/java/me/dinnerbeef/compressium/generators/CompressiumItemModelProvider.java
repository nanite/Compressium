package me.dinnerbeef.compressium.generators;

import me.dinnerbeef.compressium.Compressium;
import me.dinnerbeef.compressium.DefaultCompressiumBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class CompressiumItemModelProvider extends ItemModelProvider {
    public CompressiumItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Compressium.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        Compressium.REGISTERED_BLOCKS.forEach((k, v) -> {
            for (int i = 0; i < v.size(); i ++) {
                var name = k.name().toLowerCase();
                withExistingParent(Compressium.MODID + ":" + name + "_" + (i + 1), new ResourceLocation(Compressium.MODID, "block/" + name + "_" + (i + 1)));
            }
        });
    }
}

