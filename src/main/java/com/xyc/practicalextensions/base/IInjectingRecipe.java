package com.xyc.practicalextensions.base;

import org.apache.commons.lang3.tuple.Triple;

public interface IInjectingRecipe {
    /**
     * @see net.minecraft.server.packs.resources.SimplePreparableReloadListener<>#apply(Object, ResourceManager,
     * ProfilerFiller)
     */
    Triple<Long, Long, Long> practicalextensions$injectRecipes();
}
