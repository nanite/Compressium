package me.dinnerbeef.compressium;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public class CompressiumClient extends CompressiumCommon {
    public static void setupItemVar() {
        for (CompressiumType type : CompressiumType.VALUES) {
            for (Block block : type.blocks) {
                ItemBlockRenderTypes.setRenderLayer(block, renderType -> renderType == RenderType.solid() || renderType == RenderType.translucent());
            }
        }
    }
}