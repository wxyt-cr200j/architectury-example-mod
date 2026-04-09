package net.wxyttransit.forge;

import net.wxyttransit.WxytExpectPlatform;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class WxytExpectPlatformImpl {
    /**
     * This is our actual method to {@link WxytExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
