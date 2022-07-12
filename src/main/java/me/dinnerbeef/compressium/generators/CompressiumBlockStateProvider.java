package me.dinnerbeef.compressium.generators;

import me.dinnerbeef.compressium.Compressium;
import me.dinnerbeef.compressium.DefaultCompressiumBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.CompositeModel;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.loaders.CompositeModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class CompressiumBlockStateProvider extends BlockStateProvider {

    public CompressiumBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Compressium.MODID, exFileHelper);
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
