package com.xyc.dynamicdataext.lang;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

public abstract class ModuleLang {
    @Unmodifiable
    protected final ChatFormatting[] formats;
    protected final ReferenceLang reference;

    public ModuleLang(Collection<ChatFormatting> formats) {
        this(formats.toArray(ChatFormatting[]::new));
    }

    public ModuleLang(ChatFormatting... formats) {
        this.formats = formats;
        this.reference = ReferenceLang.of(this);
    }

    public abstract MutableComponent toComponent();

    public final ChatFormatting[] getFormats() {
        return this.formats;
    }

    public final ReferenceLang getReference() {
        return this.reference;
    }

    public final ReferenceLang createReference(ChatFormatting... formats) {
        return ReferenceLang.of(this, formats);
    }
}
