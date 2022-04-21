package me.dinnerbeef.compressium;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public class CompressiumClient {
    public static void setupItemVar() {
        Compressium.REGISTERED_BLOCKS.values().forEach(a -> a.forEach(e -> ItemBlockRenderTypes.setRenderLayer(e.get(), renderType -> renderType == RenderType.solid() || renderType == RenderType.translucent())));
    }
}
