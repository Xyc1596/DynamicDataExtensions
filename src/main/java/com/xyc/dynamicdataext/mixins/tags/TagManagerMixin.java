package com.xyc.dynamicdataext.mixins.tags;

import com.xyc.dynamicdataext.base.ITagManagerExtensions;
import net.minecraft.tags.TagManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(TagManager.class)
public interface TagManagerMixin extends ITagManagerExtensions {
    @Override
    @Accessor("results")
    List<TagManager.LoadResult<?>> getResults();

    @Override
    @Accessor("results")
    void setResults(List<TagManager.LoadResult<?>> results);
}
