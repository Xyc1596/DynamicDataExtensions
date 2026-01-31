package com.xyc.practicalextensions.mixins;

import com.xyc.practicalextensions.base.IInjectingRecipe;
import com.xyc.practicalextensions.base.IInjectingReloadableServerResources;
import com.xyc.practicalextensions.base.IInjectingTags;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.tags.TagManager;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ReloadableServerResources.class)
public abstract class ReloadableServerResourcesMixin implements IInjectingReloadableServerResources {
    @Shadow
    @Final
    private RecipeManager recipes;
    @Shadow
    @Final
    private TagManager tagManager;

    @Unique
    public void practicalExtensions$injectDataIntoManagers() {
        ((IInjectingTags) this.tagManager).practicalextensions$injectTags();
        ((IInjectingRecipe) this.recipes).practicalextensions$injectRecipes();
    }
}
