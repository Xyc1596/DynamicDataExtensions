package com.xyc.dynamicdataext.modules;

import com.xyc.dynamicdataext.DynamicDataMain;
import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.config.ModuleConfig;
import com.xyc.dynamicdataext.config.ModuleOptionBuilder;
import com.xyc.dynamicdataext.lang.TranslatableLang;
import net.minecraft.ChatFormatting;
import org.jetbrains.annotations.NotNull;

public class Common extends Module {
    public static final Common INSTANCE = new Common(DynamicDataMain.MOD_ID, "common");
    public static final TranslatableLang TITLE = INSTANCE.configBuilder
        .getTitleLangBuilder()
        .translation("zh_cn", "通用选项")
        .translation("en_us", "Common Options")
        .format(ChatFormatting.GOLD, ChatFormatting.BOLD)
        .build();

    protected Common(String namespace, String moduleId) {
        super(namespace, moduleId);
    }

    @Override
    protected @NotNull ModuleConfig buildConfig() {
        ModuleOptionBuilder<Boolean> autoReload = this.configBuilder.createBooleanOptionBuilder("auto_reload");
        return this.configBuilder
            .setTitle(TITLE)
            .defineOption(autoReload
                .setDefaultValue(true)
                .setTitle(autoReload
                    .getTitleLangBuilder()
                    .translation("zh_cn", "自动重新加载")
                    .translation("en_us", "Auto Reloading")
                    .build()
                )
                .setTooltip(autoReload
                    .getTooltipLangBuilder()
                    .translation(
                        "zh_cn",
                        "更改配置后自动重新加载数据\n禁用该选项则需要手动使用 /reload 命令使模块设置生效"
                    )
                    .translation(
                        "en_us",
                        "Automatically reload data after changing config.\n" +
                            "If disabled, you have to use /reload manually for the module configs to take effect."
                    ).build()
                ).build()
            ).build();
    }
}
