package com.xyc.dynamicdataext.base;

import com.xyc.dynamicdataext.lang.ModuleLangBuilder;
import com.xyc.dynamicdataext.lang.TranslatableBuilder;
import com.xyc.dynamicdataext.lang.TranslatableLang;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

public abstract class Module {
    protected final String id;
    protected final String namespace;
    protected TranslatableLang option;
    protected TranslatableLang tooltip;

    protected Module(String namespace, String id) {
        this.id = id;
        this.namespace = namespace;
    }

    public final String getId() {
        return this.id;
    }

    public final String getNamespace() {
        return this.namespace;
    }

    public final TranslatableLang getOption() {
        if (this.option == null) {
            this.option = buildOptionLang();
        }
        return this.option;
    }

    public final TranslatableLang getTooltip() {
        if (this.tooltip == null) {
            this.tooltip = buildTooltipLang();
        }
        return this.tooltip;
    }

    @Nonnull
    public Set<ResourceLocation> gatherRecipesToRemove() {
        return Set.of();
    }

    @Nonnull
    public Set<RecipeEntry> gatherRecipesToAdd() {
        return Set.of();
    }

    /**
     * HolderSet为空时表示移除整个标签，否则只移除该标签中HolderSet所包含的元素
     */
    @Nonnull
    public Map<TagKey<?>, Set<Holder<?>>> gatherTagsToRemove() {
        return Map.of();
    }

    /**
     * TagKey存在时表示向已有标签追加元素，否则新建标签
     */
    @Nonnull
    public Map<TagKey<?>, Set<Holder<?>>> gatherTagsToAdd() {
        return Map.of();
    }

    @Nonnull
    protected TranslatableLang buildOptionLang() {
        return TranslatableLang.of("option", this.namespace, this.id);
    }

    @Nonnull
    protected TranslatableLang buildTooltipLang() {
        return TranslatableLang.of("tooltip", this.namespace, this.id);
    }

    @Nonnull
    protected final TranslatableBuilder getOptionLangBuilder() {
        return ModuleLangBuilder.translatable("option", this.namespace, this.id);
    }

    @Nonnull
    protected final TranslatableBuilder getTooltipLangBuilder() {
        return ModuleLangBuilder.translatable("tooltip", this.namespace, this.id);
    }
}
