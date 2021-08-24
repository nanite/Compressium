package me.dinnerbeef.compressium.generators;

import me.dinnerbeef.compressium.Compressium;
import me.dinnerbeef.compressium.CompressiumType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.loaders.MultiLayerModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CompressiumBlockStateProvider extends BlockStateProvider {

    public CompressiumBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Compressium.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (CompressiumType type : CompressiumType.VALUES) {
            Block[] blocks = Compressium.BLOCKS.get(type.name);
            for (int i = 0; i < 9; i++) {
                simpleBlock(blocks[i], models().getBuilder(Compressium.MODID+":"+type.name+"_"+(i+1))
                    .parent(this.models().getExistingFile(mcLoc("block/block")))
                    .texture("particle", type.particlePath)
                    .customLoader(MultiLayerModelBuilder::begin)
                        .submodel(RenderType.solid(),
                                this.models().nested().parent(this.models().getExistingFile(type.baseBlockModel)))
                        .submodel(RenderType.translucent(),
                                this.models().nested().parent(this.models().getExistingFile(mcLoc("block/cube_all")))
                                        .texture("all", new ResourceLocation(Compressium.MODID, "block/layer_"+(i+1))))
                    .end());
            }
        }
    }
}
