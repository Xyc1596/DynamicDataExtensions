package com.xyc.dynamicdataext;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.base.*;
import com.xyc.dynamicdataext.config.ModuleConfig;
import com.xyc.dynamicdataext.lang.ModuleLang;
import com.xyc.dynamicdataext.lang.ReferenceLang;
import com.xyc.dynamicdataext.lang.TemplateLang;
import com.xyc.dynamicdataext.modules.Common;
import com.xyc.dynamicdataext.utils.ModMainUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagManager;
import net.minecraft.tags.TagManager.LoadResult;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

/**
 * 所有扩展模组共享
 */
public final class DynamicDataRegistry {
    private static final ReferenceLang TITLE = ModuleLang
        .translatable("message", DynamicDataMain.MOD_ID, "registry")
        .translation("zh_cn", "动态数据注册表")
        .translation("en_us", "Dynamic Data Registry")
        .format(ChatFormatting.DARK_AQUA, ChatFormatting.BOLD)
        .buildReference();
    private static final TemplateLang MESSAGE_FAILED_RECIPES = ModuleLang
        .translatable("message", DynamicDataMain.MOD_ID, "recipes", "failed")
        .translation("zh_cn", "[%s] 配方注入失败！%s")
        .translation("en_us", "[%s] Failed to inject recipes! %s")
        .child(TITLE)
        .child(ReferenceLang.EMPTY)
        .buildRootTemplate();
    private static final TemplateLang MESSAGE_DUPLICATE_RECIPE = ModuleLang
        .translatable("message", DynamicDataMain.MOD_ID, "recipes", "failed", "duplicate_id")
        .translation("zh_cn", "重复的配方ID：%s")
        .translation("en_us", "Duplicate recipe ID: %s")
        .child(ReferenceLang.EMPTY)
        .buildRootTemplate();
    private static final TemplateLang MESSAGE_SUCCESS_RECIPES = ModuleLang
        .translatable("message", DynamicDataMain.MOD_ID, "recipes", "success")
        .translation("zh_cn", "[%s] 配方注入成功：删除 %s，新增 %s，耗时 %s ms")
        .translation("en_us", "[%s] Successfully injected recipes: %s removed, %s added, took %s ms")
        .child(TITLE)
        .child(ReferenceLang.EMPTY)
        .child(ReferenceLang.EMPTY)
        .child(ReferenceLang.EMPTY)
        .buildRootTemplate();
    private static final TemplateLang MESSAGE_SUCCESS_TAGS = ModuleLang
        .translatable("message", DynamicDataMain.MOD_ID, "tags", "success")
        .translation("zh_cn", "[%s] 标签注入成功：修改 %s，新增 %s, 耗时 %s ms")
        .translation("en_us", "[%s] Successfully injected recipes: %s modified, %s added, took %s ms")
        .child(TITLE)
        .child(ReferenceLang.EMPTY)
        .child(ReferenceLang.EMPTY)
        .child(ReferenceLang.EMPTY)
        .buildRootTemplate();

    private static final Map<String, DynamicDataConfig> ddConfigs = new LinkedHashMap<>();
    private static IRecipeManagerExtensions recipeManager;
    private static ITagManagerExtensions tagManager;
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void registerConfig(DynamicDataConfig config) {
        ddConfigs.put(config.getNamespace(), config);
    }

    public static boolean autoReloading() {
        ModuleConfig config = getModuleConfig(DynamicDataMain.MOD_ID, Common.INSTANCE.getModuleId());
        return config != null && (boolean) config
            .getOption("auto_reload")
            .getValue();
    }

    public static boolean isModuleEnabled(String namespace, String moduleId) {
        ModuleConfig config = getModuleConfig(namespace, moduleId);
        return config != null && config.isEnabled();
    }

    public static @Nullable ModuleConfig getModuleConfig(String namespace, String moduleId) {
        if (ddConfigs.containsKey(namespace)) {
            Module module = ddConfigs.get(namespace).getModule(moduleId);
            if (module == null) {
                LOGGER.warn("Module {} not found in namespace {}", moduleId, namespace);
                return null;
            } else return module.getConfig();
        } else {
            LOGGER.warn("Namespace {} not found", namespace);
            return null;
        }
    }

    public static IRecipeManagerExtensions getRecipeManager() {
        return recipeManager;
    }

    @SuppressWarnings("unused")
    public static ITagManagerExtensions getTagManager() {
        return tagManager;
    }

    public static void injectRecipes(RecipeManager manager) {
        long t1 = System.currentTimeMillis();
        recipeManager = (IRecipeManagerExtensions) manager;

        Set<ResourceLocation> recipesToRemove = new LinkedHashSet<>();
        Set<BiPredicate<ResourceLocation, Recipe<?>>> recipeExcluders = new LinkedHashSet<>();
        Set<DynamicRecipeEntry> recipesToAdd = new LinkedHashSet<>();
        Set<Supplier<Set<DynamicRecipeEntry>>> recipeProviders = new LinkedHashSet<>();
        for (DynamicDataConfig ddConfig : ddConfigs.values()) {
            for (Module module : ddConfig.getModules()) {
                if (ddConfig.isModuleEnabled(module.getModuleId())) {
                    recipesToRemove.addAll(module.gatherRecipesToRemove());
                    recipesToAdd.addAll(module.gatherRecipesToAdd());
                    BiPredicate<ResourceLocation, Recipe<?>> excluder = module.gatherRecipeExcluder();
                    if (excluder != null)
                        recipeExcluders.add(excluder);
                    Supplier<Set<DynamicRecipeEntry>> provider = module.gatherRecipeProvider();
                    if (provider != null)
                        recipeProviders.add(provider);
                }
            }
        }

        IRecipeManagerExtensions m = (IRecipeManagerExtensions) manager;
        ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> byTypeBuilder = ImmutableMultimap.builder();
        ImmutableMap.Builder<ResourceLocation, RecipeHolder<?>> byNameBuilder = ImmutableMap.builder();

        Set<ResourceLocation> locationsRemoved = new HashSet<>();
        Set<ResourceLocation> currentLocations = new HashSet<>();
        LOOP_REM:
        for (Map.Entry<ResourceLocation, RecipeHolder<?>> entry : m.getByName().entrySet()) {
            ResourceLocation location = entry.getKey();
            if (recipesToRemove.contains(location))
                locationsRemoved.add(location);
            else {
                RecipeHolder<?> value = entry.getValue();
                Recipe<?> recipe = value.value();
                for (BiPredicate<ResourceLocation, Recipe<?>> p : recipeExcluders) {
                    if (p.test(location, recipe)) {
                        locationsRemoved.add(location);
                        continue LOOP_REM;
                    }
                }
                byTypeBuilder.put(recipe.getType(), value);
                byNameBuilder.put(location, value);
                currentLocations.add(location);
            }
        }

        Set<ResourceLocation> locationsAdded = new HashSet<>();

        for (DynamicRecipeEntry entry : recipesToAdd) {
            ResourceLocation location = entry.getId();
            if (currentLocations.contains(location)) {
                ModMainUtils.broadcastMessage(
                    MESSAGE_FAILED_RECIPES.toComponentReplacingEmpty(
                        MESSAGE_DUPLICATE_RECIPE.toComponentReplacingEmpty(Component.literal(location.toString()))
                    ),
                    LOGGER::error
                );
                return;
            } else {
                RecipeHolder<?> value = entry.getValue();
                byTypeBuilder.put(entry.getType(), value);
                byNameBuilder.put(location, value);
                locationsAdded.add(location);
            }
        }

        for (Supplier<Set<DynamicRecipeEntry>> provider : recipeProviders) {
            for (DynamicRecipeEntry entry : provider.get()) {
                ResourceLocation location = entry.getId();
                if (currentLocations.contains(location)) {
                    ModMainUtils.broadcastMessage(
                        MESSAGE_FAILED_RECIPES.toComponentReplacingEmpty(
                            MESSAGE_DUPLICATE_RECIPE.toComponentReplacingEmpty(
                                Component.literal(location.toString())
                            )
                        ),
                        LOGGER::error
                    );
                    return;
                } else {
                    RecipeHolder<?> value = entry.getValue();
                    byTypeBuilder.put(entry.getType(), value);
                    byNameBuilder.put(location, value);
                    locationsAdded.add(location);
                }
            }
        }

        m.setByType(byTypeBuilder.build());
        m.setByName(byNameBuilder.build());
        ModMainUtils.broadcastMessage(
            MESSAGE_SUCCESS_RECIPES.toComponentReplacingEmpty(
                Component.literal(String.valueOf(locationsRemoved.size())),
                Component.literal(String.valueOf(locationsAdded.size())),
                Component.literal(String.valueOf(System.currentTimeMillis() - t1))
            ),
            LOGGER::info
        );
    }

    public static void injectTags(TagManager manager) {
        long t1 = System.currentTimeMillis();
        tagManager = (ITagManagerExtensions) manager;

        Set<DynamicTagEntry<?>> tagsToAdd = new LinkedHashSet<>();
        Set<DynamicTagEntry<?>> tagsToRemove = new LinkedHashSet<>();
        for (DynamicDataConfig ddConfig : ddConfigs.values()) {
            for (Module module : ddConfig.getModules()) {
                if (ddConfig.isModuleEnabled(module.getModuleId())) {
                    tagsToAdd.addAll(module.gatherTagsToAdd());
                    tagsToRemove.addAll(module.gatherTagsToRemove());
                }
            }
        }

        Set<ResourceKey<? extends Registry<?>>> allResourceKeys = new HashSet<>();
        Map<ResourceKey<? extends Registry<?>>, Map<ResourceLocation, Set<? extends Holder<?>>>> allTagsToAddInRegistry =
            new LinkedHashMap<>();
        Map<ResourceKey<? extends Registry<?>>, Map<ResourceLocation, Set<? extends Holder<?>>>> allTagsToRemoveInRegistry =
            new LinkedHashMap<>();

        for (DynamicTagEntry<?> entry : tagsToAdd) {
            TagKey<?> key = entry.getTag();
            ResourceKey<? extends Registry<?>> registry = key.registry();
            allResourceKeys.add(registry);
            allTagsToAddInRegistry.putIfAbsent(registry, new LinkedHashMap<>());
            allTagsToAddInRegistry.get(registry).put(key.location(), entry.getHolders());
        }

        for (DynamicTagEntry<?> entry : tagsToRemove) {
            TagKey<?> key = entry.getTag();
            ResourceKey<? extends Registry<?>> resourceKey = key.registry();
            allResourceKeys.add(resourceKey);
            allTagsToRemoveInRegistry.putIfAbsent(resourceKey, new LinkedHashMap<>());
            allTagsToRemoveInRegistry.get(resourceKey).put(key.location(), entry.getHolders());
        }

        ITagManagerExtensions m = (ITagManagerExtensions) manager;
        List<LoadResult<?>> newResults = new LinkedList<>();
        int nModified = 0, nAdded = 0;
        for (var result : m.getResults()) {
            ResourceKey<? extends Registry<?>> resourceKey = result.key();
            if (allResourceKeys.contains(resourceKey)) {
                Map<ResourceLocation, Set<? extends Holder<?>>> tagsToAddInRegistry = allTagsToAddInRegistry
                    .getOrDefault(resourceKey, Map.of());
                Map<ResourceLocation, Set<? extends Holder<?>>> tagsToRemoveInRegistry = allTagsToRemoveInRegistry
                    .getOrDefault(resourceKey, Map.of());

                InjectTagsResultContext<?> context = InjectTagsResultContext.injectTagsByRegistry(
                    result,
                    tagsToAddInRegistry,
                    tagsToRemoveInRegistry
                );
                newResults.add(context.result());
                nModified += context.nAdded();
                nAdded += context.nAdded();
            } else newResults.add(result);
        }

        m.setResults(newResults);
        ModMainUtils.broadcastMessage(
            MESSAGE_SUCCESS_TAGS.toComponentReplacingEmpty(
                Component.literal(String.valueOf(nModified)),
                Component.literal(String.valueOf(nAdded)),
                Component.literal(String.valueOf(System.currentTimeMillis() - t1))
            ),
            LOGGER::info
        );
    }

    private record InjectTagsResultContext<T>(LoadResult<T> result, int nModified, int nAdded) {
        public static <T> InjectTagsResultContext<T> create(
            ResourceKey<? extends Registry<T>> keys,
            Map<ResourceLocation, Set<Holder<T>>> tags,
            Set<?> modified,
            Set<?> added
        ) {
            return new InjectTagsResultContext<>(
                new LoadResult<>(keys, new LinkedHashMap<>(tags)),
                modified.size(),
                added.size()
            );
        }

        public static <T> InjectTagsResultContext<T> injectTagsByRegistry(
            LoadResult<?> originalResult_,
            Map<ResourceLocation, Set<? extends Holder<?>>> tagsToAdd_,
            Map<ResourceLocation, Set<? extends Holder<?>>> tagsToRemove_
        ) {
            @SuppressWarnings("unchecked") LoadResult<T> originalResult = (LoadResult<T>) originalResult_;
            @SuppressWarnings("unchecked") Map<ResourceLocation, Set<Holder<T>>> tagsToAdd =
                (Map<ResourceLocation, Set<Holder<T>>>) (Map<?, ?>) tagsToAdd_;
            @SuppressWarnings("unchecked") Map<ResourceLocation, Set<Holder<T>>> tagsToRemove =
                (Map<ResourceLocation, Set<Holder<T>>>) (Map<?, ?>) tagsToRemove_;

            Map<ResourceLocation, Set<Holder<T>>> newTags = new LinkedHashMap<>();

            Set<ResourceLocation> locationsModified = new HashSet<>();
            for (Map.Entry<ResourceLocation, Collection<Holder<T>>> originalEntry : originalResult.tags().entrySet()) {
                ResourceLocation location = originalEntry.getKey();
                Set<Holder<T>> originalHolders = new LinkedHashSet<>(originalEntry.getValue());
                if (tagsToRemove.containsKey(location)) {
                    Set<Holder<T>> holdersToRemoved = tagsToRemove.get(location);
                    locationsModified.add(location);
                    if (holdersToRemoved.isEmpty())
                        newTags.put(location, new LinkedHashSet<>());
                    else
                        newTags.put(location, new LinkedHashSet<>(Sets.difference(originalHolders, holdersToRemoved)));
                } else newTags.put(location, originalHolders);
            }

            Set<ResourceLocation> locationsAdded = new HashSet<>();
            for (Map.Entry<ResourceLocation, Set<Holder<T>>> entryToAdd : tagsToAdd.entrySet()) {
                Set<Holder<T>> holdersToAdd = entryToAdd.getValue();
                if (!holdersToAdd.isEmpty()) {
                    ResourceLocation location = entryToAdd.getKey();
                    if (newTags.containsKey(location))
                        locationsModified.add(location);
                    else {
                        newTags.put(location, new LinkedHashSet<>());
                        locationsAdded.add(location);
                    }
                    newTags.get(location).addAll(holdersToAdd);
                }
            }

            return create(originalResult.key(), newTags, locationsModified, locationsAdded);
        }
    }
}
