package me.dinnerbeef.compressium;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mod(Compressium.MODID)
public class Compressium {
    public static final String MODID = "compressium";

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Using a map here allows us to avoid duplicates, we could use a set but meh, the equals is a little too
     * allowing to properly invalidate duplicate name blocks
     */
    private static final HashMap<String, CompressibleBlock> COMPRESSIUM_BLOCKS = new HashMap<>();
    public static final HashMap<CompressibleBlock, List<Supplier<Block>>> REGISTERED_BLOCKS = new HashMap<>();

    public static final CreativeModeTab creativeTab = new CreativeModeTab(Compressium.MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("compressium:cobblestone_1")));
        }
    };

    public Compressium() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addGenericListener(Block.class, this::registerBlocks);
        eventBus.addGenericListener(Item.class, this::registerItems);
        eventBus.addListener(this::clientSetup);

        loadBlocksFromConfig();
    }

    private void loadBlocksFromConfig() {
        // Attempt to load the blocks from the json file or create the json file for future launches and to allow users
        // to add their own blocks as long as they provide their own data files as a data pack
        LOGGER.info("Loading compressible blocks from data store config/compressiumblocks.json");

        Path config = FMLPaths.CONFIGDIR.get().resolve("compressiumblocks.json");
        if (Files.exists(config)) {
            try {
                CompressibleBlock[] compressableBlocks = new Gson().fromJson(Files.readString(config), CompressibleBlock[].class);
                for (CompressibleBlock compressableBlock : compressableBlocks) {
                    COMPRESSIUM_BLOCKS.put(compressableBlock.name(), compressableBlock);
                }

                List<String> foundBlocks = COMPRESSIUM_BLOCKS.values().stream().map(e -> e.name().toLowerCase()).toList();
                List<DefaultCompressiumBlocks> missingDefaultBlocks = DefaultCompressiumBlocks.VALUES.stream().filter(e -> !foundBlocks.contains(e.name().toLowerCase())).toList();

                if (missingDefaultBlocks.size() > 0) {
                    LOGGER.warn("Found a missing block from the default compressible blocks, adding it back.");
                    LOGGER.warn("We do not support dynamically removing default blocks to prevent basic registry issues.");
                    Map<String, CompressibleBlock> defaultBlocks = missingDefaultBlocks.stream().map(e -> e.block)
                            .collect(Collectors.toMap(CompressibleBlock::name, Function.identity()));

                    COMPRESSIUM_BLOCKS.putAll(defaultBlocks);

                    Files.writeString(config, new GsonBuilder().setPrettyPrinting().create().toJson(COMPRESSIUM_BLOCKS));
                }
            } catch (IOException e) {
                LOGGER.error("Unable to read json file for compressible blocks data!");
                throw new RuntimeException(e);
            }
        } else {
            LOGGER.info("Compressible blocks json not found. Creating a new one!");
            try {
                List<CompressibleBlock> defaultBlocks = Arrays.stream(DefaultCompressiumBlocks.values()).map(e -> e.block).toList();
                COMPRESSIUM_BLOCKS.putAll(Arrays.stream(DefaultCompressiumBlocks.values()).map(e -> e.block).collect(Collectors.toMap(CompressibleBlock::name, Function.identity())));
                Files.writeString(config, new GsonBuilder().setPrettyPrinting().create().toJson(defaultBlocks));
            } catch (IOException e) {
                LOGGER.error("Unable to write json file for compressible blocks data!");
                throw new RuntimeException(e);
            }
        }
    }

    private void clientSetup(FMLClientSetupEvent event) {
        CompressiumClient.setupItemVar();
    }

    private void registerBlocks(RegistryEvent.Register<Block> event) {
        for (CompressibleBlock block : COMPRESSIUM_BLOCKS.values()) {
            var registeredBlocks = new ArrayList<Supplier<Block>>();
            for (int i = 0; i < block.getNestedDepth(); i++) {
                Block blockToRegister = block.type().getConstructor().get();
                blockToRegister.setRegistryName(block.name().toLowerCase() + "_" + (i + 1));

                event.getRegistry().register(blockToRegister);
                registeredBlocks.add(() -> blockToRegister);
            }
            REGISTERED_BLOCKS.put(block, registeredBlocks);
        }
    }

    private void registerItems(RegistryEvent.Register<Item> event) {
        REGISTERED_BLOCKS.values().forEach(a -> a.forEach(e -> event.getRegistry().register(new BlockItem(e.get(), new Item.Properties().tab(creativeTab)).setRegistryName(e.get().getRegistryName()))));
    }
}
