package com.xyc.practicalextensions;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = ModMain.MOD_ID, bus= EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue LEATHER_FROM_ROTTEN_FLESH = COMMON_BUILDER
        .define("leather_from_rotten_flesh", true);
    public static boolean leatherFromRottenFlesh;

    static final ModConfigSpec COMMON = COMMON_BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        leatherFromRottenFlesh = LEATHER_FROM_ROTTEN_FLESH.get();
    }
}
