package net.wxyttransit.gui;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.wxyttransit.block.*;
import net.wxyttransit.packet.PacketHelper;
import net.wxyttransit.packet.PacketHelperClient;


public class ScreenUtil {
    public static void setPSDTopScreen(Player player, WxytPSDTop.TileEntityPSDTop top){

        System.out.println(15735);
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeBlockPos(top.getBlockPos());
       /* if(player instanceof ServerPlayer player1)*/NetworkManager.sendToPlayer((ServerPlayer) player, PacketHelper.TOP_SCREEN_PACKET,buf);
    }
    public static void setPSDDoorScreen(Player player,WxytPSDAPGDoorBase.TileEntityPSDAPGDoorBase top){

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeBlockPos(top.getBlockPos());
        /*if(player instanceof ServerPlayer player1)*/ NetworkManager.sendToPlayer((ServerPlayer) player, PacketHelper.DOOR_SCREEN_PACKET,buf);
    }
    public static void setPSDGlassScreen(Player player,WxytPSDAPGGlassBase.TileEntityPSDAPGGlassBase top){

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeBlockPos(top.getBlockPos());
        if(player instanceof ServerPlayer player1)NetworkManager.sendToPlayer(player1, PacketHelper.GLASS_SCREEN_PACKET,buf);
    }
}
