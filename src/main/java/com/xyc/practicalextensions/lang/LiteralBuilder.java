package com.xyc.practicalextensions.lang;

@SuppressWarnings("unused")
public class LiteralBuilder extends ModuleLangBuilder<TranslatableBuilder> {
    protected final String text;

    public LiteralBuilder(String text) {
        this.text = text;
    }

    public LiteralLang build() {
        return new LiteralLang(this.text, this.formats);
    }
}
