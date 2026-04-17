package net.wxyttransit.item;

import mtr.block.*;
import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.wxyttransit.WxytBlocks;
import net.wxyttransit.data.PSDTopCache;

import java.util.List;

import static net.wxyttransit.WxytTransit.WXYT_MTR;

public class WxytItemPSDTopBase extends Item implements IBlock {

    private String type;
    public WxytItemPSDTopBase(String type) {
        super(new Properties().arch$tab(WXYT_MTR));
        this.type=type;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        final int horizontalBlocks =  1;
        if (blocksNotReplaceable(context, horizontalBlocks, 1, getBlockStateFromItem().getBlock())) {
            return InteractionResult.FAIL;
        }

        final Level world = context.getLevel();
        final Direction playerFacing = context.getHorizontalDirection();
        final BlockPos pos = context.getClickedPos().relative(context.getClickedFace());

        for (int x = 0; x < horizontalBlocks; x++) {
            final BlockPos newPos = pos.relative(playerFacing.getClockWise(), x);
            final BlockPos pos1=pos.below();
            final BlockState state1=world.getBlockState(pos1);
            final BlockState state = getBlockStateFromItem().setValue(BlockPSDTop.FACING, playerFacing);
            world.setBlockAndUpdate(newPos.above(0), state.setValue(SIDE_EXTENDED, EnumSide.SINGLE)
                    .setValue(BlockPSDTop.AIR_LEFT,IBlock.getStatePropertySafe(state1, BlockPSDAPGGlassEndBase.TOUCHING_LEFT)!= BlockPSDAPGGlassEndBase.EnumPSDAPGGlassEndSide.DOOR)
                    .setValue(BlockPSDTop.AIR_RIGHT,IBlock.getStatePropertySafe(state1, BlockPSDAPGGlassEndBase.TOUCHING_RIGHT)!= BlockPSDAPGGlassEndBase.EnumPSDAPGGlassEndSide.DOOR)
            );


        }

        context.getItemInHand().shrink(1);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
       // tooltip.add(Text.translatable(type.isLift ? type.isOdd ? "tooltip.mtr.railway_sign_odd" : "tooltip.mtr.railway_sign_even" : "tooltip.mtr." + item.getSerializedName()).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
    }

    private BlockState getBlockStateFromItem() {
        return PSDTopCache.getBlock(type).defaultBlockState();
    }

    public static boolean blocksNotReplaceable(UseOnContext context, int width, int height, Block blacklistBlock) {
        final Direction facing = context.getHorizontalDirection();
        final Level world = context.getLevel();
        final BlockPos startingPos = context.getClickedPos().relative(context.getClickedFace());

        for (int x = 0; x < width; x++) {
            final BlockPos offsetPos = startingPos.relative(facing.getClockWise(), x);

            if (blacklistBlock != null) {
                final boolean isBlacklistedBelow = world.getBlockState(offsetPos.below()).is(blacklistBlock);
                final boolean isBlacklistedAbove = world.getBlockState(offsetPos.above(height)).is(blacklistBlock);
                if (isBlacklistedBelow || isBlacklistedAbove) {
                    return true;
                }
            }

            for (int y = 0; y < height; y++) {
                if (!world.getBlockState(offsetPos.above(y)).canBeReplaced()) {
                    return true;
                }
            }
        }

        return false;
    }


}
