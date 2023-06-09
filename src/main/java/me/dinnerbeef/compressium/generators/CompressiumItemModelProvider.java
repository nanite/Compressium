package me.dinnerbeef.compressium.generators;

import me.dinnerbeef.compressium.Compressium;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CompressiumItemModelProvider extends ItemModelProvider {
    public CompressiumItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Compressium.MODID, existingFileHelper);
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

