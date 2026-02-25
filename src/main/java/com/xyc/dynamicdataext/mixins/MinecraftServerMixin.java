package com.xyc.dynamicdataext.mixins;

import com.xyc.dynamicdataext.base.IReloadableServerResourcesExtensions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ReloadableServerResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    /**
     * 重新加载数据时触发
     */
    @ModifyVariable(
        method = "lambda$reloadResources$30",
        at = @At("HEAD"),
        argsOnly = true,
        remap = false
    )
    private MinecraftServer.ReloadableResources modifyReloadableResources(
        MinecraftServer.ReloadableResources p_335203_
    ) {
        ((IReloadableServerResourcesExtensions) p_335203_.managers()).dynamicdataext$injectData();
        return p_335203_;
    }

    /**
     * 加载世界时触发，只能加载配方，无法加载标签
     */
    @ModifyArg(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/MinecraftServer$ReloadableResources;" +
                "<init>(Lnet/minecraft/server/packs/resources/CloseableResourceManager;" +
                "Lnet/minecraft/server/ReloadableServerResources;)V"
        ),
        index = 1
    )
    private ReloadableServerResources modifyInitReloadableResources(
        ReloadableServerResources resources
    ) {
        ((IReloadableServerResourcesExtensions) resources).dynamicdataext$injectRecipes();
        return resources;
    }
}
