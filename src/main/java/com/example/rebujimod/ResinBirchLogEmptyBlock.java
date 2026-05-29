package com.example.rebujimod;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ResinBirchLogEmptyBlock extends RotatedPillarBlock {

    public ResinBirchLogEmptyBlock(Properties properties) {
        super(properties.randomTicks());
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Probabilidad de regeneración: 8% (ajustable)
        if (random.nextFloat() < 0.08f) {
            // Volver al bloque con resina, manteniendo el eje
            level.setBlock(pos,
                    ModBlocks.RESIN_BIRCH_LOG.get().defaultBlockState()
                            .setValue(AXIS, state.getValue(AXIS)),
                    3); // UPDATE_ALL
        }
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
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        Block.popResource(level, pos, new ItemStack(Items.STRIPPED_BIRCH_LOG, 1));
    }
}
