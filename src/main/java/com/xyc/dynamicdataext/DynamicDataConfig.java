package com.xyc.dynamicdataext;

import com.xyc.dynamicdataext.base.Module;
import com.xyc.dynamicdataext.lang.ModuleLang;
import com.xyc.dynamicdataext.lang.ModuleLangBuilder;
import com.xyc.dynamicdataext.lang.PlaceholderLang;
import com.xyc.dynamicdataext.lang.TranslatableLang;
import com.xyc.dynamicdataext.utils.ModMainUtils;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class DynamicDataConfig {
    protected final static TranslatableLang MESSAGE_RELOAD_CONFIG = ModuleLangBuilder
        .translatable("message", DynamicDataMain.MOD_ID, "reload_config")
        .translation("zh_cn", "[%s] 重新加载中！")
        .translation("en_us", "[%s] Reloading!")
        .child(PlaceholderLang.EMPTY)
        .build();
    protected final static TranslatableLang MESSAGE_AUTO_RELOAD_DISABLED = ModuleLangBuilder
        .translatable("message", DynamicDataMain.MOD_ID, "auto_reload_disabled")
        .translation(
            "zh_cn",
            "[%s] 自动重新加载已禁用！使用 /reload 命令使模块设置生效。"
        )
        .translation(
            "en_us",
            "[%s] Auto reloading is disabled! Use /reload for the module settings to take effect."
        )
        .child(PlaceholderLang.EMPTY)
        .build();
    protected final static TranslatableLang MESSAGE_NO_PERMISSION = ModuleLangBuilder
        .translatable("message", DynamicDataMain.MOD_ID, "no_permission")
        .format(ChatFormatting.RED)
        .translation(
            "zh_cn",
            "[%s] 你没有更新服务端配置的权限！配置变更已保存到本地但不会同步到服务端。"
        )
        .translation(
            "en_us",
            "[%s] You have no permission to update the server configs! Your changes have been saved locally " +
                "but will not be synchronized to the server."
        )
        .child(PlaceholderLang.EMPTY)
        .build();

    private IConfigSpec.ILoadedConfig configCache;
    private final Map<String, Module> moduleMap = new LinkedHashMap<>();
    protected final String namespace;
    protected final MutableComponent titleComponent;
    protected final Logger logger;
    public final ModConfigSpec COMMON;

    public String getNamespace() {
        return namespace;
    }

    public Collection<Module> getModules() {
        return moduleMap.values();
    }

    public Module getModule(String moduleId) {
        return this.moduleMap.get(moduleId);
    }

    public final boolean isModuleEnabled(String moduleId) {
        return this.moduleMap.get(moduleId).getConfig().isEnabled();
    }

    public DynamicDataConfig(
        String namespace,
        ModuleLang title,
        IEventBus modEventBus,
        ModContainer container,
        Module... modules
    ) {
        this.titleComponent = title.toComponent().withStyle(ChatFormatting.DARK_AQUA).withStyle(ChatFormatting.BOLD);
        this.namespace = namespace;
        for (Module m : modules) {
            String moduleId = m.getModuleId();
            if (this.moduleMap.containsKey(moduleId))
                throw new IllegalStateException("Duplicate module id " + moduleId);
            this.moduleMap.put(moduleId, m);
        }

        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        for (Module module : this.getModules()) {
            module.getConfig().buildSpec(builder);
            moduleMap.put(module.getModuleId(), module);
        }

        this.COMMON = builder.build();
        container.registerConfig(ModConfig.Type.COMMON, this.COMMON);

        modEventBus.addListener(this::handleLoadConfig);
        modEventBus.addListener(this::handleReloadConfig);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            container.registerExtensionPoint(
                IConfigScreenFactory.class,
                (c, s) -> this.getClothBuilder(title).setParentScreen(s).build()
            );
        }
        this.logger = LoggerFactory.getLogger(this.getClass().getName() + this.namespace);
    }

    public void handleLoadConfig(final ModConfigEvent.Loading event) {
        IConfigSpec.ILoadedConfig loadedConfig = event.getConfig().getLoadedConfig();
        if (loadedConfig != null) {
            this.configCache = loadedConfig;
        }
    }

    public void handleReloadConfig(final ModConfigEvent.Reloading event) {
        IConfigSpec.ILoadedConfig loadedConfig = event.getConfig().getLoadedConfig();
        if (loadedConfig == null || loadedConfig.equals(this.configCache))
            return;

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null)
            return;
        PlayerList playerList = server.getPlayerList();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            LocalPlayer localPlayer = Minecraft.getInstance().player;
            if (localPlayer == null)
                return;

            ServerPlayer player = playerList.getPlayer(localPlayer.getUUID());
            if (player == null)
                return;

            if (!playerList.isOp(player.getGameProfile())) {
                player.sendSystemMessage(
                    MESSAGE_NO_PERMISSION.toComponentReplacingPlaceholders(this.titleComponent)
                );
                return;
            }
        }

        if (DynamicDataRegistry.autoReloading()) {
            server.reloadResources(server.getPackRepository().getSelectedIds());
            ModMainUtils.broadcastMessage(
                MESSAGE_RELOAD_CONFIG.toComponentReplacingPlaceholders(this.titleComponent),
                this.logger::info
            );
        } else {
            ModMainUtils.broadcastMessage(
                MESSAGE_AUTO_RELOAD_DISABLED.toComponentReplacingPlaceholders(this.titleComponent),
                true,
                this.logger::warn
            );
        }
        this.configCache = loadedConfig;
    }

    public static Set<TranslatableLang> gatherAllMessageLang() {
        return Set.of(MESSAGE_RELOAD_CONFIG, MESSAGE_AUTO_RELOAD_DISABLED, MESSAGE_NO_PERMISSION);
    }

    protected ConfigBuilder getClothBuilder(ModuleLang title) {
        ConfigBuilder builder = ConfigBuilder.create()
                                             .setTitle(title.toComponent())
                                             .setSavingRunnable(this.COMMON::save);
        builder.setGlobalized(true);
        builder.setGlobalizedExpanded(true);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        for (Module module : this.getModules()) {
            module.getConfig().buildCloth(builder, entryBuilder);
        }
        return builder;
    }
}
