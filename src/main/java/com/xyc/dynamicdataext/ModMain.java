package com.xyc.dynamicdataext;

import com.xyc.dynamicdataext.lang.LanguageProviderWrapper;
import com.xyc.dynamicdataext.modules.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Optional;

@Mod(ModMain.MOD_ID)
public class ModMain {
    public static final String MOD_ID = "dynamicdataext";

    public static ModuleConfig CONFIG;

    public ModMain(IEventBus modEventBus, ModContainer container) {
        CONFIG = new ModuleConfig(
            modEventBus,
            container,
            List.of(
                new LeatherFromRottenFlesh(),
                new RawOreBlockSmelting(),
                new WoolToString(),
                new ConvenientCrafting(),
                new AllStones(),
                new TagsTest()
            )
        );
        DynamicDataRegistry.registerConfig(CONFIG);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            Optional<ModuleClothConfig> clothConfig = ModList.get().isLoaded("cloth_config")
                ? Optional.of(new ModuleClothConfig(MOD_ID, container, CONFIG))
                : Optional.empty();

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
                            add(MOD_ID, "动态数据扩展");
                            add(ModuleConfig.MESSAGE_RELOAD_CONFIG, "[%s] 重新加载中！");
                            add(
                                ModuleConfig.MESSAGE_AUTO_RELOAD_DISABLED,
                                "[%s] 自动重新加载已禁用！使用 /reload 命令使模块设置生效。"
                            );
                            add(
                                ModuleConfig.MESSAGE_NO_PERMISSION,
                                "[%s] 你没有更新服务端配置的权限！配置变更已保存到本地但不会同步到服务端。"
                            );
                            clothConfig.ifPresent(c -> {
                                add(c.TITLE_CONFIG, "动态数据扩展");
                                add(ModuleClothConfig.TITLE_MODULES, "模块设置");
                                add(ModuleClothConfig.TITLE_GENERAL, "通用设置");
                                add(ModuleClothConfig.OPTION_AUTO_RELOAD, "自动重新加载");
                                add(
                                    ModuleClothConfig.TOOLTIP_AUTO_RELOAD,
                                    "更改配置后自动重新加载数据\n禁用该选项则需要手动使用 /reload 命令使模块设置生效"
                                );
                            });
                            CONFIG.getModules().forEach(this::addModuleTranslations);
                        }
                    }
                );

                // en_us
                generator.addProvider(
                    event.includeClient(),
                    new LanguageProviderWrapper(output, MOD_ID, "en_us") {
                        @Override
                        protected void addTranslations() {
                            add(MOD_ID, "Dynamic Data Extensions");
                            add(ModuleConfig.MESSAGE_RELOAD_CONFIG, "[%s] Reloading!");
                            add(
                                ModuleConfig.MESSAGE_AUTO_RELOAD_DISABLED,
                                "[%s] Auto reloading is disabled! Use /reload for the module settings to take effect."
                            );
                            add(
                                ModuleConfig.MESSAGE_NO_PERMISSION,
                                "[%s] You have no permission to update the server configs! Your changes have been " +
                                    "saved locally but will not be synchronized to the server."
                            );
                            clothConfig.ifPresent(c -> {
                                add(c.TITLE_CONFIG, "Dynamic Data Extensions");
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
                            CONFIG.getModules().forEach(this::addModuleTranslations);
                        }
                    }
                );
            });
        }
    }
}
