package com.xyc.practicalextensions.utils;

import org.apache.commons.lang3.tuple.Triple;

public interface IInjectingRecipe {
    /**
     * @see net.minecraft.server.packs.resources.SimplePreparableReloadListener<>#apply(Object, ResourceManager,
     * ProfilerFiller)
     */
    Triple<Long, Long, Long> practicalextensions$injectRecipes();
}
