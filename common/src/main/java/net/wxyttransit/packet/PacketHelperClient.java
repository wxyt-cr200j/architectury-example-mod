package net.wxyttransit.packet;

import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.wxyttransit.block.WxytPSDAPGDoorBase;
import net.wxyttransit.block.WxytPSDAPGGlassBase;
import net.wxyttransit.block.WxytPSDTop;
import net.wxyttransit.gui.GUIPSDAPGDoorSettings;
import net.wxyttransit.gui.GUIPSDAPGGlassSettings;
import net.wxyttransit.gui.GUIPSDTopSettings;

import static net.wxyttransit.WxytTransit.MOD_ID;

public class PacketHelperClient {
    public static final ResourceLocation DOOR_SCREEN_PACKET = new ResourceLocation(MOD_ID, "door_screen");
    public static final ResourceLocation GLASS_SCREEN_PACKET = new ResourceLocation(MOD_ID, "glass_screen");
    public static final ResourceLocation TOP_SCREEN_PACKET = new ResourceLocation(MOD_ID, "top_screen");

    public static void registerPackets() {

        //System.out.println(565895);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, DOOR_SCREEN_PACKET, (buf, context) -> {
            context.queue(() -> {WxytPSDAPGDoorBase.TileEntityPSDAPGDoorBase base = (WxytPSDAPGDoorBase.TileEntityPSDAPGDoorBase) Minecraft.getInstance().level.getBlockEntity(buf.readBlockPos());

                //System.out.println(1451457777);

                Minecraft.getInstance().setScreen(new GUIPSDAPGDoorSettings<>(Component.literal("door"),base));

            });
        });
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, GLASS_SCREEN_PACKET, (buf, context) -> {

            context.queue(() -> {   WxytPSDAPGGlassBase.TileEntityPSDAPGGlassBase base = (WxytPSDAPGGlassBase.TileEntityPSDAPGGlassBase) Minecraft.getInstance().level.getBlockEntity(buf.readBlockPos());

                Minecraft.getInstance().setScreen(new GUIPSDAPGGlassSettings<>(Component.literal("glass"),base));
            });
        });
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, TOP_SCREEN_PACKET, (buf, context) -> {
            context.queue(() -> { WxytPSDTop.TileEntityPSDTop base = (WxytPSDTop.TileEntityPSDTop) Minecraft.getInstance().level.getBlockEntity(buf.readBlockPos());

                Minecraft.getInstance().setScreen(new GUIPSDTopSettings<>(Component.literal("top"),base));

            });
        });

    }


}
