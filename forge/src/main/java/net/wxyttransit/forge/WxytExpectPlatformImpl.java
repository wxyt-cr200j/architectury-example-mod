package net.wxyttransit.forge;

import dev.architectury.platform.Platform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.wxyttransit.WxytExpectPlatform;
import net.minecraftforge.fml.loading.FMLPaths;
import net.wxyttransit.block.WxytPSDTop;
import net.wxyttransit.gui.GUIPSDTopSettings;

import java.nio.file.Path;

public class WxytExpectPlatformImpl {
    /**
     * This is our actual method to {@link WxytExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static void setScreen(Screen screen){
        if(Platform.getEnv() == Dist.CLIENT){
            Minecraft.getInstance().setScreen(screen);
        }
    }
    public static void setPSDTopScreen(WxytPSDTop.TileEntityPSDTop top){
        if(Platform.getEnv() == Dist.CLIENT){
            Minecraft.getInstance().setScreen(new GUIPSDTopSettings<>(Component.literal("psd_top"),top));
        }
    }
}
