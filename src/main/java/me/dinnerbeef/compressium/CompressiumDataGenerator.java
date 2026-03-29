package me.dinnerbeef.compressium;

import me.dinnerbeef.compressium.generators.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class CompressiumDataGenerator {

    @SubscribeEvent
    public static void dataClient(GatherDataEvent.Client event) {
        final DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new CompressiumLanguageProvider(packOutput, "en_us"));
        generator.addProvider(true, new CompressiumModelDataProvider(packOutput));
        generator.addProvider(true, new CompressiumRecipeProvider(packOutput, lookupProvider));
        generator.addProvider(true, new CompressiumLootTableProvider(packOutput, lookupProvider));
        generator.addProvider(true, new CompressiumBlockTagProvider(packOutput, lookupProvider));
    }
}
