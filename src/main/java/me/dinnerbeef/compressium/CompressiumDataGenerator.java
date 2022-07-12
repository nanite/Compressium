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
            generator.addProvider(true, new CompressiumRecipeProvider(generator));
            generator.addProvider(true,new CompressiumLootTableProvider(generator));
        }
        if (event.includeClient()) {
            generator.addProvider(true, new CompressiumBlockStateProvider(generator, event.getExistingFileHelper()));
            generator.addProvider(true, new CompressiumItemModelProvider(generator, event.getExistingFileHelper()));
            //this only generates the english language portion. other languages need their own provider
            generator.addProvider(true, new CompressiumLanguageProvider(generator, "en_us"));
            CompressiumBlockTagProvider compressiumBlockTagProvider = new CompressiumBlockTagProvider(generator, event.getExistingFileHelper());
            generator.addProvider(true, compressiumBlockTagProvider);
            generator.addProvider(true, new CompressiumItemTagProvider(generator, compressiumBlockTagProvider,event.getExistingFileHelper()));
        }
    }
}

