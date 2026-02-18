package com.xyc.dynamicdataext.lang;

import com.xyc.dynamicdataext.base.Module;
import net.minecraft.data.PackOutput;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public abstract class LanguageProviderWrapper extends LanguageProvider {
    protected final String locale;
    protected final Set<String> defaultKeys = new HashSet<>();

    public LanguageProviderWrapper(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
        this.locale = locale;
    }

    public void addModuleLangTranslations(TranslatableLang lang) {
        lang.getAllTranslationsOfLocale(this.locale)
            .ifPresent(t -> {
                for (Map.Entry<String, String> entry : t.entrySet()) {
                    String key = entry.getKey();
                    if (key.contains(".default.")) {
                        if (defaultKeys.contains(key))
                            continue;
                        else
                            defaultKeys.add(key);
                    }
                    this.add(entry.getKey(), entry.getValue());
                }
            });
    }

    public void addModuleTranslations(Module module) {
        for (TranslatableLang lang : module.getConfig().getAllTranslatableLang()) {
            this.addModuleLangTranslations(lang);
        }
    }
}
