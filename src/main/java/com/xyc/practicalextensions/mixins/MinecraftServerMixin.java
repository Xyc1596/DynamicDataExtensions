package com.xyc.practicalextensions.mixins;

import com.xyc.practicalextensions.base.IInjectingReloadableServerResources;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @ModifyVariable(
        method = "lambda$reloadResources$30",
        at = @At("HEAD"),
        argsOnly = true,
        remap = false
    )
    private MinecraftServer.ReloadableResources modifyReloadableResources(
        MinecraftServer.ReloadableResources p_335203_
    ) {
        ((IInjectingReloadableServerResources) p_335203_.managers()).practicalExtensions$injectDataIntoManagers();
        return p_335203_;
    }
}
