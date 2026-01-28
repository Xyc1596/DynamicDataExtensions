package com.xyc.practicalextensions.base;

import com.xyc.practicalextensions.lang.TranslatableLang;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import javax.annotation.Nonnull;
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

    public String getId() {
        return id;
    }

    public TranslatableLang getOption() {
        if (this.option == null) {
            this.option = buildOptionLang();
        }
        return this.option;
    }

    public TranslatableLang getTooltip() {
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
    public Set<RecipeHolder<Recipe<?>>> gatherRecipesToAdd() {
        return Set.of();
    }

    @Nonnull
    protected TranslatableLang buildOptionLang() {
        return TranslatableLang.of("option", this.namespace, this.id);
    }

    @Nonnull
    protected TranslatableLang buildTooltipLang() {
        return TranslatableLang.of("tooltip", this.namespace, this.id);
    }
}
