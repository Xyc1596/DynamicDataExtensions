package com.xyc.dynamicdataext.base;

import com.xyc.dynamicdataext.config.ModuleConfig;
import com.xyc.dynamicdataext.config.ModuleConfigBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

public abstract class Module {
    protected final String moduleId;
    protected final String namespace;
    protected ModuleConfig config;

    protected Module(String namespace, String moduleId) {
        this.moduleId = moduleId;
        this.namespace = namespace;
    }

    public final String getModuleId() {
        return this.moduleId;
    }

    public final String getNamespace() {
        return this.namespace;
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
    protected ModuleConfig buildConfig() {
        return ModuleConfig.defaultConfig(this.namespace, this.moduleId);
    }

    public final ModuleConfig getConfig() {
        if (this.config == null) {
            this.config = this.buildConfig();
        }
        return this.config;
    }

    protected final ModuleConfigBuilder createConfigBuilder() {
        return new ModuleConfigBuilder(this.namespace, this.moduleId);
    }
}
