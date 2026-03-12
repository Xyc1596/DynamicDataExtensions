package com.xyc.dynamicdataext.lang;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

public abstract class AbstractTranslatableLang extends ModuleLang {
    protected final String key;
    protected final List<ModuleLang> children;
    protected final Map<String, String> translations; // locale, translation

    public AbstractTranslatableLang(
        String category,
        String namespace,
        String[] id,
        List<ModuleLang> children,
        Map<String, String> translations,
        Collection<ChatFormatting> formats
    ) {
        super(formats);
        this.key = assembleKey(category, namespace, id);
        this.children = children;
        this.translations = translations;
    }

    protected AbstractTranslatableLang(
        String key,
        List<ModuleLang> children,
        Map<String, String> translations,
        Collection<ChatFormatting> formats
    ) {
        super(formats);
        this.key = key;
        this.children = children;
        this.translations = translations;
    }

    public String getKey() {
        return this.key;
    }

    public List<ModuleLang> getChildren() {
        return this.children;
    }

    public Map<String, String> getTranslations() {
        return this.translations;
    }

    @Override
    public MutableComponent toComponent() {
        MutableComponent[] childComponents = new MutableComponent[this.children.size()];
        int idx = 0;
        for (ModuleLang child : this.children)
            childComponents[idx++] = child.toComponent();
        return Component.translatable(this.key, (Object) childComponents).withStyle(this.formats);
    }

    protected static String assembleKey(String category, String namespace, String[] id) {
        List<String> keyParts = new ArrayList<>();
        if (!(category == null || category.isEmpty()))
            keyParts.add(category);
        if (namespace == null || namespace.isEmpty())
            throw new IllegalArgumentException("Namespace is null or empty");
        else
            keyParts.add(namespace);
        keyParts.addAll(Arrays.asList(id));
        return String.join(".", keyParts);
    }

    public static class Builder extends AbstractBuilder<Builder> {
        protected final String category;
        protected final String namespace;
        protected final String[] id;
        protected final String[] appendId;
        protected final List<ModuleLang> children = new ArrayList<>();
        protected final Map<String, String> translations = new HashMap<>();

        public Builder(String category, String namespace, String... id) {
            this(category, namespace, id, new String[0]);
        }

        public Builder(
            String category,
            String namespace,
            String[] id,
            String... appendId
        ) {
            this.category = category;
            this.namespace = namespace;
            this.id = id;
            this.appendId = appendId;
        }

        public Builder child(ModuleLang child) {
            this.children.add(child);
            return this;
        }

        public Builder translation(String locale, String text) {
            this.translations.put(locale, text);
            return this;
        }

        public TranslatableLang build() {
            return new TranslatableLang(
                this.category,
                this.namespace,
                this.id,
                this.children,
                this.translations,
                this.formats
            );
        }

        public TemplateLang buildRootTemplate() {
            return new TemplateLang(
                this.category,
                this.namespace,
                this.id,
                this.children,
                this.translations,
                this.formats
            );
        }

        public ReferenceLang buildReference() {
            return this.build().getReference();
        }

        public Builder childTranslatableBuilder(String... appendId) {
            String[] newId = ArrayUtils.addAll(this.id, appendId);
            return new Builder(this.category, this.namespace, newId);
        }

        public Builder childTranslatableBuilder() {
            String[] newId = ArrayUtils.addAll(this.id, String.valueOf(this.children.size()));
            return new Builder(this.category, this.namespace, newId);
        }
    }
}
