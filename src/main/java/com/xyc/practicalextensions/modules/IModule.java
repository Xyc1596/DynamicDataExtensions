package com.xyc.practicalextensions.modules;

import com.xyc.practicalextensions.ModMain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Set;

public interface IModule {
    String getId();

    // 在扩展模组中应重写，替换掉命名空间
    default ResourceLocation getResourceLocation() {
        return ResourceLocation.fromNamespaceAndPath(ModMain.MOD_ID, getId());
    }

    default Set<ResourceLocation> gatherRecipesToRemove() {
        return null;
    }

    default Set<Recipe<?>> gatherRecipesToAdd() {
        return null;
    }


}
