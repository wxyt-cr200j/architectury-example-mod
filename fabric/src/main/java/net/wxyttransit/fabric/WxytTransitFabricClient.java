package net.wxyttransit.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.wxyttransit.WxytTransit;
import net.wxyttransit.WxytTransitClient;

public class WxytTransitFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WxytTransitClient.initClient();
    }
}
