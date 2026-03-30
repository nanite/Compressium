package me.dinnerbeef.compressium;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

@Mod(Compressium.MODID)
public class Compressium {
    public static final String MODID = "compressium";
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> COMPRESSIUM_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = COMPRESSIUM_TAB.register(MODID, () -> new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 1)
            .icon(() -> new ItemStack(Items.COBBLESTONE))
            .title(Component.translatable("itemGroup.compressium"))
            .displayItems((config, builder) -> ITEMS.getEntries().forEach(entry -> builder.accept(entry.get())))
            .build());
    public static final HashMap<CompressibleBlock, List<Supplier<Block>>> REGISTERED_BLOCKS = new HashMap<>();

    public Compressium(IEventBus modEventBus, ModContainer modContainer) {
        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);
        COMPRESSIUM_TAB.register(modEventBus);
        modEventBus.addListener(CompressiumDataGenerator::dataClient);

        registerBlocks();
    }

    private void registerBlocks() {
        for (CompressibleBlock block : DefaultCompressiumBlocks.VALUES.stream().map(e -> e.block).toList()) {
            var registeredBlocks = new ArrayList<Supplier<Block>>();
            for (int i = 0; i < block.getNestedDepth(); i++) {
                String name = block.name().toLowerCase() + "_" + (i + 1);
                DeferredBlock<Block> blockSupplier = BLOCKS.register(name,
                        id -> block.type().getConstructor().apply(
                                BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id))
                        ));
                ITEMS.register(name, id -> new BlockItem(blockSupplier.get(),
                        new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
                registeredBlocks.add(blockSupplier);
            }
            REGISTERED_BLOCKS.put(block, registeredBlocks);
        }
    }
}
