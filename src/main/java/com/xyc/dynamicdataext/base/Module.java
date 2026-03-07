package com.xyc.dynamicdataext.base;

import com.xyc.dynamicdataext.config.ModuleConfig;
import com.xyc.dynamicdataext.config.ModuleConfigBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public abstract class Module {
    protected final String moduleId;
    protected final String namespace;
    protected final ModuleConfigBuilder configBuilder;
    protected ModuleConfig config;

    protected Module(String namespace, String moduleId) {
        this.moduleId = moduleId;
        this.namespace = namespace;
        this.configBuilder = new ModuleConfigBuilder(namespace, moduleId);
    }

    public final String getModuleId() {
        return this.moduleId;
    }

    public final String getNamespace() {
        return this.namespace;
    }

    /**
     * @return 包含的配方ID会被滤除（Mixin注入配方前确定）
     */
    @Nonnull
    public Set<ResourceLocation> gatherRecipesToRemove() {
        return Set.of();
    }

    /**
     * @return 不为null时，满足条件的原有配方会被滤除（Mixin注入配方期间运行）
     * @see com.xyc.dynamicdataext.DynamicDataRegistry#injectRecipes(net.minecraft.world.item.crafting.RecipeManager)
     */
    @Nullable
    public BiPredicate<ResourceLocation, Recipe<?>> gatherRecipeExcluder() {
        return null;
    }

    /**
     * @return 添加静态配方（Mixin注入配方前生成）
     */
    @Nonnull
    public Set<DynamicRecipeEntry> gatherRecipesToAdd() {
        return Set.of();
    }

    /**
     * 可使用{@link Module#gatherRecipeExcluder()}返回值产生的中间结果（用字段或属性临时存储）
     *
     * @return 添加动态配方（Mixin注入配方期间生成）
     * @see com.xyc.dynamicdataext.DynamicDataRegistry#injectRecipes(net.minecraft.world.item.crafting.RecipeManager)
     */
    @Nullable
    public Supplier<Set<DynamicRecipeEntry>> gatherRecipeProvider() {
        return null;
    }

    /**
     * HolderSet为空时表示移除整个标签，否则只移除该标签中HolderSet所包含的元素
     */
    @Nonnull
    public Set<DynamicTagEntry<?>> gatherTagsToRemove() {
        return Set.of();
    }

    /**
     * TagKey存在时表示向已有标签追加元素，否则新建标签
     */
    @Nonnull
    public Set<DynamicTagEntry<?>> gatherTagsToAdd() {
        return Set.of();
    }

    @Nonnull
    protected ModuleConfig buildConfig() {
        return this.configBuilder.build();
    }

    public final ModuleConfig getConfig() {
        if (this.config == null) {
            this.config = this.buildConfig();
        }
        return this.config;
    }

    public final boolean isEnabled() {
        return this.getConfig().isEnabled();
    }
}
