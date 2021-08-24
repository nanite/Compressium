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

public class CompressiumToolProvider extends BlockStateProvider {

    public CompressiumToolProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Compressium.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (CompressiumType type : CompressiumType.VALUES) {

            }
        }
    }

