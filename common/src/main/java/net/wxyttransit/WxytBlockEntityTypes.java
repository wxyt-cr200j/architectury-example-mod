package net.wxyttransit;


import mtr.RegistryObject;
import mtr.block.BlockPSDDoor;
import mtr.mappings.RegistryUtilities;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.wxyttransit.block.*;

public class WxytBlockEntityTypes {
    public static RegistryObject<BlockEntityType<WxytPSDDoor.TileEntityPSDDoor>> PSD_DOOR_0_TILE_ENTITY = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType(WxytPSDDoor.TileEntityPSDDoor::new, WxytBlocks.WXYT_PSD_DOOR_0.get()));
    public static RegistryObject<BlockEntityType<WxytPSDTop.TileEntityPSDTop>> PSD_TOP_0_TILE_ENTITY = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType(WxytPSDTop.TileEntityPSDTop::new, WxytBlocks.WXYT_PSD_TOP_0.get()));
    public static RegistryObject<BlockEntityType<WxytPSDGlass.TileEntityPSDGlass>> PSD_GLASS_0_TILE_ENTITY = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType(WxytPSDGlass.TileEntityPSDGlass::new, WxytBlocks.WXYT_PSD_GLASS_0.get()));

}