package me.dinnerbeef.compressium.generators;

import me.dinnerbeef.compressium.Compressium;
import me.dinnerbeef.compressium.CompressiumType;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CompressiumItemModelProvider extends ItemModelProvider {
    public CompressiumItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Compressium.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (CompressiumType type : CompressiumType.VALUES) {
            for (int i = 1; i <= 9; i++) {
                withExistingParent(Compressium.MODID+":"+type.name+"_"+i, new ResourceLocation(Compressium.MODID, "block/" + type.name+"_"+i));
            }
        }
    }
}

