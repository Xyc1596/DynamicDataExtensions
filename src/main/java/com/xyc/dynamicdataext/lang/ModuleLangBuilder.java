package com.xyc.dynamicdataext.lang;

import net.minecraft.ChatFormatting;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public abstract class ModuleLangBuilder<T extends ModuleLangBuilder<T>> {
    protected final Set<ChatFormatting> formats = new HashSet<>();

    @SuppressWarnings("unchecked")
    protected final T self() {
        return (T) this;
    }

    public T format(ChatFormatting format) {
        formats.add(format);
        return self();
    }

    public T format(ChatFormatting... formats) {
        this.formats.addAll(Set.of(formats));
        return self();
    }

    public static LiteralBuilder literal(String text) {
        return new LiteralBuilder(text);
    }

    public static TranslatableBuilder translatable(String category, String namespace, String id) {
        return new TranslatableBuilder(category, namespace, id);
    }
}
