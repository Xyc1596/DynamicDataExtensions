package com.xyc.dynamicdataext.utils;

import com.xyc.dynamicdataext.lang.ModuleLangBuilder;
import com.xyc.dynamicdataext.lang.TranslatableBuilder;

public final class ModMainUtils {
    public static TranslatableBuilder createModTitleBuilder(String namespace) {
        return ModuleLangBuilder.translatable(null, namespace);
    }
}
