package me.dinnerbeef.compressium.generators;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import me.dinnerbeef.compressium.Compressium;
import me.dinnerbeef.compressium.DefaultCompressiumBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class CompressiumLanguageProvider extends LanguageProvider {
    public CompressiumLanguageProvider(DataGenerator gen, String locale) {
        super(gen, Compressium.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.compressium", "Compressium");

        Compressium.REGISTERED_BLOCKS.forEach((k, v) -> {
            for (int i = 0; i < v.size(); i ++) {
                add(v.get(i).get(), "Compressed " + (k.isBlockOf() ? "Block of " : "") + titleCase(k.name()) + " ("+(i+1)+"x)");
            }
        });
    }

    private static String titleCase(String input) {
        return Arrays.stream(input.toLowerCase().split(" ")).map(e -> e.substring(0, 1).toUpperCase() + e.substring(1)).collect(Collectors.joining(" "));
    }
}
