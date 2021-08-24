package me.dinnerbeef.compressium.generators;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import me.dinnerbeef.compressium.Compressium;
import me.dinnerbeef.compressium.CompressiumType;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import java.io.*;
import java.util.Map;

public class CompressiumLanguageProvider extends LanguageProvider {

    private final String locale;

    public CompressiumLanguageProvider(DataGenerator gen, String locale) {
        super(gen, Compressium.MODID, locale);
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        Map<String, Map<String,String>> config = null;
        try {
            Reader reader = new FileReader("config/languageValues.json");
            Gson gson = new Gson();
            JsonReader configFile = new JsonReader(reader);
            config = gson.fromJson(configFile, Map.class);
        } catch (FileNotFoundException e) {
        }
        if (config != null) {
            Map<String, String> localeMap = config.get(locale);
            add("itemGroup.compressium", localeMap.get("itemGroup"));
            for (CompressiumType type : CompressiumType.VALUES) {
                for (int i = 0; i < 9; i++) {
                    add(type.blocks.get(i), localeMap.get(type.name) + " ("+(i+1)+"X)");
                }
            }
        }
    }
}
