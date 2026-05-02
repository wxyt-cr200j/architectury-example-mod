package net.wxyttransit.fabric;

import mtr.block.BlockPSDTop;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.wxyttransit.WxytExpectPlatform;
import net.fabricmc.loader.api.FabricLoader;
import net.wxyttransit.block.WxytPSDTop;
import net.wxyttransit.gui.GUIPSDTopSettings;

import java.nio.file.Path;

public class WxytExpectPlatformImpl {
    /**
     * This is our actual method to {@link WxytExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
    @Environment(EnvType.CLIENT)
    public static void setScreen(Screen screen){
        Minecraft.getInstance().setScreen(screen);

    }
    @Environment(EnvType.CLIENT)
    public static void setPSDTopScreen(WxytPSDTop.TileEntityPSDTop top){
        Minecraft.getInstance().setScreen(new GUIPSDTopSettings<>(Component.literal("psd_top"),top));
    }
}
