package com.xyc.dynamicdataext.lang;

import net.minecraft.ChatFormatting;

import java.util.HashSet;
import java.util.Set;

public abstract class ModuleLangBuilder<T extends ModuleLangBuilder<T>> {
    protected final Set<ChatFormatting> formats = new HashSet<>();

    @SuppressWarnings("unchecked")
    protected final T self() {
        return (T) this;
    }

    public T format(ChatFormatting format) {
        this.formats.add(format);
        return self();
    }

    public T format(ChatFormatting... formats) {
        this.formats.addAll(Set.of(formats));
        return self();
    }

    public static LiteralBuilder literal(String text) {
        return new LiteralBuilder(text);
    }

    public static TranslatableBuilder<TranslatableLang> translatable(String category, String namespace, String... id) {
        return new TranslatableBuilder<>(TranslatableLang::new, category, namespace, id);
    }

    public static TranslatableBuilder<TemplateLang> template(String category, String namespace, String... id) {
        return new TranslatableBuilder<>(TemplateLang::new, category, namespace, id);
    }
}
