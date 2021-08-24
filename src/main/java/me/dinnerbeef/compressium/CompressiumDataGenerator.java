package me.dinnerbeef.compressium;

import me.dinnerbeef.compressium.generators.*;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Compressium.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CompressiumDataGenerator {

    @SubscribeEvent
    public static void data(GatherDataEvent event) {
        final DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            generator.addProvider(new CompressiumRecipeProvider(generator));
            generator.addProvider(new CompressiumLootTableProvider(generator));
        }
        if (event.includeClient()) {
            generator.addProvider(new CompressiumBlockStateProvider(generator, event.getExistingFileHelper()));
            generator.addProvider(new CompressiumItemModelProvider(generator, event.getExistingFileHelper()));
            //this only generates the english language portion. other languages need their own provider
            generator.addProvider(new CompressiumLanguageProvider(generator, "en_us"));
        }
    }
}

