package com.example.rebujimod;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class RebujitoGlassVoidItem extends Item {

    public RebujitoGlassVoidItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        // SOLO si clicas en un caldero con agua (ejemplo)
        if (level.getBlockState(pos).is(Blocks.DIRT)) {

            // Cambiar item en mano
            context.getPlayer().setItemInHand(
                    context.getHand(),
                    new ItemStack(ModItems.REBUJITO_GLASS.get())
            );

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}