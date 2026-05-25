package com.example.rebujimod;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BarrelBlock extends Block implements EntityBlock {

    public static final IntegerProperty LLENADO = IntegerProperty.create("llenado", 0, 4);
    public static final BooleanProperty TINTO = BooleanProperty.create("tinto");

    public BarrelBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LLENADO, 0).setValue(TINTO, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LLENADO, TINTO);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
            BlockHitResult hit) {
        ItemStack itemInHand = player.getItemInHand(hand);
        int currentLlenado = state.getValue(LLENADO);
        boolean isGreenGrape = itemInHand.is(ModItems.GREEN_GRAPE.get());
        boolean isPurpleGrape = itemInHand.is(ModItems.PURPLE_GRAPE.get());

        if ((isGreenGrape || isPurpleGrape) && currentLlenado < 4) {
            boolean currentTinto = state.getValue(TINTO);
            boolean requestedTinto = isPurpleGrape;

            // No mezclar tipos de vino una vez que el barril ya tiene contenido.
            if (currentLlenado > 0 && currentTinto != requestedTinto) {
                return InteractionResult.PASS;
            }

            if (!level.isClientSide) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof BarrelBlockEntity) {
                    BarrelBlockEntity barrelEntity = (BarrelBlockEntity) blockEntity;

                    barrelEntity.incrementInteractionCount();
                    itemInHand.shrink(1);

                    if (barrelEntity.getInteractionCount() >= 5) {
                        int newLlenado = currentLlenado + 1;
                        boolean newTinto = currentLlenado == 0 ? requestedTinto : currentTinto;
                        level.setBlock(pos, state.setValue(LLENADO, newLlenado).setValue(TINTO, newTinto), 3);
                        barrelEntity.resetInteractionCount();
                    }

                    barrelEntity.setChanged();
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (currentLlenado == 4) {
            return InteractionResult.PASS;
        }

        return super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        // Ocupar todo el bloque (0 a 1 en los tres ejes)
        return Shapes.box(0, 0, 0, 1, 1, 1);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BarrelBlockEntity(pos, state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(LLENADO, 0).setValue(TINTO, false);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state,
            net.minecraft.world.level.block.entity.BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);

        Block.popResource(level, pos, new ItemStack(ModItems.BARRICA.get(), 1));
    }
}
