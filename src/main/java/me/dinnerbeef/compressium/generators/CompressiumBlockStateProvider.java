package me.dinnerbeef.compressium.generators;

import me.dinnerbeef.compressium.Compressium;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.loaders.CompositeModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CompressiumBlockStateProvider extends BlockStateProvider {

    public CompressiumBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Compressium.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        Compressium.REGISTERED_BLOCKS.forEach((k, v) -> {
            for (int i = 0; i < v.size(); i ++) {
                var block = v.get(i);
                simpleBlock(block.get(), models().getBuilder(Compressium.MODID + ":" + k.name().toLowerCase() + "_" + (i + 1))
                        .parent(this.models().getExistingFile(mcLoc("block/block")))
                        .texture("particle", k.particlePath())
                        .customLoader(CompositeModelBuilder::begin)
                        .child("Solid",
                                this.models().nested().parent(this.models().getExistingFile(k.baseBlockModel())))
                        .child("Translucent",
                                this.models().nested().parent(this.models().getExistingFile(mcLoc("block/cube_all")))
                                        .texture("all", new ResourceLocation(Compressium.MODID, "block/layer_" + (i + 1))))
                        .end());
            }
        });
    }
}
