package com.xyc.practicalextensions;

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

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mod(ModMain.MOD_ID)
public class ModMain {
    public static final String MOD_ID = "practicalextensions";

    public ModMain(IEventBus modEventBus, ModContainer container) {
        ModuleConfig config = new ModuleConfig(
            modEventBus,
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
                            add(ModuleConfig.MESSAGE_RELOAD_CONFIG, "[%s] 重新加载中！");
                            add(
                                ModuleConfig.MESSAGE_AUTO_RELOAD_DISABLED,
                                "[%s] 自动重新加载已禁用！使用 /reload 命令使模块设置生效。"
                            );
                            clothConfig.ifPresent(c -> {
                                add(c.TITLE_CONFIG, "实用配方扩展");
                                add(ModuleClothConfig.TITLE_MODULES, "模块设置");
                                add(ModuleClothConfig.TITLE_GENERAL, "通用设置");
                                add(ModuleClothConfig.OPTION_AUTO_RELOAD, "自动重新加载");
                                add(
                                    ModuleClothConfig.TOOLTIP_AUTO_RELOAD,
                                    "更改配置后自动重新加载数据\n禁用该选项则需要手动使用 /reload 命令使模块设置生效"
                                );
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
                            add(ModuleConfig.MESSAGE_RELOAD_CONFIG, "[%s] Reloading!");
                            add(
                                ModuleConfig.MESSAGE_AUTO_RELOAD_DISABLED,
                                "[%s] Auto reloading is disabled! Use /reload for the module settings to take effect."
                            );
                            clothConfig.ifPresent(c -> {
                                add(c.TITLE_CONFIG, "Practical Extensions");
                                add(ModuleClothConfig.TITLE_MODULES, "Module Settings");
                                add(ModuleClothConfig.TITLE_GENERAL, "General Settings");
                                add(ModuleClothConfig.OPTION_AUTO_RELOAD, "Auto Reloading");
                                add(
                                    ModuleClothConfig.TOOLTIP_AUTO_RELOAD,
                                    "Automatically reload data after changing config.\n" +
                                        "If disabled, you have to use /reload manually " +
                                        "for the module configs to take effect."
                                );
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
