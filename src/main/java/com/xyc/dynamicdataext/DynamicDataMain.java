package com.xyc.dynamicdataext;

import com.xyc.dynamicdataext.lang.TranslatableLang;
import com.xyc.dynamicdataext.modules.*;
import com.xyc.dynamicdataext.utils.ModMainUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(DynamicDataMain.MOD_ID)
public class DynamicDataMain {
    public static final String MOD_ID = "dynamicdataext";

    public static DynamicDataConfig CONFIG;

    public DynamicDataMain(IEventBus modEventBus, ModContainer container) {
        TranslatableLang title = ModMainUtils.createModTitleBuilder(MOD_ID)
                                             .translation("zh_cn", "动态数据扩展")
                                             .translation("en_us", "Dynamic Data Extensions")
                                             .build();
        CONFIG = new DynamicDataConfig(
            MOD_ID,
            title,
            modEventBus,
            container,
            LeatherFromRottenFlesh.INSTANCE,
            RawOreBlockSmelting.INSTANCE,
            WoolToString.INSTANCE,
            ConvenientCrafting.INSTANCE,
            AllStones.INSTANCE,
            SlabBonding.INSTANCE,
            TrapdoorBalance.INSTANCE,
            ReversibleCutting.INSTANCE,
            GlassCutting.INSTANCE,
            WoodCutting.INSTANCE,
            TagsTest.INSTANCE,
            Common.INSTANCE
        );
        DynamicDataRegistry.registerConfig(CONFIG);
    }
}
