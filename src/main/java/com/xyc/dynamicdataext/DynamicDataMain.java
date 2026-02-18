package com.xyc.dynamicdataext;

import com.xyc.dynamicdataext.lang.LanguageProviderWrapper;
import com.xyc.dynamicdataext.lang.TranslatableLang;
import com.xyc.dynamicdataext.modules.*;
import com.xyc.dynamicdataext.utils.ModMainUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;

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
            List.of(
                new LeatherFromRottenFlesh(),
                new RawOreBlockSmelting(),
                new WoolToString(),
                new ConvenientCrafting(),
                new AllStones(),
                new SlabRecycling(),
                new TagsTest(),
                new Common()
            ),
            modEventBus,
            container
        );
        DynamicDataRegistry.registerConfig(CONFIG);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            // DataGen
            modEventBus.addListener((final GatherDataEvent event) -> {
                DataGenerator generator = event.getGenerator();
                PackOutput output = generator.getPackOutput();

                // zh_cn
                generator.addProvider(
                    event.includeClient(),
                    new LanguageProviderWrapper(output, MOD_ID, "zh_cn") {
                        @Override
                        protected void addTranslations() {
                            this.addModuleLangTranslations(title);
                            CONFIG.getModules().forEach(this::addModuleTranslations);
                            DynamicDataConfig.gatherAllMessageLang().forEach(this::addModuleLangTranslations);
                        }
                    }
                );

                // en_us
                generator.addProvider(
                    event.includeClient(),
                    new LanguageProviderWrapper(output, MOD_ID, "en_us") {
                        @Override
                        protected void addTranslations() {
                            this.addModuleLangTranslations(title);
                            CONFIG.getModules().forEach(this::addModuleTranslations);
                            DynamicDataConfig.gatherAllMessageLang().forEach(this::addModuleLangTranslations);
                        }
                    }
                );
            });
        }
    }
}
