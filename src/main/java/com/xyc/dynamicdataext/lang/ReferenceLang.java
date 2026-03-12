package com.xyc.dynamicdataext.lang;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * 转换为{@link MutableComponent}前将自身替换为另一个{@link ModuleLang}，
 * 使得同一{@link ModuleLang}能够重复出现而不引发Duplicate translation key报错
 */
public class ReferenceLang extends ModuleLang {
    public static final ReferenceLang EMPTY = new ReferenceLang(null);

    protected final @Nullable ModuleLang content;

    protected ReferenceLang(@Nullable ModuleLang content, ChatFormatting... formats) {
        super(formats);
        this.content = content;
    }

    public ModuleLang get() {
        return this.content == null ? LiteralLang.EMPTY : this.content;
    }

    public boolean isEmpty() {
        return this.content == null;
    }

    @Override
    public MutableComponent toComponent() {
        return this.get().toComponent().withStyle(this.formats);
    }

    static ReferenceLang of(@NotNull ModuleLang content, ChatFormatting... formats) {
        if (content instanceof ReferenceLang reference)
            return Arrays.equals(reference.formats, formats)
                ? reference
                : new ReferenceLang(reference.content, formats);
        return new ReferenceLang(content, formats);
    }
}
