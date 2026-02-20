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
    protected final ModuleLang defaultContent;

    public PlaceholderLang(@Nullable ModuleLang defaultContent, Set<ChatFormatting> formats) {
        super(formats);
        this.defaultContent = defaultContent == null ? LiteralLang.EMPTY : defaultContent;
    }

    @Override
    public MutableComponent toComponent() {
        return this.defaultContent.toComponent().withStyle(this.formats);
    }

    public Component replaceWith(@Nullable Component component) {
        return (component == null ? this.defaultContent.toComponent() : component);
    }

    @Override
    public PlaceholderLang getPlaceholder() {
        return this;
    }

    public static final PlaceholderLang EMPTY = new PlaceholderLang(null, Set.of());
}
