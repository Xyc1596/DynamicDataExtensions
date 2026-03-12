package com.xyc.dynamicdataext.lang;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

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
        if (formats.length == 0)
            return this.reference;
        return ReferenceLang.of(this, formats);
    }

    public static TranslatableLang.Builder translatable(String category, String namespace, String... id) {
        return new TranslatableLang.Builder(category, namespace, id);
    }

    public abstract static class AbstractBuilder<T extends AbstractBuilder<T>> {
        protected final Set<ChatFormatting> formats = new LinkedHashSet<>();

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

    }
}
