package com.xyc.dynamicdataext.lang;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import javax.annotation.Nullable;
import java.util.*;

public class TemplateLang extends AbstractTranslatableLang {
    protected final @Nullable TemplateLang root;
    protected final List<Integer> emptyChildIndices = new ArrayList<>();

    public TemplateLang(
        String category,
        String namespace,
        String[] id,
        List<ModuleLang> children,
        Map<String, String> translations,
        Collection<ChatFormatting> formats
    ) {
        this(null, assembleKey(category, namespace, id), children, translations, formats);
    }

    protected TemplateLang(
        @Nullable TemplateLang root,
        String key,
        List<ModuleLang> children,
        Map<String, String> translations,
        Collection<ChatFormatting> formats
    ) {
        super(key, children, translations, formats);
        this.root = root;
        for (int i = 0; i < children.size(); i++)
            if (children.get(i) instanceof TemplateLang)
                this.emptyChildIndices.add(i);
    }

    public final boolean hasSameRootWith(TemplateLang other) {
        return this.root != null && this.root.equals(other.root);
    }

    public final TemplateLang getRootOrSelf() {
        return this.root != null ? this.root : this;
    }

    public final boolean isRoot() {
        return this.root == null;
    }

    @Override
    public MutableComponent toComponent() {
        if (this.emptyChildIndices.isEmpty())
            return super.toComponent();
        else throw new IllegalStateException(
            "Template " + this.key + " cannot be translated because it still has empty child(ren) at: " +
                this.emptyChildIndices +
                ". Use toComponentReplacingEmpty() to replace them or createFiller() to create a completed template"
        );
    }

    public MutableComponent toComponentReplacingEmpty(Component... replacements) {
        int emptyChildCount = this.emptyChildIndices.size();
        if (replacements.length != emptyChildCount)
            throw new IllegalArgumentException(
                "Template " + this.key + " requires " + emptyChildCount +
                    " replacements, but got " + replacements.length
            );

        Component[] childComponents = new MutableComponent[this.children.size()];
        int idx = 0, replaceIdx = 0;
        for (ModuleLang child : this.children)
            childComponents[idx++] = child.equals(ReferenceLang.EMPTY)
                ? replacements[replaceIdx++]
                : child.toComponent();
        return Component.translatable(this.key, (Object) childComponents).withStyle(this.formats);
    }

    public Builder createFiller() {
        return new Builder(this);
    }

    public static class Builder extends AbstractBuilder<Builder> {
        protected final TemplateLang parent;
        protected final List<ModuleLang> newChildren;
        protected final List<Integer> newEmptyChildIndices;

        protected Builder(TemplateLang parent) {
            this.parent = parent;
            this.newChildren = new ArrayList<>(parent.children);
            this.newEmptyChildIndices = new ArrayList<>(parent.emptyChildIndices);
        }

        public Builder fill(ModuleLang... replacements) {
            for (int i = 0; i < replacements.length; i++)
                this.fill(this.parent.emptyChildIndices.get(i), replacements[i]);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder fill(int idx, ModuleLang replacement) {
            this.newChildren.set(idx, replacement);
            this.newEmptyChildIndices.remove(Integer.valueOf(idx));
            return this;
        }

        public TemplateLang build() {
            Set<ChatFormatting> formats = new LinkedHashSet<>(Arrays.asList(this.parent.formats));
            formats.addAll(this.formats);
            return new TemplateLang(
                this.parent.root,
                this.parent.key,
                this.newChildren,
                this.parent.translations,
                formats
            );
        }
    }
}
