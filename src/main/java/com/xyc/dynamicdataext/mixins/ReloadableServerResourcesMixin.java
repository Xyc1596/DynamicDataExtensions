package com.xyc.dynamicdataext.mixins;

import com.mojang.logging.LogUtils;
import com.xyc.dynamicdataext.DynamicDataRegistry;
import com.xyc.dynamicdataext.base.IReloadableServerResourcesExtensions;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.tags.TagManager;
import net.minecraft.world.item.crafting.RecipeManager;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ReloadableServerResources.class)
public abstract class ReloadableServerResourcesMixin implements IReloadableServerResourcesExtensions {
    @Shadow
    @Final
    private RecipeManager recipes;
    @Shadow
    @Final
    private TagManager tagManager;

    @Unique
    private static final Logger practicalExtensions$LOGGER = LogUtils.getLogger();

    @Unique
    @Override
    public void dynamicdataext$injectTags() {
        DynamicDataRegistry.injectTags(this.tagManager);
    }

    @Unique
    @Override
    public void dynamicdataext$injectRecipes() {
        DynamicDataRegistry.injectRecipes(this.recipes);
    }

    @Unique
    @Override
    public void dynamicdataext$injectData() {
        DynamicDataRegistry.injectTags(this.tagManager);
        DynamicDataRegistry.injectRecipes(this.recipes);
    }
}
