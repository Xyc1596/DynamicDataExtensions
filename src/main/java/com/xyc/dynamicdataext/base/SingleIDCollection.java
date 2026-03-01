package com.xyc.dynamicdataext.base;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class SingleIDCollection {
    protected static final Logger LOGGER = LogUtils.getLogger();
    protected final Set<ResourceLocation> singles = new HashSet<>();
    protected final Set<Pattern> patterns = new HashSet<>();

    protected SingleIDCollection() {
    }

    public void addAll(Iterable<String> ids) {
        for (String id : ids)
            this.add(id);
    }

    public void add(String id) {
        if (id.startsWith("@")) {
            String id_ = id.substring(1);
            if (!id_.startsWith("#"))
                try {
                    this.patterns.add(Pattern.compile(id_));
                } catch (Exception e) {
                    LOGGER.warn(e.getLocalizedMessage());
                }
        } else if (!id.startsWith("#")) {
            ResourceLocation location = ResourceLocation.tryParse(id);
            if (location != null)
                this.singles.add(location);
        }
    }

    public boolean isEmpty() {
        return this.singles.isEmpty() && this.patterns.isEmpty();
    }

    public boolean contains(ResourceLocation location) {
        if (!this.isEmpty()) {
            if (this.singles.contains(location))
                return true;
            else
                for (Pattern pattern : patterns)
                    if (pattern.matcher(location.toString()).matches())
                        return true;
        }
        return false;
    }

    public boolean test(ResourceLocation location, boolean whitelist) {
        return this.contains(location) == whitelist;
    }

    public static SingleIDCollection of(Iterable<String> ids) {
        SingleIDCollection instance = new SingleIDCollection();
        instance.addAll(ids);
        return instance;
    }
}
