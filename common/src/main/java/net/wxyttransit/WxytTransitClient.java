package net.wxyttransit;

import mtr.RegistryClient;
import mtr.render.RenderPSDAPGDoor;
import net.minecraft.client.renderer.RenderType;
import net.wxyttransit.render.RenderWxytPSDAPGDoor;
import net.wxyttransit.render.RenderWxytPSDTop;

public class WxytTransitClient {
    public static void initClient(){
        RegistryClient.registerBlockRenderType(RenderType.cutout(),WxytBlocks.WXYT_PSD_DOOR_0.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(),WxytBlocks.WXYT_PSD_TOP_0.get());

        RegistryClient.registerTileEntityRenderer(WxytBlockEntityTypes.PSD_DOOR_0_TILE_ENTITY.get(), dis->{System.out.println("bitchhhhhhhhhhhhhhhhhhhhhhhhhhhh");return new RenderWxytPSDAPGDoor<>(dis,1);});
        RegistryClient.registerTileEntityRenderer(WxytBlockEntityTypes.PSD_TOP_0_TILE_ENTITY.get(),RenderWxytPSDTop::new);
        System.out.println("hello client");
    }
}
