package me.dinnerbeef.compressium.generators;

import me.dinnerbeef.compressium.Compressium;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CompressiumItemTagProvider extends ItemTagsProvider {

    public CompressiumItemTagProvider(DataGenerator gen, BlockTagsProvider provider, ExistingFileHelper exFileHelper) {
        super(gen, provider ,Compressium.MODID, exFileHelper);
    }

    @Override
    protected void addTags() {
    }
}

