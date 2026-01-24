package com.xyc.practicalextensions;

import com.mojang.logging.LogUtils;
import com.xyc.practicalextensions.utils.IInjectingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * 所有扩展模组共享
 */
@EventBusSubscriber(modid = ModMain.MOD_ID)
public final class PracticalExtensionRegistry {
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final Set<ModuleConfig> CONFIGS = new HashSet<>();

    public static void registerConfig(ModuleConfig config) {
        CONFIGS.add(config);
    }

    public static Pair<Set<RecipeHolder<Recipe<?>>>, Set<ResourceLocation>> getAllRecipesToUpdate() {
        Set<RecipeHolder<Recipe<?>>> recipesToAdd = new HashSet<>();
        Set<ResourceLocation> recipesToRemove = new HashSet<>();
        CONFIGS.forEach(
            config -> config.MODULES
                .stream()
                .filter(module -> config.isModuleEnabled(module.ID))
                .forEach(
                    module -> {
                        recipesToAdd.addAll(module.gatherRecipesToAdd());
                        recipesToRemove.addAll(module.gatherRecipesToRemove());
                    }
                )
        );
        return Pair.of(recipesToAdd, recipesToRemove);
    }

    @SubscribeEvent
    public static void onServerStarting(final ServerStartingEvent event) {
        /*
        起初考虑在 RecipeManager#apply 末尾注入配方，
        但配方载入早于非 Minecraft 内置的标签的注册，
        因此在每次启动后重新加载一次资源前可用标签只有少量内置标签，导致使用标签的动态配方无法生成
        改为在标签更新（TagsUpdatedEvent）后再注入配方可以在集成服务器中解决这一问题，
        但在专用服务器（Dedicated Server）中此时 ServerLifecycleHooks.getCurrentServer() 获取到的服务器实例仍是 null
        因此进一步改为在服务器启动后（ServerStartingEvent）注入配方
        */

        Triple<Long, Long, Long> result =
            ((IInjectingRecipe) event.getServer().getRecipeManager()).practicalextensions$injectRecipes();
        LOGGER.info(
            "{} recipe(s) removed and {} recipe(s) added in {} ms",
            result.getLeft(), result.getMiddle(), result.getRight()
        );
    }
}
