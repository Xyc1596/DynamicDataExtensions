package com.xyc.dynamicdataext.lang;

import com.google.errorprone.annotations.CanIgnoreReturnValue;

import java.util.ArrayList;
import java.util.List;

public class TemplateReplacer {
    protected TemplateLang original;
    protected List<ModuleLang> children;
    protected int childIndex = 0;

    public TemplateReplacer(TemplateLang original) {
        this.original = original;
        this.children = new ArrayList<>(original.getChildren());
    }

    public TemplateReplacer replaceEmpty(ModuleLang... replacement) {
        int replaceIdx = 0;
        while (this.childIndex < this.children.size() && replaceIdx < replacement.length) {
            if (this.children.get(this.childIndex).equals(ReferenceLang.EMPTY)) {
                this.replaceEmpty(this.childIndex, replacement[replaceIdx++]);
                this.childIndex++;
            }
        }
        return this;
    }

    @CanIgnoreReturnValue
    public TemplateReplacer replaceEmpty(int idx, ModuleLang replacement) {
        this.children.set(idx, replacement);
        return this;
    }

    public TemplateLang toTemplate() {
        return this.original.copyReplacingChildren(this.children);
    }
}
