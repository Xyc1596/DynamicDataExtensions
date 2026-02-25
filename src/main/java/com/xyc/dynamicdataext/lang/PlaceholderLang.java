package com.xyc.dynamicdataext.lang;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * 运行时替换的%s
 */
public class PlaceholderLang extends ModuleLang {
    protected final @Nullable ModuleLang defaultContent;

    public PlaceholderLang(@Nullable ModuleLang defaultContent, Set<ChatFormatting> formats) {
        super(formats);
        this.defaultContent = defaultContent;
    }

    protected ModuleLang getDefaultContent() {
        return this.defaultContent == null ? LiteralLang.EMPTY : this.defaultContent;
    }

    public boolean isEmpty() {
        return this.defaultContent == null;
    }

    @Override
    public MutableComponent toComponent() {
        return this.getDefaultContent().toComponent().withStyle(this.formats);
    }

    public Component replaceWith(@Nullable Component component) {
        return (component == null ? this.getDefaultContent().toComponent() : component);
    }

    @Override
    public PlaceholderLang getPlaceholder(ChatFormatting... formats) {
        return this;
    }

    public static final PlaceholderLang EMPTY = new PlaceholderLang(null, Set.of());
}
