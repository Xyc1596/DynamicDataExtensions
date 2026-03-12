package com.xyc.dynamicdataext.lang;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class LiteralLang extends ModuleLang {
    protected final String text;

    protected LiteralLang(String text, ChatFormatting... formats) {
        super(formats);
        this.text = text;
    }

    @Override
    public MutableComponent toComponent() {
        return Component.literal(this.text).withStyle(this.formats);
    }

    public static LiteralLang of(String text, ChatFormatting... formats) {
        return new LiteralLang(text, formats);
    }

    public static final LiteralLang EMPTY = new LiteralLang("");
}
