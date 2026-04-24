package net.wxyttransit.mixin;

import mtr.client.ClientCache;
import net.wxyttransit.render.WxytTextureData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCache.class)
public class MixinMTRClientCache {
    @Inject(method = "refreshDynamicResources()V", at = @At("RETURN"),remap = false)
    private void refreshWxytTextures(CallbackInfo ci){
        WxytTextureData.data.refreshAll();
    }
}
