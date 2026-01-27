package com.xyc.practicalextensions.lang;

import net.minecraft.ChatFormatting;

import java.util.*;

public abstract class ModuleLangBuilder {
    protected final Set<ChatFormatting> formats = new HashSet<>();

    public ModuleLangBuilder format(ChatFormatting format) {
        formats.add(format);
        return this;
    }

    public ModuleLangBuilder format(ChatFormatting... formats) {
        this.formats.addAll(Set.of(formats));
        return this;
    }

    public static LiteralBuilder literal(String text) {
        return new LiteralBuilder(text);
    }

    public static TranslatableBuilder translatable(String category, String namespace, String id) {
        return new TranslatableBuilder(category, namespace, id);
    }

    public static class LiteralBuilder extends ModuleLangBuilder {
        protected final String text;

        public LiteralBuilder(String text) {
            this.text = text;
        }

        public ModuleLang.LiteralLang build() {
            return new ModuleLang.LiteralLang(text, formats);
        }
    }

    public static class TranslatableBuilder extends ModuleLangBuilder {
        protected final String category, namespace, id;
        protected final List<ModuleLang> children = new ArrayList<>();
        protected final Map<String, String> translations = new HashMap<>();

        public TranslatableBuilder(String category, String namespace, String id) {
            this.category = category;
            this.namespace = namespace;
            this.id = id;
        }

        public TranslatableBuilder child(ModuleLang child) {
            this.children.add(child);
            return this;
        }

        public TranslatableBuilder translation(String locale, String text) {
            translations.put(locale, text);
            return this;
        }

        public ModuleLang.TranslatableLang build() {
            return new ModuleLang.TranslatableLang(
                String.join(".", category, namespace, id),
                formats,
                children,
                translations
            );
        }
    }
}
