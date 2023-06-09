package me.dinnerbeef.compressium;

import com.google.gson.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public enum CompressibleType {
    BLOCK("block", () -> new Block(BlockBehaviour.Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE))),
    METAL("block", () -> new Block(BlockBehaviour.Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.METAL))),
    NETHER_METAL("block", () -> new Block(BlockBehaviour.Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK))),
    DIRT("block", () -> new FallingDamageBlock(BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.GRAVEL))),
    CLAY("block", () -> new FallingDamageBlock(BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.GRAVEL))),
    COPPER("block", () -> new FallingDamageBlock(BlockBehaviour.Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.COPPER))),
    SAND("falling", () -> new FallingDamageBlock(BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.SAND))),
    GRAVEL("falling", () -> new FallingDamageBlock(BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.GRAVEL))),
    NETHER_RACK("nether_rack", () -> new NetherrackBlock(BlockBehaviour.Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.NETHERRACK))),
    SOUL_SAND("soul_sand", () -> new SoulSandBlock(BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.SOUL_SAND))),
    POWERED("powered", () -> new PoweredBlock(BlockBehaviour.Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    private String blockType;
    private Supplier<? extends Block> constructor;

    private static final Logger LOGGER = LogManager.getLogger();
    public static final List<CompressibleType> VALUES = Arrays.asList(values());

    CompressibleType(String block, Supplier<? extends Block> constructor) {
        this.blockType = block;
        this.constructor = constructor;
    }

    public String getBlockType() {
        return blockType;
    }

    public Supplier<? extends Block> getConstructor() {
        return constructor;
    }

    static Optional<CompressibleType> findFromName(String name) {
        return VALUES.stream().filter(e -> e.blockType.equals(name)).findFirst();
    }

    public static class Serializer implements JsonDeserializer<CompressibleType>, JsonSerializer<CompressibleType> {
        public CompressibleType deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            return findFromName(element.getAsString()).orElseGet(() -> {
                LOGGER.warn("Failed to find the correct compressable type from {}", element.getAsString());
                return BLOCK;
            });
        }

        public JsonElement serialize(CompressibleType type, Type p_135856_, JsonSerializationContext p_135857_) {
            return new JsonPrimitive(type.getBlockType());
        }
    }
}
