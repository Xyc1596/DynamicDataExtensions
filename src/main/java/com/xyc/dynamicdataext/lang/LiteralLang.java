package com.xyc.dynamicdataext.lang;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Set;

public class LiteralLang extends ModuleLang {
    protected final String text;
    protected PlaceholderLang placeholder;

    public LiteralLang(String text, Set<ChatFormatting> formats) {
        super(formats);
        this.text = text;
    }

    @Override
    public MutableComponent toComponent() {
        return Component.literal(this.text).withStyle(this.formats);
    }

    @Override
    public PlaceholderLang getPlaceholder(ChatFormatting... formats) {
        if (this.placeholder == null)
            this.placeholder = new PlaceholderLang(this, Set.of(formats));
        return this.placeholder;
    }

    public static final LiteralLang EMPTY = new LiteralLang("", Set.of());
}
