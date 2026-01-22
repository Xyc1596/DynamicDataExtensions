package com.xyc.practicalextensions.modules;

import com.xyc.practicalextensions.config.ModuleConfig;
import com.xyc.practicalextensions.modules.contents.LeatherFromRottenFlesh;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.Set;

public class ModuleManager {
    protected static final Set<IModule> MODULES = Set.of(
        new LeatherFromRottenFlesh()
    );

    public static Set<IModule> getModules() {
        return MODULES;
    }

    public static Pair<Set<RecipeHolder<Recipe<?>>>, Set<ResourceLocation>> getRecipesToUpdate() {
        Set<RecipeHolder<Recipe<?>>> recipesToAdd = new HashSet<>();
        Set<ResourceLocation> recipesToRemove = new HashSet<>();
        MODULES.stream()
               .filter(m -> ModuleConfig.isModuleEnabled(m.getId()))
               .forEach(
                   m -> {
                       recipesToAdd.addAll(m.gatherRecipesToAdd());
                       recipesToRemove.addAll(m.gatherRecipesToRemove());
                   }
               );
        return Pair.of(recipesToAdd, recipesToRemove);
    }
}
