package com.xyc.dynamicdataext.lang;

public class PlaceholderBuilder extends ModuleLangBuilder<PlaceholderBuilder> {
    protected ModuleLang defaultContent;

    public PlaceholderBuilder() {
    }

    @SuppressWarnings("unused")
    public PlaceholderBuilder defaultContent(ModuleLang defaultContent) {
        this.defaultContent = defaultContent;
        return this;
    }

    public PlaceholderLang build() {
        return new PlaceholderLang(this.defaultContent, this.formats);
    }
}
