package com.xyc.practicalextensions.lang;

import com.xyc.practicalextensions.base.Module;
import net.minecraft.data.PackOutput;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public abstract class LanguageProviderWrapper extends LanguageProvider {
    protected String locale;

    public LanguageProviderWrapper(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
        this.locale = locale;
    }

    public void addModuleLangTranslations(TranslatableLang lang) {
        lang.getAllTranslationsOfLocale(locale)
            .ifPresent(t -> {
                for (Map.Entry<String, String> entry: t.entrySet()) {
                    this.add(entry.getKey(), entry.getValue());
                }
            });
    }

    public void addModuleTranslations(Module module) {
        this.addModuleLangTranslations(module.getOption());
        this.addModuleLangTranslations(module.getTooltip());
    }
}
