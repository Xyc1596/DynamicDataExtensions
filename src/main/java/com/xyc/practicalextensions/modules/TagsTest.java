package com.xyc.practicalextensions.modules;

import com.xyc.practicalextensions.ModMain;
import com.xyc.practicalextensions.base.Module;
import com.xyc.practicalextensions.base.ModuleUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class TagsTest extends Module {
    public TagsTest() {
        super(ModMain.MOD_ID, "tags_test");
    }

    private static final TagKey<Item> TEST_TAG = TagKey.create(
        Registries.ITEM,
        ResourceLocation.fromNamespaceAndPath(ModMain.MOD_ID, "test_tag")
    );

    @Override
    public @NotNull Map<TagKey<?>, Set<Holder<?>>> gatherTagsToAdd() {
        return Map.of(
            TEST_TAG,
            Set.of(Items.ROTTEN_FLESH.getDefaultInstance().getItemHolder())
        );
    }

    @Override
    public @NotNull Set<RecipeHolder<Recipe<?>>> gatherRecipesToAdd() {
        return Set.of(
            ModuleUtils.createRecipeHolder(
                this.namespace,
                "test_tag_recipe",
                ShapelessRecipeBuilder.shapeless(
                    RecipeCategory.MISC,
                    Items.ROTTEN_FLESH,
                    3
                ).requires(Ingredient.of(TEST_TAG), 3)
            )
        );
    }
}
