package net.wxyttransit;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.platform.Platform;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.wxyttransit.block.WxytPSDDoor;
import net.wxyttransit.block.WxytPSDTop;

import java.nio.file.Path;

public class WxytExpectPlatform {
    /**
     * We can use {@link Platform#getConfigFolder()} but this is just an example of {@link ExpectPlatform}.
     * <p>
     * This must be a <b>public static</b> method. The platform-implemented solution must be placed under a
     * platform sub-package, with its class suffixed with {@code Impl}.
     * <p>
     * Wxyt:
     * Expect: net.wxyttransit.WxytExpectPlatform#getConfigDirectory()
     * Actual Fabric: net.wxyttransit.fabric.WxytExpectPlatformImpl#getConfigDirectory()
     * Actual Forge: net.wxyttransit.forge.WxytExpectPlatformImpl#getConfigDirectory()
     * <p>
     * <a href="https://plugins.jetbrains.com/plugin/16210-architectury">You should also get the IntelliJ plugin to help with @ExpectPlatform.</a>
     */
    @ExpectPlatform
    public static Path getConfigDirectory() {
        // Just throw an error, the content should get replaced at runtime.
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void setScreen(Screen screen){
    }

    @ExpectPlatform
    public static void setPSDTopScreen(WxytPSDTop.TileEntityPSDTop top){

    }
}
