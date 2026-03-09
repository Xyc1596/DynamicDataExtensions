package com.xyc.dynamicdataext.lang;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 键可以重复出现<br/>
 * 可以从另一个{@link TemplateLang}建造并替换其中的空占位符<br/>
 * 可以在调用时先替换其中的空占位符再转换为{@link MutableComponent}
 */
public class TemplateLang extends TranslatableLang {
    public TemplateLang(
        String category,
        String namespace,
        String[] id,
        List<ModuleLang> children,
        Map<String, String> translations,
        Collection<ChatFormatting> formats
    ) {
        super(category, namespace, id, children, translations, formats);
    }

    protected TemplateLang(
        String key,
        List<ModuleLang> children,
        Map<String, String> translations,
        ChatFormatting... formats
    ) {
        super(key, children, translations, formats);
    }

    public MutableComponent toComponentReplacingEmpty(Component... replacements) {
        Component[] childComponents = new MutableComponent[this.children.size()];
        int idx = 0, replaceIdx = 0;
        for (ModuleLang child : this.children)
            childComponents[idx++] = child.equals(ReferenceLang.EMPTY)
                ? replacements[replaceIdx++]
                : child.toComponent();
        return Component.translatable(this.key, (Object) childComponents).withStyle(this.formats);
    }

    public TemplateLang copyReplacingChildren(List<ModuleLang> children) {
        return new TemplateLang(this.key, children, this.translations, this.formats);
    }
}
