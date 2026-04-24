package net.wxyttransit.gui;

import mtr.mappings.ScreenMapper;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class WxytScreen extends ScreenMapper {

    public WxytScreen(Component component) {
        super(component);
    }
    public void addRenderable1( Renderable listener){
         addRenderableOnly(listener);
    }
    public <T extends Renderable&GuiEventListener& NarratableEntry>  void addRenderWidget(T listener){
        addRenderableWidget(listener);
    }
}
