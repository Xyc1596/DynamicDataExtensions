package com.xyc.practicalextensions.modules;

import com.xyc.practicalextensions.ModMain;
import com.xyc.practicalextensions.base.Module;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class TagsTest extends Module {
    public TagsTest() {
        super(ModMain.MOD_ID, "tags_test");
    }

    @Override
    public @NotNull Map<TagKey<?>, Set<Holder<?>>> gatherTagsToAdd() {
        return Map.of(
            TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace("pig_food")),
            Set.of(Items.ROTTEN_FLESH.getDefaultInstance().getItemHolder())
        );
    }
}
