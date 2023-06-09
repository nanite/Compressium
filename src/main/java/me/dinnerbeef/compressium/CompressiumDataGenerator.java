package me.dinnerbeef.compressium;

import me.dinnerbeef.compressium.generators.*;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Compressium.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CompressiumDataGenerator {

    @SubscribeEvent
    public static void data(GatherDataEvent event) {
        final DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            generator.addProvider(true, new CompressiumRecipeProvider(generator.getPackOutput()));
            generator.addProvider(true, new CompressiumLootTableProvider(generator.getPackOutput()));
        }
        if (event.includeClient()) {
            generator.addProvider(true, new CompressiumBlockStateProvider(generator.getPackOutput(), event.getExistingFileHelper()));
            generator.addProvider(true, new CompressiumItemModelProvider(generator.getPackOutput(), event.getExistingFileHelper()));
            //this only generates the english language portion. other languages need their own provider
            generator.addProvider(true, new CompressiumLanguageProvider(generator.getPackOutput(), "en_us"));
            CompressiumBlockTagProvider compressiumBlockTagProvider = new CompressiumBlockTagProvider(generator.getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper());
            generator.addProvider(true, compressiumBlockTagProvider);
        }
    }
}

