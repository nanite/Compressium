package me.dinnerbeef.compressium;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
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
    private static final Logger LOGGER = LogUtils.getLogger();

    public Compressium(IEventBus modEventBus, ModContainer modContainer) {
        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);
        COMPRESSIUM_TAB.register(modEventBus);
        modEventBus.addListener(CompressiumDataGenerator::dataClient);

        loadBlocksFromConfig();
    }

    private void loadBlocksFromConfig() {
        LOGGER.info("Loading compressible blocks from data store config/compressiumblocks.json");

        Path config = FMLPaths.CONFIGDIR.get().resolve("compressiumblocks.json");

        List<CompressibleBlock> compressedBlocks = new ArrayList<>();
        if (Files.exists(config)) {
            try {
                CompressibleBlock[] compressableBlocks = new Gson().fromJson(Files.readString(config), CompressibleBlock[].class);
                compressedBlocks.addAll(Arrays.asList(compressableBlocks));

                List<String> foundBlocks = compressedBlocks.stream().map(e -> e.name().toLowerCase()).toList();
                List<DefaultCompressiumBlocks> missingDefaultBlocks = DefaultCompressiumBlocks.VALUES.stream().filter(e -> !foundBlocks.contains(e.name().toLowerCase())).toList();

                if (missingDefaultBlocks.size() > 0) {
                    LOGGER.warn("Found a missing block from the default compressible blocks, adding it back.");
                    LOGGER.warn("We do not support dynamically removing default blocks to prevent basic registry issues.");
                    compressedBlocks.addAll(missingDefaultBlocks.stream().map(e -> e.block).toList());

                    Files.writeString(config, new GsonBuilder().setPrettyPrinting().create().toJson(compressedBlocks));
                }
            } catch (IOException e) {
                LOGGER.error("Unable to read json file for compressible blocks data!");
                throw new RuntimeException(e);
            }
        } else {
            LOGGER.info("Compressible blocks json not found. Creating a new one!");
            try {
                List<CompressibleBlock> defaultBlocks = Arrays.stream(DefaultCompressiumBlocks.values()).map(e -> e.block).toList();
                compressedBlocks.addAll(defaultBlocks);
                Files.writeString(config, new GsonBuilder().setPrettyPrinting().create().toJson(defaultBlocks));
            } catch (IOException e) {
                LOGGER.error("Unable to write json file for compressible blocks data!");
                throw new RuntimeException(e);
            }
        }

        for (CompressibleBlock block : compressedBlocks) {
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
