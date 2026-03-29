package me.dinnerbeef.compressium.generators;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.dinnerbeef.compressium.Compressium;
import me.dinnerbeef.compressium.CompressibleBlock;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CompressiumModelDataProvider implements DataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final PackOutput output;

    public CompressiumModelDataProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        Path resourcesDir = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK);

        for (var entry : Compressium.REGISTERED_BLOCKS.entrySet()) {
            CompressibleBlock block = entry.getKey();
            var suppliers = entry.getValue();

            for (int i = 0; i < suppliers.size(); i++) {
                String name = block.name().toLowerCase() + "_" + (i + 1);
                int depth = i + 1;

                String baseModel = block.baseBlockModel().toString();
                String particleTexture = block.particlePath().toString();

                // --- blockstate ---
                JsonObject variant = new JsonObject();
                variant.addProperty("model", "compressium:block/" + name);

                JsonObject variants = new JsonObject();
                variants.add("", variant);

                JsonObject blockstate = new JsonObject();
                blockstate.add("variants", variants);

                futures.add(save(cache, blockstate,
                        resourcesDir.resolve("compressium/blockstates/" + name + ".json")));

                // --- block model (neoforge:composite) ---
                JsonObject blockModel = new JsonObject();
                blockModel.addProperty("parent", "minecraft:block/block");
                blockModel.addProperty("loader", "neoforge:composite");

                // particle texture
                JsonObject textures = new JsonObject();
                textures.addProperty("particle", particleTexture);
                blockModel.add("textures", textures);

                // --- children ---
                JsonObject children = new JsonObject();

                // overlay child
                JsonObject overlayTextures = new JsonObject();
                overlayTextures.addProperty("all", "compressium:block/layer_" + depth);

                JsonObject overlay = new JsonObject();
                overlay.addProperty("parent", "minecraft:block/cube_all");
                overlay.add("textures", overlayTextures);

                children.add("overlay", overlay);

                // base block child
                JsonObject base = new JsonObject();
                base.addProperty("parent", baseModel);

                children.add("block", base);

                blockModel.add("children", children);

                // render order (important!)
                blockModel.add("item_render_order", GSON.toJsonTree(new String[]{
                        "overlay",
                        "block"
                }));

                futures.add(save(cache, blockModel,
                        resourcesDir.resolve("compressium/models/block/" + name + ".json")));

                // --- item model (MC 26.1 new items/ format) ---
                JsonObject itemModelInner = new JsonObject();
                itemModelInner.addProperty("type", "minecraft:model");
                itemModelInner.addProperty("model", "compressium:block/" + name);

                JsonObject itemModel = new JsonObject();
                itemModel.add("model", itemModelInner);

                futures.add(save(cache, itemModel,
                        resourcesDir.resolve("compressium/items/" + name + ".json")));
            }
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private static CompletableFuture<?> save(CachedOutput cache, JsonElement json, Path path) {
        return DataProvider.saveStable(cache, json, path);
    }

    @Override
    public String getName() {
        return "Compressium Models";
    }
}
