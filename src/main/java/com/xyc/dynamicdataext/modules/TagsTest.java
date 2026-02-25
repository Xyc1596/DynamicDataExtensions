package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.DynamicDataMain;
import com.xyc.dynamicdataext.base.DynamicRecipeEntry;
import com.xyc.dynamicdataext.base.DynamicTagEntry;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.utils.ContentUtils;
import com.xyc.dynamicdataext.utils.CriterionUtils;
import com.xyc.dynamicdataext.utils.LocationUtils;
import com.xyc.dynamicdataext.utils.RecipeUtils;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class TagsTest extends Module {
    public TagsTest() {
        super(DynamicDataMain.MOD_ID, "tags_test");
    }

    private static final TagKey<Item> TEST_TAG = LocationUtils.createItemTag(
        LocationUtils.fromNamespaceAndPath(DynamicDataMain.MOD_ID, "test_tag")
    );

    @Override
    public @NotNull Set<DynamicTagEntry<?>> gatherTagsToAdd() {
        return Set.of(DynamicTagEntry.itemTag(TEST_TAG, Set.of(ContentUtils.getItemHolder(Items.ROTTEN_FLESH))));
    }

    @Override
    public @NotNull Set<DynamicRecipeEntry> gatherRecipesToAdd() {
        return Set.of(
            RecipeUtils.createRecipeEntry(
                this,
                "test_tag_recipe",
                ShapelessRecipeBuilder
                    .shapeless(RecipeCategory.MISC, Items.ROTTEN_FLESH, 3)
                    .requires(Ingredient.of(TEST_TAG), 3)
                    .unlockedBy("has_rotten_flesh", CriterionUtils.hasItems(Items.ROTTEN_FLESH))
            )
        );
    }
}
