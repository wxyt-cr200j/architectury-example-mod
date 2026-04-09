package net.wxyttransit;

import mtr.RegistryObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.wxyttransit.block.*;

public interface WxytBlocks {
    RegistryObject<Block> WXYT_PSD_DOOR_0 = new RegistryObject<>(()-> new WxytPSDDoor(0));
    RegistryObject<Block> WXYT_PSD_TOP_0=new RegistryObject<>(WxytPSDTop::new);

}