package me.dinnerbeef.compressium.generators;

import me.dinnerbeef.compressium.Compressium;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CompressiumLanguageProvider extends LanguageProvider {
    public CompressiumLanguageProvider(PackOutput output, String locale) {
        super(output, Compressium.MODID, locale);
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
