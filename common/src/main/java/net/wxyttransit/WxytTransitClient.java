package net.wxyttransit;

import mtr.RegistryClient;
import mtr.render.RenderPSDAPGDoor;
import net.minecraft.client.renderer.RenderType;
import net.wxyttransit.block.WxytPSDTop;
import net.wxyttransit.packet.PacketHelperClient;
import net.wxyttransit.render.*;
import net.wxyttransit.shenzhen.*;

public class WxytTransitClient {
    public static void initClient(){
        PacketHelperClient.registerPackets();

        WxytTextureData.data.addRenderConsumer("sz_now", RenderShenzhenMetroStationNow::draw);
        WxytTextureData.data.addRenderConsumer("sz_route", RenderShenzhenMetroRoute::draw);
        WxytTextureData.data.addRenderConsumer("sz_none",RenderShenzhenMetroNone::draw);
        WxytTextureData.data.addRenderConsumer("sz_destination",RenderShenzhenMetroDestination::draw);

        RegistryClient.registerBlockRenderType(RenderType.cutout(),WxytBlocks.WXYT_PSD_DOOR_0.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(),WxytBlocks.WXYT_PSD_TOP_0.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(),WxytBlocks.WXYT_PSD_GLASS_0.get());
RegistryClient.registerBlockRenderType(RenderType.cutout(),WxytBlocks.WXYT_PSD_ONLY_DOOR_0.get());
        RegistryClient.registerTileEntityRenderer(WxytBlockEntityTypes.PSD_DOOR_0_TILE_ENTITY.get(), dis->{return new RenderWxytPSDAPGDoor<>(dis,"shenzhen",0,1);});
        RegistryClient.registerTileEntityRenderer(WxytBlockEntityTypes.PSD_TOP_0_TILE_ENTITY.get(),dis-> new RenderWxytPSDTop<>(dis, "sz_now"));
        RegistryClient.registerTileEntityRenderer(WxytBlockEntityTypes.PSD_GLASS_0_TILE_ENTITY.get(), dis->{return new RenderWxytPSDAPGGlass<>(dis,"shenzhen",0,1);});
    RegistryClient.registerTileEntityRenderer(WxytBlockEntityTypes.PSD_ONLY_DOOR_0_TILE_ENTITY.get(),(dis)->new RenderWxytPSDAPGDoor<>(false,dis,"shenzhen",0,1));
    }
}
