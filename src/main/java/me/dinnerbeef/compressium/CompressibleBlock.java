package me.dinnerbeef.compressium;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import net.minecraft.resources.Identifier;

import java.lang.reflect.Type;
import java.util.Objects;

public final class CompressibleBlock {
    private final String name;
    private final int nestedDepth;
    private final boolean isBlockOf;

    @JsonAdapter(IdentifierAdapter.class)
    private final Identifier baseResourceLocation;
    @JsonAdapter(IdentifierAdapter.class)
    private final Identifier particlePath;
    @JsonAdapter(IdentifierAdapter.class)
    private final Identifier baseBlockModel;

    @JsonAdapter(CompressibleType.Serializer.class)
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

    /** GSON adapter for {@link Identifier} (replaces the old ResourceLocation.Serializer). */
    public static class IdentifierAdapter implements JsonSerializer<Identifier>, JsonDeserializer<Identifier> {
        @Override
        public JsonElement serialize(Identifier src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }

        @Override
        public Identifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Identifier.parse(json.getAsString());
        }
    }
}
