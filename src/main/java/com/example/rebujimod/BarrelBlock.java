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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BarrelBlock extends Block implements EntityBlock {

    public static final IntegerProperty LLENADO = IntegerProperty.create("llenado", 0, 4);

    public BarrelBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LLENADO, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LLENADO);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
            BlockHitResult hit) {
        ItemStack itemInHand = player.getItemInHand(hand);
        int currentLlenado = state.getValue(LLENADO);

        // Solo permitir interacción si el barril no está completamente lleno (llenado < 4)
        if (itemInHand.is(ModItems.GREEN_GRAPE.get()) && currentLlenado < 4) {
            if (!level.isClientSide) {
                // Obtener el BlockEntity
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof BarrelBlockEntity) {
                    BarrelBlockEntity barrelEntity = (BarrelBlockEntity) blockEntity;

                    // Incrementar el contador de interacciones
                    barrelEntity.incrementInteractionCount();

                    // Restar 1 del item del inventario
                    itemInHand.shrink(1);

                    // Si alcanzamos 5 interacciones, incrementar llenado y resetear contador
                    if (barrelEntity.getInteractionCount() >= 5) {
                        int newLlenado = currentLlenado + 1;
                        level.setBlock(pos, state.setValue(LLENADO, newLlenado), 3);
                        barrelEntity.resetInteractionCount();
                    }

                    // Marcar el BlockEntity como modificado
                    barrelEntity.setChanged();
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Si el barril está lleno (llenado == 4), no hacer nada
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
        return this.defaultBlockState().setValue(LLENADO, 0);
    }
}
