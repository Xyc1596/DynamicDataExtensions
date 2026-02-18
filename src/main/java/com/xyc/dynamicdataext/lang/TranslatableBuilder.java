package com.xyc.dynamicdataext.lang;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranslatableBuilder extends ModuleLangBuilder<TranslatableBuilder> {
    protected final String category;
    protected final String namespace;
    protected final String[] id;
    protected final List<ModuleLang> children = new ArrayList<>();
    protected final Map<String, String> translations = new HashMap<>();

    public TranslatableBuilder(String category, String namespace, String... id) {
        this.category = category;
        this.namespace = namespace;
        this.id = id;
    }

    public com.xyc.dynamicdataext.lang.TranslatableBuilder child(ModuleLang child) {
        this.children.add(child);
        return this;
    }

    public com.xyc.dynamicdataext.lang.TranslatableBuilder translation(String locale, String text) {
        this.translations.put(locale, text);
        return this;
    }

    public TranslatableLang build() {
        return new TranslatableLang(
            this.category,
            this.namespace,
            this.id,
            this.formats,
            this.children,
            this.translations
        );
    }

    public TranslatableBuilder childTranslatableBuilder(String... appendId) {
        String[] newId = ArrayUtils.addAll(this.id, appendId);
        return new TranslatableBuilder(this.category, this.namespace, newId);
    }

    public TranslatableBuilder childTranslatableBuilder() {
        String[] newId = ArrayUtils.addAll(this.id, String.valueOf(this.children.size()));
        return new TranslatableBuilder(this.category, this.namespace, newId);
    }
}
