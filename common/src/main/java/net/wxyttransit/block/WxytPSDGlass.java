package net.wxyttransit.block;

import mtr.Items;
import net.minecraft.world.item.Item;

public class WxytPSDGlass extends WxytPSDAPGGlassBase {

	private final int style;

	public WxytPSDGlass(int style) {
		super();
		this.style = style;
	}

	@Override
	public Item asItem() {
		return style == 0 ? Items.PSD_GLASS_1.get() : Items.PSD_GLASS_2.get();
	}
}
