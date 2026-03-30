package me.dinnerbeef.compressium;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;

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

}
