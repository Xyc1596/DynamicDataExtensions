package com.xyc.dynamicdataext;

import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.lang.*;
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

    protected final Set<String> addedTemplateKeys = new HashSet<>();
    protected final Set<TemplateLang> addedTemplateRoots = new HashSet<>();
    protected final Set<String> addedTranslatableKeys = new HashSet<>();

    public DynamicDataLanguageProvider(String modId) {
        this.modId = modId;
    }

    protected void addAbstract(AbstractTranslatableLang lang) {
        String key = lang.getKey();
        for (Map.Entry<String, String> entry : lang.getTranslations().entrySet()) {
            String locale = entry.getKey();
            this.allTranslations.computeIfAbsent(locale, l -> new LinkedHashMap<>())
                                .put(key, entry.getValue());
        }

        for (ModuleLang child : lang.getChildren())
            this.addModuleLang(child);
    }

    /**
     * 如果lang不是root，则尝试添加它的root<br/>
     * 如果lang是root，那么：<ul>
     * <li>key重复，root不重复 -> 抛出异常</li>
     * <li>key重复，root重复 -> 无事发生</li>
     * <li>key不重复，root重复 -> 添加成功（实际无事发生）</li>
     * <li>key不重复，root不重复 -> 添加成功</li></ul>
     */
    protected void add(TemplateLang lang) {
        if (lang.isRoot()) {
            String key = lang.getKey();
            if (this.addedTemplateKeys.add(key)) {
                if (!this.addedTemplateRoots.add(lang))
                    throw new IllegalStateException(
                        "Duplicate template translation key " + key + " for templates with different roots."
                    );
            } else this.add(lang);
        } else this.add(lang.getRootOrSelf());
    }

    protected void add(TranslatableLang lang) {
        String key = lang.getKey();
        if (!this.addedTranslatableKeys.add(key))
            throw new IllegalStateException(
                "Duplicate translation key " + key + ". Use ReferenceLang or TemplateLang " +
                    "instead of the original ModuleLang instance as a child of another ModuleLang."
            );
        this.addAbstract(lang);
    }

    /**
     * 如果lang是空的，那么无事发生；<br/>
     * 如果lang不是空的，那么：<ul>
     * <li>content为{@link TranslatableLang}，key重复 -> 无事发生</li>
     * <li>content为{@link TranslatableLang}，key不重复 -> 添加成功</li>
     * <li>content为{@link TemplateLang} -> 尝试添加</li>
     * <li>content为{@link ReferenceLang} -> 不存在</li></ul>
     */
    protected void add(ReferenceLang lang) {
        if (!lang.isEmpty()) {
            switch (lang.get()) {
                case TranslatableLang translatable -> {
                    if (!this.addedTranslatableKeys.contains(translatable.getKey()))
                        this.addAbstract(translatable);
                }
                case TemplateLang template -> this.add(template);
                default -> {
                }
            }
        }
    }

    public void addModule(Module module) {
        for (ModuleLang lang : module.getConfig().getAllModuleLang())
            this.addModuleLang(lang);
    }

    public void addModuleLang(ModuleLang lang) {
        switch (lang) {
            case TranslatableLang translatable -> this.add(translatable);
            case TemplateLang template -> this.add(template);
            case ReferenceLang reference -> this.add(reference);
            default -> {
            }
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
