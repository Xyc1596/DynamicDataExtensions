package com.xyc.dynamicdataext.lang;

import com.xyc.dynamicdataext.DynamicDataMain;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = DynamicDataMain.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class DynamicLangRegistry {
    private static final Map<String, Map<String, String>> localeToTranslations = new HashMap<>();
    private static final Set<String> allAddedKeys = new HashSet<>();

    static void register(AbstractTranslatableLang lang) {
        String key = lang.getKey();
        if (!allAddedKeys.add(key))
            throw new IllegalArgumentException("Duplicate lang key: " + key);
        for (Map.Entry<String, String> entry : lang.getTranslations().entrySet())
            localeToTranslations.computeIfAbsent(entry.getKey(), k -> new HashMap<>())
                                .put(key, entry.getValue());
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        boolean run = event.includeClient();
        for (Map.Entry<String, Map<String, String>> localeAndTranslations : localeToTranslations.entrySet()) {
            generator.addProvider(
                run,
                new LanguageProvider(output, DynamicDataMain.MOD_ID, localeAndTranslations.getKey()) {
                    @Override
                    protected void addTranslations() {
                        for (Map.Entry<String, String> keyAndTranslation :
                            localeAndTranslations.getValue().entrySet()) {
                            this.add(keyAndTranslation.getKey(), keyAndTranslation.getValue());
                        }
                    }
                }
            );
        }
    }
}
