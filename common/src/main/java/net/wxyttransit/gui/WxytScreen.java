package net.wxyttransit.gui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;


public class WxytScreen extends Screen implements IWxytScreen{

    public WxytScreen(Component component) {
       super(component);
    }

    @Override
    public Screen setScreen() {
        return this;
    }
}
