package net.wxyttransit;

import mtr.RegistryClient;
import mtr.render.RenderPSDAPGDoor;
import net.minecraft.client.renderer.RenderType;
import net.wxyttransit.block.WxytPSDTop;
import net.wxyttransit.render.RenderWxytPSDAPGDoor;
import net.wxyttransit.render.RenderWxytPSDTop;
import net.wxyttransit.render.WxytTextureData;
import net.wxyttransit.shenzhen.RenderShenzhenMetroRoute;
import net.wxyttransit.shenzhen.RenderShenzhenMetroStationNow;

public class WxytTransitClient {
    public static void initClient(){
        WxytTextureData.data.addRenderConsumer("sz_now", RenderShenzhenMetroStationNow::draw);
        WxytTextureData.data.addRenderConsumer("sz_route", RenderShenzhenMetroRoute::draw);

        RegistryClient.registerBlockRenderType(RenderType.cutout(),WxytBlocks.WXYT_PSD_DOOR_0.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(),WxytBlocks.WXYT_PSD_TOP_0.get());

        RegistryClient.registerTileEntityRenderer(WxytBlockEntityTypes.PSD_DOOR_0_TILE_ENTITY.get(), dis->{System.out.println("bitchhhhhhhhhhhhhhhhhhhhhhhhhhhh");return new RenderWxytPSDAPGDoor<>(dis,"shenzhen",0,1);});
        RegistryClient.registerTileEntityRenderer(WxytBlockEntityTypes.PSD_TOP_0_TILE_ENTITY.get(),dis-> new RenderWxytPSDTop<>(dis, "sz_now"));
        System.out.println("hello client");
    }
}
