package com.xyc.practicalextensions.lang;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Set;

public class LiteralLang extends ModuleLang {
    protected final String text;

    public LiteralLang(String text, Set<ChatFormatting> formats) {
        super(formats);
        this.text = text;
    }

    @Override
    public MutableComponent toComponent() {
        return Component.literal(this.text).withStyle(this.formats);
    }
}
