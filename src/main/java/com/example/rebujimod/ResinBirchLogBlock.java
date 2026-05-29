package com.example.rebujimod;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ResinBirchLogBlock extends RotatedPillarBlock {

    public ResinBirchLogBlock(Properties properties) {
        super(properties.randomTicks());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
            BlockHitResult hit) {

        ItemStack stack = player.getItemInHand(hand);

        // 1. Solo hachas pueden interactuar
        if (!stack.is(ItemTags.AXES)) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {

            // 2. Drop de resina (3 unidades)
            ItemStack drop = new ItemStack(ModItems.RAW_RESIN.get(), 3);
            Block.popResource(level, pos, drop);

            // 3. Cambiar al bloque sin corteza (sin resina)
            level.setBlock(pos,
                    ModBlocks.RESIN_BIRCH_LOG_EMPTY.get().defaultBlockState()
                            .setValue(AXIS, state.getValue(AXIS)),
                    Block.UPDATE_ALL);

            // 4. Dañar el hacha
            stack.hurtAndBreak(1, player,
                    p -> p.broadcastBreakEvent(hand));
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        ItemStack tool = player.getMainHandItem();

        if (tool.is(ItemTags.AXES)) {
            return super.getDestroyProgress(state, player, level, pos);
        }

        return 0.0F; // sin hacha = casi imposible
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, net.minecraft.world.level.block.entity.BlockEntity blockEntity, ItemStack tool) {
        Block.popResource(level, pos, new ItemStack(Items.BIRCH_LOG, 1));
        Block.popResource(level, pos, new ItemStack(ModItems.RAW_RESIN.get(), 3));
    }

}
