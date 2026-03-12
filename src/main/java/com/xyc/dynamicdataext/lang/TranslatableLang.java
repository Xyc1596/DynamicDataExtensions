package com.xyc.dynamicdataext.lang;

import net.minecraft.ChatFormatting;

import java.util.*;

public class TranslatableLang extends AbstractTranslatableLang {
    public TranslatableLang(
        String category,
        String namespace,
        String[] id,
        List<ModuleLang> children,
        Map<String, String> translations,
        Collection<ChatFormatting> formats
    ) {
        super(category, namespace, id, children, translations, formats);
    }
}
