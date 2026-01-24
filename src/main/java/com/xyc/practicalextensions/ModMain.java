package com.xyc.practicalextensions;

import com.mojang.logging.LogUtils;
import com.xyc.practicalextensions.client.ModuleClothConfig;
import com.xyc.practicalextensions.client.ModuleLang;
import com.xyc.practicalextensions.modules.Module;
import com.xyc.practicalextensions.modules.contents.LeatherFromRottenFlesh;
import com.xyc.practicalextensions.modules.contents.RawOreBlockSmelting;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;

import java.util.*;

@Mod(ModMain.MOD_ID)
public class ModMain {
    public static final String MOD_ID = "practicalextensions";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ModMain(IEventBus modEventBus, ModContainer container) {
        ModuleConfig config = new ModuleConfig(
            modEventBus,
            MOD_ID,
            container,
            List.of(
                new LeatherFromRottenFlesh(),
                new RawOreBlockSmelting()
            )
        );
        PracticalExtensionRegistry.registerConfig(config);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            Optional<ModuleClothConfig> clothConfig = ModList.get().isLoaded("cloth_config")
                ? Optional.of(new ModuleClothConfig(MOD_ID, container, config))
                : Optional.empty();

            // DataGen
            modEventBus.addListener((final GatherDataEvent event) -> {
                DataGenerator generator = event.getGenerator();
                PackOutput output = generator.getPackOutput();
                Map<Module, Map<String, ModuleLang>> allModuleLang = ModuleLang.getAllModuleLang(config.MODULES);

                // zh_cn
                generator.addProvider(
                    event.includeClient(),
                    new LanguageProvider(output, MOD_ID, "zh_cn") {
                        @Override
                        protected void addTranslations() {
                            add(MOD_ID, "实用配方扩展");
                            add(config.MESSAGE_RELOAD_CONFIG, "[%s] 重新加载中！");
                            clothConfig.ifPresent(c -> {
                                add(c.TITLE_CONFIG, "实用配方扩展");
                                add(c.TITLE_MODULES, "模块");
                            });
                            config.MODULES.forEach(module -> {
                                ModuleLang lang = allModuleLang.get(module).get("zh_cn");
                                lang.option().ifPresent(s -> add(lang.optionKey(), s));
                                lang.tooltip().ifPresent(s -> add(lang.tooltipKey(), s));
                            });
                        }
                    }
                );

                // en_us
                generator.addProvider(
                    event.includeClient(),
                    new LanguageProvider(output, MOD_ID, "en_us") {
                        @Override
                        protected void addTranslations() {
                            add(MOD_ID, "Practical Extensions");
                            add(config.MESSAGE_RELOAD_CONFIG, "[%s] Reloading!");
                            clothConfig.ifPresent(c -> {
                                add(c.TITLE_CONFIG, "Practical Extensions");
                                add(c.TITLE_MODULES, "Modules");
                            });
                            config.MODULES.forEach(module -> {
                                ModuleLang lang = allModuleLang.get(module).get("en_us");
                                lang.option().ifPresent(s -> add(lang.optionKey(), s));
                                lang.tooltip().ifPresent(s -> add(lang.tooltipKey(), s));
                            });
                        }
                    }
                );
            });
        }
    }
}
