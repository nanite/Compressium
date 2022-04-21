package me.dinnerbeef.compressium;

import com.google.gson.annotations.JsonAdapter;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public final class CompressibleBlock {
    private final String name;
    private final int nestedDepth;
    private final boolean isBlockOf;

    @JsonAdapter(ResourceLocation.Serializer.class)
    private final ResourceLocation baseResourceLocation;
    @JsonAdapter(ResourceLocation.Serializer.class)
    private final ResourceLocation particlePath;
    @JsonAdapter(ResourceLocation.Serializer.class)
    private final ResourceLocation baseBlockModel;

    @JsonAdapter(CompressibleType.Serializer.class)
    private final CompressibleType type;

    public CompressibleBlock(
            String name,
            ResourceLocation baseResourceLocation,
            ResourceLocation particlePath,
            ResourceLocation baseBlockModel,
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

    public ResourceLocation baseResourceLocation() {
        return baseResourceLocation;
    }

    public ResourceLocation particlePath() {
        return particlePath;
    }

    public ResourceLocation baseBlockModel() {
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

    @Override
    public String toString() {
        return "CompressableBlock{" +
                "name='" + name + '\'' +
                ", nestedDepth=" + nestedDepth +
                ", isBlockOf=" + isBlockOf +
                ", baseResourceLocation=" + baseResourceLocation +
                ", particlePath=" + particlePath +
                ", baseBlockModel=" + baseBlockModel +
                ", type=" + type +
                '}';
    }
}
