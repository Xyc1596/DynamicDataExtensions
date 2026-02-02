package com.xyc.dynamicdataext.lang;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;

public abstract class ModuleLang {
    @Unmodifiable
    protected final ChatFormatting[] formats;

    public ModuleLang(Set<ChatFormatting> formats) {
        this.formats = formats.toArray(ChatFormatting[]::new);
    }

    public abstract MutableComponent toComponent();
}
