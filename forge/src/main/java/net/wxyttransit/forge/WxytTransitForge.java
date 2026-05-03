package net.wxyttransit.forge;

import dev.architectury.platform.Platform;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.wxyttransit.WxytTransit;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("wxyttransit")
public class WxytTransitForge {
    public WxytTransitForge() {
        //System.out.println(10001);
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(WxytTransit.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        WxytTransit.init();
        if(FMLEnvironment.dist==Dist.CLIENT)FMLJavaModLoadingContext.get().getModEventBus().addListener(WxytTransitForgeClient::init);
    }
}
