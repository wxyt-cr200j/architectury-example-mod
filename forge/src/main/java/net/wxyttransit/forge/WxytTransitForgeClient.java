package net.wxyttransit.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.wxyttransit.WxytTransit;
import net.wxyttransit.WxytTransitClient;

public class WxytTransitForgeClient {
    public static void init(FMLClientSetupEvent event){
        event.enqueueWork(WxytTransitClient::initClient);
    }
}
