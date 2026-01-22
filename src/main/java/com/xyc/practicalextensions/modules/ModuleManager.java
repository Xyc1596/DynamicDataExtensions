package com.xyc.practicalextensions.modules;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ModuleManager {
    protected static final Map<ResourceLocation, IModule> MODULES = new HashMap<>();

    public static void register(IModule m) {
        MODULES.put(m.getResourceLocation(), m);
    }
}
