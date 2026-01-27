package com.xyc.practicalextensions.lang;

import net.minecraft.ChatFormatting;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class ModuleLang {
    protected final Set<ChatFormatting> formats;

    public ModuleLang(Set<ChatFormatting> formats) {
        this.formats = formats;
    }

    public static class LiteralLang extends ModuleLang {
        protected final String text;

        public LiteralLang(String text, Set<ChatFormatting> formats) {
            super(formats);
            this.text = text;
        }
    }

    public static class TranslatableLang extends ModuleLang {
        protected final String key;
        protected final List<ModuleLang> children;
        protected final Map<String, String> translations;

        public TranslatableLang(
            String key,
            Set<ChatFormatting> formats,
            List<ModuleLang> children,
            Map<String, String> translations
        ) {
            super(formats);
            this.key = key;
            this.children = children;
            this.translations = translations;
        }

        public static TranslatableLang of(String category, String namespace, String id) {
            return new TranslatableLang(
                String.join(".", category, namespace, id),
                Set.of(),
                List.of(),
                Map.of()
            );
        }

        public String getKey() {
            return key;
        }
    }
}
