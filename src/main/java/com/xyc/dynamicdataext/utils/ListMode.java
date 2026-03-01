package com.xyc.dynamicdataext.utils;

import com.xyc.dynamicdataext.lang.ModuleLang;

public enum ListMode implements NamedEnum {
    WHITELIST(ConfigUtils.DEFAULT_LIST_MODE_WHITELIST),
    BLACKLIST(ConfigUtils.DEFAULT_LIST_MODE_BLACKLIST);

    private final ModuleLang name;

    ListMode(ModuleLang name) {
        this.name = name;
    }

    @Override
    public ModuleLang getName() {
        return this.name;
    }
}
