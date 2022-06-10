package me.dinnerbeef.compressium;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;

@Mod(Compressium.MODID)
public class Compressium {
    public static final String MODID = "compressium";

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final CreativeModeTab creativeTab = new CreativeModeTab(Compressium.MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("compressium:cobblestone_1")));
        }
    };

    public static final HashMap<CompressibleBlock, List<Supplier<Block>>> REGISTERED_BLOCKS = new HashMap<>();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.Keys.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.Keys.ITEMS, MODID);

    public Compressium() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(eventBus);
        BLOCKS.register(eventBus);
        eventBus.addListener(this::clientSetup);

        loadBlocksFromConfig();
    }

    private void loadBlocksFromConfig() {
        // Attempt to load the blocks from the json file or create the json file for future launches and to allow users
        // to add their own blocks as long as they provide their own data files as a data pack
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
                Supplier<Block> blockSupplier = BLOCKS.register(name, () -> block.type().getConstructor().get());
                ITEMS.register(name, () -> new BlockItem(blockSupplier.get(), new Item.Properties().tab(creativeTab)));
                registeredBlocks.add(blockSupplier);
            }
            REGISTERED_BLOCKS.put(block, registeredBlocks);
        }
    }

    private void clientSetup(FMLClientSetupEvent event) {
        CompressiumClient.setupItemVar();
    }

}
