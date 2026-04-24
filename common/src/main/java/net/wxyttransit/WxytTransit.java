package net.wxyttransit;

import com.google.common.base.Suppliers;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import mtr.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.wxyttransit.block.WxytPSDTop;
import net.wxyttransit.data.PSDTopCache;

import java.util.function.Supplier;

public class WxytTransit {
    public static final String MOD_ID = "wxyttransit";
    // We can use this if we don't want to use DeferredRegister
    public static final Supplier<RegistrarManager> REGISTRIES = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(MOD_ID, Registries.CREATIVE_MODE_TAB);
    public static final DeferredRegister<Block> BLOCKS=DeferredRegister.create(MOD_ID,Registries.BLOCK);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES=DeferredRegister.create(MOD_ID,Registries.BLOCK_ENTITY_TYPE);


    public static final RegistrySupplier<CreativeModeTab> WXYT_MTR = TABS.register("wxyt_mtr", () ->
            CreativeTabRegistry.create(Component.translatable("itemGroup." + MOD_ID + ".wxyt_mtr"),
                    () -> new ItemStack(WxytTransit.EXAMPLE_ITEM.get())));


    public  static RegistrySupplier<Block> BLOCK_WXYT_PSD_DOOR=BLOCKS.register(new ResourceLocation(MOD_ID,"psd_sz_door_0"),()->{System.out.println("fuckkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");return WxytBlocks.WXYT_PSD_DOOR_0.get();});
    public  static RegistrySupplier<Block> BLOCK_WXYT_PSD_TOP=BLOCKS.register(new ResourceLocation(MOD_ID,"psd_sz_top_0"),WxytBlocks.WXYT_PSD_TOP_0::get);


    public static final RegistrySupplier<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () ->
            new Item(new Item.Properties().arch$tab(WxytTransit.WXYT_MTR)));
    public static RegistrySupplier<Item> ITEM_WXYT_PSD_DOOR=ITEMS.register(new ResourceLocation(MOD_ID,"psd_sz_door_0"),WxytItems.WXYT_PSD_DOOR_ITEM::get);
    public static RegistrySupplier<Item> ITEM_WXYT_BRUSH = ITEMS.register(new ResourceLocation(MOD_ID,"brush"),WxytItems.WXYT_BRUSH::get);

    public  static RegistrySupplier<BlockEntityType<?>> BLOCK_ENTITY_TYPE_WXYT_PSD_DOOR=BLOCK_ENTITY_TYPES.register(new ResourceLocation(MOD_ID,"psd_sz_door_0"),()->{System.out.println("shitttttttttttttttkkkkkkkk");return WxytBlockEntityTypes.PSD_DOOR_0_TILE_ENTITY.get();});
    public  static RegistrySupplier<BlockEntityType<?>> BLOCK_ENTITY_TYPE_WXYT_PSD_TOP=BLOCK_ENTITY_TYPES.register(new ResourceLocation(MOD_ID,"psd_sz_top_0"),WxytBlockEntityTypes.PSD_TOP_0_TILE_ENTITY::get);


    public static void init() {
        //PSDTopCache.register("sz_now_1",new RegistryObject<>(WxytPSDTop::new));
        TABS.register();
        BLOCKS.register();
        ITEMS.register();
        BLOCK_ENTITY_TYPES.register();
        System.out.println(WxytExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }
}
