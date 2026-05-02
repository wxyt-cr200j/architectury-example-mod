package net.wxyttransit.packet;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.wxyttransit.block.WxytPSDAPGDoorBase;
import net.wxyttransit.block.WxytPSDAPGGlassBase;
import net.wxyttransit.block.WxytPSDTop;

import static net.wxyttransit.WxytTransit.MOD_ID;

public class PacketHelper {
    public static final ResourceLocation DOOR_SCREEN_PACKET = new ResourceLocation(MOD_ID, "door_screen");
    public static final ResourceLocation GLASS_SCREEN_PACKET = new ResourceLocation(MOD_ID, "glass_screen");
    public static final ResourceLocation TOP_SCREEN_PACKET = new ResourceLocation(MOD_ID, "top_screen");
    public static final ResourceLocation RENDER_TYPE_PACKET = new ResourceLocation(MOD_ID, "render_type");
    public static final ResourceLocation TYPE_PACKET = new ResourceLocation(MOD_ID, "type");
    public static final ResourceLocation LIGHT_TYPE_PACKET = new ResourceLocation(MOD_ID, "light_type");
    public static final ResourceLocation DISTRICT_PACKET = new ResourceLocation(MOD_ID,"district");
    public static final ResourceLocation LIGHT_DISTRICT_PACKET = new ResourceLocation(MOD_ID,"light_district");

    public static void registerPackets() {


        NetworkManager.registerReceiver(NetworkManager.Side.C2S, RENDER_TYPE_PACKET, (buf, context) -> {
            BlockPos pos = buf.readBlockPos();
            String renderType = buf.readUtf();

            context.queue(() -> {
                ServerPlayer player = (ServerPlayer) context.getPlayer();
                BlockEntity be = player.level().getBlockEntity(pos);
                if (be instanceof WxytPSDTop.TileEntityRouteBase routeBase) {
                    routeBase.renderType = renderType;
                    routeBase.refresh();
                }
            });
        });
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, TYPE_PACKET, (buf, context) -> {
            BlockPos pos = buf.readBlockPos();
            int type = Integer.parseInt(buf.readUtf());

            context.queue(() -> {
                ServerPlayer player = (ServerPlayer) context.getPlayer();
                BlockEntity be = player.level().getBlockEntity(pos);
                if (be instanceof WxytPSDAPGDoorBase.TileEntityPSDAPGDoorBase base) {
                    if(base.type!=null)base.type = type;
                    base.refresh();
                }
            });
        });
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, LIGHT_TYPE_PACKET, (buf, context) -> {
            BlockPos pos = buf.readBlockPos();
            int type = Integer.parseInt(buf.readUtf());

            context.queue(() -> {
                ServerPlayer player = (ServerPlayer) context.getPlayer();
                BlockEntity be = player.level().getBlockEntity(pos);
                if (be instanceof WxytPSDAPGDoorBase.TileEntityPSDAPGDoorBase base) {
                    base.light_type = type;
                    base.refresh();
                }
            });
        });
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, DISTRICT_PACKET, (buf, context) -> {
            BlockPos pos = buf.readBlockPos();
            String district = buf.readUtf();

            context.queue(() -> {
                ServerPlayer player = (ServerPlayer) context.getPlayer();
                BlockEntity be = player.level().getBlockEntity(pos);
                if (be instanceof WxytPSDAPGDoorBase.TileEntityPSDAPGDoorBase base) {
                    base.district = district;
                    base.refresh();
                }
            });
        });
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, LIGHT_DISTRICT_PACKET, (buf, context) -> {
            BlockPos pos = buf.readBlockPos();
            String lightDistrict = buf.readUtf();

            context.queue(() -> {
                ServerPlayer player = (ServerPlayer) context.getPlayer();
                BlockEntity be = player.level().getBlockEntity(pos);
                if (be instanceof WxytPSDAPGDoorBase.TileEntityPSDAPGDoorBase base) {
                    base.lightDistrict = lightDistrict;
                    base.refresh();
                }
            });
        });
    }


}
