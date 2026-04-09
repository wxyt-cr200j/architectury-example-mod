package net.wxyttransit.fabric;

import net.wxyttransit.WxytExpectPlatform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class WxytExpectPlatformImpl {
    /**
     * This is our actual method to {@link WxytExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
