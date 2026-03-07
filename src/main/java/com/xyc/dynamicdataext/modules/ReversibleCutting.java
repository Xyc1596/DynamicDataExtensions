package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.DynamicDataMain;
import com.xyc.dynamicdataext.DynamicDataRegistry;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.base.*;
import com.xyc.dynamicdataext.config.ModuleConfig;
import com.xyc.dynamicdataext.config.ModuleOption;
import com.xyc.dynamicdataext.config.ModuleOptionBuilder;
import com.xyc.dynamicdataext.lang.PlaceholderLang;
import com.xyc.dynamicdataext.lang.TranslatableLang;
import com.xyc.dynamicdataext.utils.ConfigUtils;
import com.xyc.dynamicdataext.utils.ListMode;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class ReversibleCutting extends Module {
    public static final ReversibleCutting INSTANCE = new ReversibleCutting(
        DynamicDataMain.MOD_ID, "reversible_cutting"
    );
    public static final TranslatableLang TITLE = INSTANCE.configBuilder
        .getTitleLangBuilder()
        .translation("zh_cn", "可逆切石")
        .translation("en_us", "Reversible Cutting")
        .build();

    protected final SingleItemRecipeGraph graph = new SingleItemRecipeGraph();
    protected ModuleOption<List<String>> recipeList;
    protected ModuleOption<ListMode> recipeListMode;

    protected ReversibleCutting(String namespace, String moduleId) {
        super(namespace, moduleId);
    }

    @Override
    public BiPredicate<ResourceLocation, Recipe<?>> gatherRecipeExcluder() {
        IRecipeManagerExtensions manager = DynamicDataRegistry.getRecipeManager();
        this.graph.clear();
        SingleIDCollection collection = SingleIDCollection.of(this.recipeList.getValue());
        boolean whitelist = this.recipeListMode.getValue() == ListMode.WHITELIST;
        return (location, recipe) -> {
            if (recipe instanceof StonecutterRecipe r && collection.test(location, whitelist)) {
                ItemStack resultStack = manager.getResultItemStack(r);
                for (Ingredient ingredient : r.getIngredients())
                    for (ItemStack itemStack : ingredient.getItems())
                        this.graph.addConnection(itemStack.getItem(), resultStack);
                return true;
            }
            return false;
        };
    }

    @Override
    public @Nullable Supplier<Set<DynamicRecipeEntry>> gatherRecipeProvider() {
        return () -> this.graph.createAllRecipes(this, true);
    }

    @Override
    protected @NotNull ModuleConfig buildConfig() {
        ModuleOptionBuilder<Boolean> enabled = ConfigUtils.createEnabledOptionBuilderWithTitle(this.configBuilder);

        ModuleOptionBuilder<List<String>> recipeList = this.configBuilder
            .createStringListOptionBuilder("recipe_list")
            .setDefaultValue(List.of("@create:.*"))
            .setTooltip(ConfigUtils.DEFAULT_SINGLE_FORMAT_INSTRUCTION);
        TranslatableLang recipeListTitle = recipeList
            .getTitleLangBuilder()
            .translation("zh_cn", "配方列表")
            .translation("en_us", "Recipe List")
            .build();
        PlaceholderLang recipeListTitlePlaceholder = recipeListTitle.getPlaceholder(ChatFormatting.WHITE);
        this.recipeList = recipeList.setTitle(recipeListTitle).build();

        ModuleOptionBuilder<ListMode> recipeListMode = ConfigUtils.createListModeOptionBuilderWithTitle(
            this.configBuilder, this.recipeList, ListMode.BLACKLIST
        );
        this.recipeListMode = recipeListMode
            .setTooltip(recipeListMode
                .getTooltipLangBuilder()
                .translation(
                    "zh_cn",
                    """
                        %s
                        %s模式 - 如果切石配方的ID存在于【%s】/ 符合【%s】中的匹配条件，
                                 则不会根据该配方生成配方
                        %s模式 - 如果切石配方的ID不存在于【%s】/ 不符合【%s】中的匹配条件，
                                 则不会根据该配方生成配方"""
                ).translation(
                    "en_us",
                    """
                        %s
                        %s Mode: If a recipe's id exists in [%s] / matches any condition in [%s],
                                 new recipes based on it will not be generated
                        %s Mode: If a recipe's id does not exist in [%s] / match any condition in [%s],
                                 new recipes based on it will not be generated"""
                ).child(ConfigUtils.DEFAULT_LIST_MODE_TOOLTIP_HEAD)
                .child(ConfigUtils.DEFAULT_LIST_MODE_BLACKLIST)
                .child(recipeListTitlePlaceholder)
                .child(recipeListTitlePlaceholder)
                .child(ConfigUtils.DEFAULT_LIST_MODE_WHITELIST)
                .child(recipeListTitlePlaceholder)
                .child(recipeListTitlePlaceholder)
                .format(ChatFormatting.GRAY)
                .build()
            ).build();

        return this.configBuilder
            .setTitle(TITLE)
            .defineEnabled(enabled
                .setTooltip(enabled
                    .getTooltipLangBuilder()
                    .translation(
                        "zh_cn",
                        "部分切石配方的材料和产物可以任意相互转换"
                    ).translation(
                        "en_us",
                        "The ingredients and products in some stonecutting recipes can be mutually " +
                            "converted into each other arbitrarily"
                    ).build()
                ).build()
            ).defineOption(this.recipeList)
            .defineOption(this.recipeListMode)
            .build();
    }
}
