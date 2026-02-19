package com.xyc.dynamicdataext;

import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.lang.TranslatableLang;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public class DynamicDataLanguageProvider {
    protected final String modId;
    protected final Map<String, Map<String, String>> allTranslations = new HashMap<>();
    protected final Map<String, Set<String>> defaultKeys = new HashMap<>();

    public DynamicDataLanguageProvider(String modId) {
        this.modId = modId;
    }

    public void addModuleLang(TranslatableLang lang) {
        for (Map.Entry<String, Map<String, String>> localeAndTranslations : lang.getAllTranslations().entrySet()) {
            String locale = localeAndTranslations.getKey();
            this.allTranslations.putIfAbsent(locale, new LinkedHashMap<>());
            this.defaultKeys.putIfAbsent(locale, new HashSet<>());
            Map<String, String> currentAdded = this.allTranslations.get(locale);
            Set<String> currentDefaultKeys = this.defaultKeys.get(locale);
            for (Map.Entry<String, String> keyAndTranslation : localeAndTranslations.getValue().entrySet()) {
                String key = keyAndTranslation.getKey();
                if (key.contains(".default.")) {
                    if (currentDefaultKeys.contains(key))
                        continue;
                    else
                        currentDefaultKeys.add(key);
                } else if (currentAdded.containsKey(key)) {
                    throw new IllegalStateException("Duplicate translation key " + key);
                }
                currentAdded.put(key, keyAndTranslation.getValue());
            }
        }
    }

    public void addModule(Module module) {
        for (TranslatableLang lang : module.getConfig().getAllTranslatableLang()) {
            this.addModuleLang(lang);
        }
    }

    public void onGatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        boolean run = event.includeClient();
        for (Map.Entry<String, Map<String, String>> localeAndTranslations : this.allTranslations.entrySet()) {
            generator.addProvider(
                run,
                new LanguageProvider(output, this.modId, localeAndTranslations.getKey()) {
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
