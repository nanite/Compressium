package me.dinnerbeef.compressium;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

// Client-only mod class — only loaded on the client side.
@Mod(value = Compressium.MODID, dist = Dist.CLIENT)
public class CompressiumClient {
    public CompressiumClient(IEventBus modEventBus, ModContainer modContainer) {
        // Render types for composite models are handled via block properties in CompressibleType.
        // Add any other client-only setup here.
    }
}
