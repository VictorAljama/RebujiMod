package com.example.rebujimod;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GrapeVineBlock extends BushBlock {

    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 3);

    public GrapeVineBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, net.minecraft.world.level.block.state.BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {

        int age = state.getValue(AGE);

        if (age < 3) {

            // probabilidad de crecimiento
            if (true) {

                level.setBlock(pos, state.setValue(AGE, age + 1), 2);
            }
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        int age = state.getValue(AGE);

        // Stage 0 y 1 (edad 0 y 1): Sin colisión significativa (solo tronco fino)
        if (age <= 1) {
            return Shapes.empty();
        }

        // Stage 2 (edad 2): Hitbox pequeño-mediano (más desarrollo de hojas y uvas)
        if (age == 2) {
            return Shapes.box(0.3, 0, 0.3, 0.7, 0.8, 0.7);
        }

        // Stage 3 (edad 3): Hitbox más grande (planta completamente desarrollada)
        return Shapes.box(0.2, 0, 0.2, 0.8, 1.0, 0.8);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.getItemInHand(hand).is(Items.SHEARS)) {
            int age = state.getValue(AGE);
            if (age == 3) {
                if (!level.isClientSide) {
                    boolean isGreen = state.is(ModItems.GREEN_GRAPE_VINE.get());
                    ItemStack drop = new ItemStack(isGreen ? ModItems.GREEN_GRAPE.get() : ModItems.PURPLE_GRAPE.get(), 2);
                    Block.popResource(level, pos, drop);
                    level.setBlock(pos, state.setValue(AGE, 0), 2);
                    player.getItemInHand(hand).hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, net.minecraft.world.level.block.entity.BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);

        int age = state.getValue(AGE);

        // Stage 0 y 1: Dropea 1 palo
        if (age <= 1) {
            Block.popResource(level, pos, new ItemStack(Items.STICK, 1));
        }
        // Stage 2 y 3: Dropea 3 palos
        else {
            Block.popResource(level, pos, new ItemStack(Items.STICK, 3));
        }
    }
}

