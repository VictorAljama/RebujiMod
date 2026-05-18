package com.example.rebujimod;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class GreenGrapeVineBlock extends BushBlock {

    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 3);

    public GreenGrapeVineBlock(Properties properties) {
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

        System.out.println("AGE: " + age);

        if (age < 3) {

            // probabilidad de crecimiento
            if (true) {

                level.setBlock(pos, state.setValue(AGE, age + 1), 2);
            }
        }
    }
}