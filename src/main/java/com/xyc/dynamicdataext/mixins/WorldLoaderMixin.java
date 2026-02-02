package com.xyc.dynamicdataext.mixins;

import com.xyc.dynamicdataext.base.IInjectingReloadableServerResources;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.WorldLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WorldLoader.class)
public abstract class WorldLoaderMixin {
    /**
     * 加载世界时触发，只能加载标签，无法加载配方
     */
    @ModifyVariable(
        method = "lambda$load$1",
        at = @At("HEAD"),
        argsOnly = true,
        remap = false
    )
    private static ReloadableServerResources modifyReloadableServerResources(ReloadableServerResources p_335216_) {
        ((IInjectingReloadableServerResources) p_335216_).dynamicdataext$injectTags();
        return p_335216_;
    }
}
