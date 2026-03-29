package me.dinnerbeef.compressium;

import com.google.gson.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public enum CompressibleType {
    BLOCK("block", props -> new Block(props.strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE))),
    METAL("block", props -> new Block(props.strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.METAL))),
    NETHER_METAL("block", props -> new Block(props.strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK))),
    DIRT("block", props -> new FallingDamageBlock(props.strength(1.5f).sound(SoundType.GRAVEL))),
    CLAY("block", props -> new FallingDamageBlock(props.strength(1.5f).sound(SoundType.GRAVEL))),
    COPPER("block", props -> new FallingDamageBlock(props.strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.COPPER))),
    SAND("falling", props -> new FallingDamageBlock(props.strength(1.5f).sound(SoundType.SAND))),
    GRAVEL("falling", props -> new FallingDamageBlock(props.strength(1.5f).sound(SoundType.GRAVEL))),
    NETHER_RACK("nether_rack", props -> new NetherrackBlock(props.strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.NETHERRACK))),
    SOUL_SAND("soul_sand", props -> new SoulSandBlock(props.strength(1.5f).sound(SoundType.SOUL_SAND))),
    POWERED("powered", props -> new PoweredBlock(props.strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    private final String blockType;
    private final Function<BlockBehaviour.Properties, Block> constructor;

    private static final Logger LOGGER = LoggerFactory.getLogger(CompressibleType.class);
    public static final List<CompressibleType> VALUES = Arrays.asList(values());

    CompressibleType(String block, Function<BlockBehaviour.Properties, Block> constructor) {
        this.blockType = block;
        this.constructor = constructor;
    }

    public String getBlockType() {
        return blockType;
    }

    public Function<BlockBehaviour.Properties, Block> getConstructor() {
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
