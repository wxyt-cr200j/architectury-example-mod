package net.wxyttransit.fabric;

import net.wxyttransit.WxytTransit;
import net.fabricmc.api.ModInitializer;

public class WxytTransitFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        WxytTransit.init();
    }
}
