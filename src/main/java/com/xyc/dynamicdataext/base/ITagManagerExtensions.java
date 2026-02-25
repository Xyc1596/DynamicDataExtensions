package com.xyc.dynamicdataext.base;

import net.minecraft.tags.TagManager.LoadResult;

import java.util.List;

public interface ITagManagerExtensions {
    List<LoadResult<?>> getResults();

    void setResults(List<LoadResult<?>> results);
}
