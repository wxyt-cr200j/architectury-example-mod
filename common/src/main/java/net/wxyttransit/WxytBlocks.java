package net.wxyttransit;

import mtr.RegistryObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.wxyttransit.block.*;

public class WxytBlocks {
    public static RegistryObject<Block> WXYT_PSD_DOOR_0 = new RegistryObject<>(()-> new WxytPSDDoor(0));
    public static RegistryObject<Block> WXYT_PSD_GLASS_0 = new RegistryObject<>(()-> new WxytPSDGlass(0));

    public static RegistryObject<Block> WXYT_PSD_TOP_0=new RegistryObject<>(WxytPSDTop::new);

}