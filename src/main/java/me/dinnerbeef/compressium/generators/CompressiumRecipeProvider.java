package me.dinnerbeef.compressium.generators;

import com.mojang.logging.LogUtils;
import me.dinnerbeef.compressium.Compressium;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;

public class CompressiumRecipeProvider extends RecipeProvider.Runner {

    private static final Logger LOGGER = LogUtils.getLogger();

    public CompressiumRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    public String getName() {
        return "Compressium Recipes";
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            protected void buildRecipes() {
                HolderGetter<Item> items = registries.lookupOrThrow(Registries.ITEM);
                Compressium.REGISTERED_BLOCKS.forEach((k, v) -> {
                    var name = k.name().toLowerCase();
                    Block baseBlock = BuiltInRegistries.BLOCK.get(k.baseResourceLocation())
                            .map(Holder.Reference::value)
                            .orElse(null);
                    if (baseBlock == null) {
                        LOGGER.warn("Could not find base block for {}, skipping recipe generation.", name);
                        return;
                    }

                    ShapelessRecipeBuilder
                            .shapeless(items, RecipeCategory.BUILDING_BLOCKS, baseBlock, 9)
                            .requires(v.get(0).get())
                            .unlockedBy("has_compressed_" + name + "_x1", has(v.get(0).get()))
                            .save(output, ResourceKey.create(Registries.RECIPE,
                                    Identifier.parse(Compressium.MODID + ":" + name + "/uncraft/" + name + "_1")));

                    ShapedRecipeBuilder
                            .shaped(items, RecipeCategory.BUILDING_BLOCKS, v.get(0).get())
                            .define('#', baseBlock)
                            .pattern("###").pattern("###").pattern("###")
                            .unlockedBy("has_" + name, has(baseBlock))
                            .save(output, ResourceKey.create(Registries.RECIPE,
                                    Identifier.parse(Compressium.MODID + ":" + name + "/compact/" + name + "_1")));

                    for (int i = 0; i < v.size() - 1; i++) {
                        int index = i + 1;
                        ShapelessRecipeBuilder
                                .shapeless(items, RecipeCategory.BUILDING_BLOCKS, v.get(index - 1).get(), 9)
                                .requires(v.get(index).get())
                                .unlockedBy("has_compressed_" + name + "_x" + (index + 1), has(v.get(index).get()))
                                .save(output, ResourceKey.create(Registries.RECIPE,
                                        Identifier.parse(Compressium.MODID + ":" + name + "/uncraft/" + name + "_" + (index + 1))));

                        ShapedRecipeBuilder
                                .shaped(items, RecipeCategory.BUILDING_BLOCKS, v.get(index).get())
                                .define('#', v.get(index - 1).get())
                                .pattern("###").pattern("###").pattern("###")
                                .unlockedBy("has_compressed_" + name + "_x" + index, has(v.get(index - 1).get()))
                                .save(output, ResourceKey.create(Registries.RECIPE,
                                        Identifier.parse(Compressium.MODID + ":" + name + "/compact/" + name + "_" + (index + 1))));
                    }
                });
            }
        };
    }
}
