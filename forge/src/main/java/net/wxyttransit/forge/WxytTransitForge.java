package net.wxyttransit.forge;

import dev.architectury.platform.forge.EventBuses;
import net.wxyttransit.WxytTransit;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(WxytTransit.MOD_ID)
public class WxytTransitForge {
    public WxytTransitForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(WxytTransit.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        WxytTransit.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(WxytTransitForgeClient::init);
    }
}
