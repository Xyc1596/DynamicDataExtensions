package com.xyc.dynamicdataext.lang;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.*;

public class TranslatableLang extends ModuleLang {
    protected final String key;
    protected final List<ModuleLang> children;
    protected final Map<String, Map<String, String>> allTranslations;   // locale, (key, translation)

    public TranslatableLang(
        String category,
        String namespace,
        String[] id,
        Set<ChatFormatting> formats,
        List<ModuleLang> children,
        Map<String, String> translations    // locale, translation
    ) {
        super(formats);
        List<String> keyParts = new LinkedList<>();
        if (!(category == null || category.isEmpty()))
            keyParts.add(category);
        if (namespace == null || namespace.isEmpty())
            throw new IllegalArgumentException("Namespace is null or empty");
        else
            keyParts.add(namespace);
        keyParts.addAll(Arrays.asList(id));

        this.key = String.join(".", keyParts);
        this.children = children;
        this.allTranslations = new HashMap<>();

        Set<TranslatableLang> allTranslatableChildren = new HashSet<>();
        for (ModuleLang child : children) {
            if (child instanceof TranslatableLang tc) {
                allTranslatableChildren.add(tc);
            }
        }

        for (Map.Entry<String, String> entry : translations.entrySet()) {
            String locale = entry.getKey(), translation = entry.getValue();
            this.allTranslations.putIfAbsent(entry.getKey(), new HashMap<>());
            this.allTranslations.get(locale).put(key, translation);
        }

        for (TranslatableLang child : allTranslatableChildren) {
            Map<String, Map<String, String>> childAllTranslations = child.getAllTranslations();
            for (Map.Entry<String, Map<String, String>> entry : childAllTranslations.entrySet()) {
                String locale = entry.getKey();
                this.allTranslations.putIfAbsent(locale, new HashMap<>());
                this.allTranslations.get(locale).putAll(entry.getValue());
            }
        }
    }

    public static TranslatableLang empty(String category, String namespace, String... id) {
        return new TranslatableLang(category, namespace, id, Set.of(), List.of(), Map.of());
    }

    /**
     * locale, (key, translation)
     */
    public Map<String, Map<String, String>> getAllTranslations() {
        return this.allTranslations;
    }

    public Optional<Map<String, String>> getAllTranslationsOfLocale(String locale) {
        return Optional.ofNullable(this.allTranslations.get(locale));
    }

    @Override
    public MutableComponent toComponent() {
        return Component.translatable(
            this.key,
            (Object[]) children.stream().map(ModuleLang::toComponent).toArray(MutableComponent[]::new)
        ).withStyle(this.formats);
    }

    public MutableComponent toComponentReplacingPlaceholders(Component... contents) {
        List<Component> objects = new LinkedList<>();
        Queue<Component> extra = new LinkedList<>(Arrays.asList(contents));
        for (ModuleLang c : children) {
            if (c instanceof PlaceholderLang cp) {
                objects.add(cp.replaceWith(extra.poll()));
            } else {
                objects.add(c.toComponent());
            }
        }
        return Component.translatable(
            this.key,
            objects.toArray()
        ).withStyle(this.formats);
    }
}
