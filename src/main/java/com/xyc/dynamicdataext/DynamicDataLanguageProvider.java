package com.xyc.dynamicdataext;

import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.lang.ModuleLang;
import com.xyc.dynamicdataext.lang.ReferenceLang;
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

    protected final Set<String> duplicateKeys = new HashSet<>();
    protected final Set<String> addedKeys = new HashSet<>();

    public DynamicDataLanguageProvider(String modId) {
        this.modId = modId;
    }

    public void addModuleLang(TranslatableLang lang) {
        this.addTranslatableLang(lang, false);
    }

    protected void addTranslatableLang(TranslatableLang lang, boolean fromPlaceholder) {
        String key = lang.getKey();
        if (lang.isTemplate() || fromPlaceholder) {
            if (!duplicateKeys.add(key))
                return;
        } else if (duplicateKeys.contains(key)) {
            return;
        } else if (!addedKeys.add(key)) {
            throw new IllegalStateException(
                "Duplicate translation key " + key + ". Use PlaceholderLang " +
                    "instead of the original ModuleLang instance as a child of another ModuleLang."
            );
        }

        for (Map.Entry<String, String> entry : lang.getTranslations().entrySet()) {
            String locale = entry.getKey();
            this.allTranslations.putIfAbsent(locale, new LinkedHashMap<>());
            this.allTranslations.get(locale).put(key, entry.getValue());
        }

        for (ModuleLang child : lang.getChildren()) {
            if (child instanceof TranslatableLang translatable)
                this.addTranslatableLang(translatable, false);
            else if (child instanceof ReferenceLang placeholder) {
                this.addPlaceholderLang(placeholder);
            }
        }
    }

    protected void addPlaceholderLang(ReferenceLang lang) {
        if (!lang.isEmpty()) {
            ModuleLang content = lang.get();
            if (content instanceof TranslatableLang translatable)
                this.addTranslatableLang(translatable, true);
            else if (content instanceof ReferenceLang placeholder)
                this.addPlaceholderLang(placeholder);
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
