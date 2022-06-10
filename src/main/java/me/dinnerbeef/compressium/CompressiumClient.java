package me.dinnerbeef.compressium;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class CompressiumClient {
    public static void setupItemVar() {
        Compressium.BLOCKS.getEntries().forEach(entry -> ItemBlockRenderTypes.setRenderLayer(entry.get(), renderType -> renderType == RenderType.solid() || renderType == RenderType.translucent()));
    }
}
