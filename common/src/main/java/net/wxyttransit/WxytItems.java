package net.wxyttransit;


import mtr.RegistryObject;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.wxyttransit.item.*;

public class WxytItems {

    public static RegistryObject<Item>  WXYT_PSD_DOOR_ITEM = new RegistryObject<>(()->new WxytItemPSDAPGBase(WxytItemPSDAPGBase.EnumPSDAPGItem.WXYT_PSD_DOOR, WxytItemPSDAPGBase.EnumPSDAPGType.WXYT_PSD));;
    // public static RegistryObject<Item>  OAPG_GLASS_END = new RegistryObject<>(()->new BlockItem(ONEFUN_APG_Glass_End.get(),new Item.Properties().tab(OnefunCreativeTabs.onefunMtr)));

}
