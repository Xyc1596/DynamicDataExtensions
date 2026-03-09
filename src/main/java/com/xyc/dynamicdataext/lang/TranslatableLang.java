package com.xyc.dynamicdataext.lang;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.*;

public class TranslatableLang extends ModuleLang {
    protected final String key;
    protected final List<ModuleLang> children;
    protected final Map<String, String> translations; // locale, translation

    protected TranslatableLang(
        String key,
        List<ModuleLang> children,
        Map<String, String> translations,
        ChatFormatting... formats
    ) {
        super(formats);
        this.key = key;
        this.children = children;
        this.translations = translations;
    }

    public TranslatableLang(
        String category,
        String namespace,
        String[] id,
        List<ModuleLang> children,
        Map<String, String> translations,
        Collection<ChatFormatting> formats
    ) {
        super(formats);
        this.key = assembleKey(category, namespace, id);
        this.children = children;
        this.translations = translations;
    }

    public String getKey() {
        return this.key;
    }

    public List<ModuleLang> getChildren() {
        return this.children;
    }

    public Map<String, String> getTranslations() {
        return this.translations;
    }

    @Override
    public MutableComponent toComponent() {
        MutableComponent[] childComponents = new MutableComponent[this.children.size()];
        int idx = 0;
        for (ModuleLang child : this.children)
            childComponents[idx++] = child.toComponent();
        return Component.translatable(this.key, (Object) childComponents).withStyle(this.formats);
    }

    protected static String assembleKey(String category, String namespace, String[] id) {
        List<String> keyParts = new ArrayList<>();
        if (!(category == null || category.isEmpty()))
            keyParts.add(category);
        if (namespace == null || namespace.isEmpty())
            throw new IllegalArgumentException("Namespace is null or empty");
        else
            keyParts.add(namespace);
        keyParts.addAll(Arrays.asList(id));
        return String.join(".", keyParts);
    }
}
