package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.DynamicDataMain;
import com.xyc.dynamicdataext.DynamicDataRegistry;
import com.xyc.dynamicdataext.base.DynamicRecipeEntry;
import com.xyc.dynamicdataext.base.IRecipeManagerExtensions;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.config.ModuleConfig;
import com.xyc.dynamicdataext.config.ModuleConfigBuilder;
import com.xyc.dynamicdataext.config.ModuleOptionBuilder;
import com.xyc.dynamicdataext.utils.*;
import net.minecraft.ChatFormatting;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class TrapdoorBalance extends Module {
    protected Set<DynamicRecipeEntry> newRecipes = new LinkedHashSet<>();

    public TrapdoorBalance() {
        super(DynamicDataMain.MOD_ID, "trapdoor_balance");
    }

    @Override
    public BiPredicate<ResourceLocation, Recipe<?>> gatherRecipeExcluder() {
        this.newRecipes.clear();
        IRecipeManagerExtensions manager = DynamicDataRegistry.getRecipeManager();

        return (location, recipe) -> {
            if (recipe instanceof ShapedRecipe shapedRecipe) {
                ItemStack resultStack = manager.getResultItemStack(shapedRecipe);
                if (resultStack.is(ItemTags.TRAPDOORS)) {
                    ShapedRecipePattern pattern = shapedRecipe.pattern;
                    int h = pattern.height(), w = pattern.width(), r = resultStack.getCount();
                    if (h == 2)
                        if (w == 3 && r == 2)
                            return this.processOriginalRecipe(pattern, resultStack, TrapdoorBalance::trapdoor6Builder);
                        else if (w == 2 && r == 1)
                            return this.processOriginalRecipe(pattern, resultStack, TrapdoorBalance::trapdoor4Builder);
                }
            }
            return false;
        };
    }

    @Override
    public @NotNull Supplier<Set<DynamicRecipeEntry>> gatherRecipeProvider() {
        return () -> this.newRecipes;
    }

    @Override
    protected @NotNull ModuleConfig buildConfig() {
        ModuleConfigBuilder builder = this.createConfigBuilder();
        ModuleOptionBuilder<Boolean> enabled = ConfigUtils.createEnabledOptionBuilderWithTitle(builder);
        return builder.setTitle(builder
            .getTitleLangBuilder()
            .translation("zh_cn", "活板门配方平衡")
            .translation("en_us", "Trapdoor Recipe Balance")
            .build()
        ).defineEnabled(enabled
            .setTooltip(enabled
                .getTooltipLangBuilder()
                .translation("zh_cn", "增加活板门合成配方的产物数量\n%s")
                .translation("en_us", "Increase the result count of the crafting recipe of trapdoors\n%s")
                .child(enabled
                    .createTooltipChildLangBuilder()
                    .translation("zh_cn", "为什么6块木板能合成3个门却只能合成2个活板门？")
                    .translation("en_us", "Why can 6 planks craft 3 doors but only 2 trapdoors?")
                    .format(ChatFormatting.ITALIC)
                    .build()
                ).build()
            ).build()
        ).build();
    }

    protected boolean processOriginalRecipe(
        ShapedRecipePattern pattern,
        ItemStack resultStack,
        BiFunction<ItemLike, Ingredient, RecipeBuilder> factory
    ) {
        Set<Ingredient> ingredients = Set.copyOf(pattern.ingredients());
        Item result = resultStack.getItem();
        String resultName = RegistryUtils.getItemId(result);
        if (ingredients.size() == 1) {
            this.newRecipes.add(RecipeUtils.createRecipeEntry(
                this,
                resultName,
                factory.apply(result, ingredients.toArray(new Ingredient[1])[0])
            ));
            return true;
        } else return false;
    }

    protected static RecipeBuilder trapdoor6Builder(ItemLike trapdoor, Ingredient material) {
        return ShapedRecipeBuilder
            .shaped(RecipeCategory.REDSTONE, trapdoor, 6)
            .define('#', material).pattern("###").pattern("###")
            .unlockedBy("has_material", CriterionUtils.hasItems(material.getItems()[0].getItem()));
    }

    protected static RecipeBuilder trapdoor4Builder(ItemLike trapdoor, Ingredient material) {
        return ShapedRecipeBuilder
            .shaped(RecipeCategory.REDSTONE, trapdoor, 4)
            .define('#', material).pattern("##").pattern("##")
            .unlockedBy("has_material", CriterionUtils.hasItems(material.getItems()[0].getItem()));
    }
}
