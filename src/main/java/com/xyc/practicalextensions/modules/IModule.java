package com.xyc.practicalextensions.modules;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Set;

public interface IModule {
    String getId();

    /*
     在扩展模组中应重写，替换掉命名空间
     default ResourceLocation getResourceLocation() {
         return ResourceLocation.fromNamespaceAndPath(ModMain.MOD_ID, getId());
     }
    */

    default Set<ResourceLocation> gatherRecipesToRemove() {
        return Set.of();
    }

    default Set<RecipeHolder<Recipe<?>>> gatherRecipesToAdd() {
        return Set.of();
    }

    default String getOptionTranslateKey() {
        return Utils.translateKey("option", getId());
    }

    default String getTooltipTranslateKey() {
        return Utils.translateKey("tooltip", getId());
    }
}
