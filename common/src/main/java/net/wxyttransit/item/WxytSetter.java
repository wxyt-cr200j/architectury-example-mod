package net.wxyttransit.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.wxyttransit.block.WxytPSDTop;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.wxyttransit.WxytTransit.WXYT_MTR;

public class WxytSetter extends Item {
    public WxytSetter(){
        super(new Properties().arch$tab(WXYT_MTR));
    }
    public WxytSetter(Properties properties){
        super(properties);

    }
    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        // 1. 获取物品NBT
        CompoundTag tag = stack.getTag();
        String type = "default";

        // 2. 安全读取字符串
        if(tag != null && tag.contains("psd_top_script")){
            type = tag.getString("psd_top_script");
        }

        // 3. 加入悬浮提示（灰色小字，原版风格）
        tooltip.add(
                Component.literal(Component.translatable("item.wxyttransit.setter").getString()+Component.translatable("renderScript."+type).getString())
                        .withStyle(style -> style.withColor(TextColor.fromRgb(0x888888)))
        );

    }
    @Override
    public boolean canAttackBlock(BlockState state,Level world,BlockPos pos,Player player){
        return true;
    }
    @Override
    public InteractionResult useOn(UseOnContext ctx){

        return InteractionResult.SUCCESS;
    }

}
