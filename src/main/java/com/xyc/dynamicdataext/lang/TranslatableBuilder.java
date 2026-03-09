package com.xyc.dynamicdataext.lang;

import net.minecraft.ChatFormatting;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

public class TranslatableBuilder<T extends TranslatableLang> extends ModuleLangBuilder<TranslatableBuilder<T>> {
    protected final String category;
    protected final String namespace;
    protected final String[] id;
    protected final String[] appendId;
    protected final Factory<T> factory;
    protected final List<ModuleLang> children = new ArrayList<>();
    protected final Map<String, String> translations = new HashMap<>();

    public TranslatableBuilder(Factory<T> factory, String category, String namespace, String... id) {
        this(factory, category, namespace, id, new String[0]);
    }

    public TranslatableBuilder(
        Factory<T> factory,
        String category,
        String namespace,
        String[] id,
        String... appendId
    ) {
        this.factory = factory;
        this.category = category;
        this.namespace = namespace;
        this.id = id;
        this.appendId = appendId;
    }

    public TranslatableBuilder<T> child(ModuleLang child) {
        this.children.add(child);
        return this;
    }

    public TranslatableBuilder<T> translation(String locale, String text) {
        this.translations.put(locale, text);
        return this;
    }

    public T build() {
        return this.factory.build(
            this.category,
            this.namespace,
            this.id,
            this.children,
            this.translations,
            this.formats
        );
    }

    public TranslatableBuilder<TranslatableLang> childTranslatableBuilder(String... appendId) {
        String[] newId = ArrayUtils.addAll(this.id, appendId);
        return new TranslatableBuilder<>(TranslatableLang::new, this.category, this.namespace, newId);
    }

    public TranslatableBuilder<TranslatableLang> childTranslatableBuilder() {
        String[] newId = ArrayUtils.addAll(this.id, String.valueOf(this.children.size()));
        return new TranslatableBuilder<>(TranslatableLang::new, this.category, this.namespace, newId);
    }

    public interface Factory<T> {
        T build(
            String category,
            String namespace,
            String[] id,
            List<ModuleLang> children,
            Map<String, String> translations,
            Collection<ChatFormatting> formats
        );
    }
}
