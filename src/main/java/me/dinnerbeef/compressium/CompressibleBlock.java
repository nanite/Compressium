package me.dinnerbeef.compressium;

import net.minecraft.resources.Identifier;

import java.util.Objects;

public final class CompressibleBlock {
    private final String name;
    private final int nestedDepth;
    private final boolean isBlockOf;

    private final Identifier baseResourceLocation;
    private final Identifier particlePath;
    private final Identifier baseBlockModel;
    private final CompressibleType type;

    public CompressibleBlock(
            String name,
            Identifier baseResourceLocation,
            Identifier particlePath,
            Identifier baseBlockModel,
            CompressibleType type,
            int nestedDepth,
            boolean isBlockOf) {
        this.name = name;
        this.baseResourceLocation = baseResourceLocation;
        this.particlePath = particlePath;
        this.baseBlockModel = baseBlockModel;
        this.type = type;
        this.nestedDepth = nestedDepth;
        this.isBlockOf = isBlockOf;
    }

    public String name() {
        return name;
    }

    public Identifier baseResourceLocation() {
        return baseResourceLocation;
    }

    public Identifier particlePath() {
        return particlePath;
    }

    public Identifier baseBlockModel() {
        return baseBlockModel;
    }

    public CompressibleType type() {
        return type;
    }

    public int getNestedDepth() {
        if (this.nestedDepth == 0) {
            return 9;
        }
        return nestedDepth;
    }

    public boolean isBlockOf() {
        return isBlockOf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompressibleBlock block = (CompressibleBlock) o;
        return nestedDepth == block.nestedDepth && isBlockOf == block.isBlockOf && Objects.equals(name, block.name) && Objects.equals(baseResourceLocation, block.baseResourceLocation) && Objects.equals(particlePath, block.particlePath) && Objects.equals(baseBlockModel, block.baseBlockModel) && type == block.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, nestedDepth, isBlockOf, baseResourceLocation, particlePath, baseBlockModel, type);
    }

}
