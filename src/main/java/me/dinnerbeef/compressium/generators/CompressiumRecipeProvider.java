package me.dinnerbeef.compressium.generators;

import me.dinnerbeef.compressium.Compressium;
import me.dinnerbeef.compressium.DefaultCompressiumBlocks;
import net.minecraft.data.*;
import net.minecraft.data.recipes.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

public class CompressiumRecipeProvider extends RecipeProvider {
    public CompressiumRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        Compressium.REGISTERED_BLOCKS.forEach((k, v) -> {
            var name = k.name().toLowerCase();

            Block baseBlock = ForgeRegistries.BLOCKS.getValue(k.baseResourceLocation());
            ShapelessRecipeBuilder // Uses Minecraft Blocks Here
                    .shapeless(baseBlock, 9)
                    .requires(v.get(0).get())
                    .unlockedBy("has_compressed_" + name + "_x1", has(v.get(0).get()))
                    .save(consumer, Compressium.MODID + ":" + name + "_" + 1 + "_uncraft");
            ShapedRecipeBuilder
                    .shaped(v.get(0).get())
                    .define('#', baseBlock)
                    .pattern("###").pattern("###").pattern("###")
                    .unlockedBy("has_" + name, has(baseBlock))
                    .save(consumer);

            for (int i = 0; i < v.size() - 1; i ++) {
                int index = i + 1;
                ShapelessRecipeBuilder
                        .shapeless(v.get(index - 1).get(), 9)
                        .requires(v.get(index).get())
                        .unlockedBy("has_compressed_" + name + "_x" + (index + 1), has(v.get(index).get()))
                        .save(consumer, Compressium.MODID + ":" + name + "_" + (index + 1) + "_uncraft");
                ShapedRecipeBuilder
                        .shaped(v.get(index).get())
                        .define('#', v.get(index - 1).get())
                        .pattern("###").pattern("###").pattern("###")
                        .unlockedBy("has_compressed_" + name + "_x" + index, has(v.get(index - 1).get()))
                        .save(consumer);
            };
        });
    }
}
