package com.example.rebujimod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;

import org.jetbrains.annotations.Nullable;

public class BottleRack extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final int ROWS = 6;
    private static final int COLS = 4;
    private static final int SIZE = 24;

    private static final VoxelShape SHAPE_NORTH = Shapes.box(0, 0, 0, 1, 1, 0.5);
    private static final VoxelShape SHAPE_SOUTH = Shapes.box(0, 0, 0.5, 1, 1, 1);
    private static final VoxelShape SHAPE_WEST = Shapes.box(0, 0, 0, 0.5, 1, 1);
    private static final VoxelShape SHAPE_EAST = Shapes.box(0.5, 0, 0, 1, 1, 1);

    public BottleRack(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    private VoxelShape getShapeForState(BlockState state) {
        return switch (state.getValue(FACING)) {
            case NORTH -> SHAPE_SOUTH;
            case SOUTH -> SHAPE_NORTH;
            case EAST -> SHAPE_WEST;
            case WEST -> SHAPE_EAST;
            default -> SHAPE_SOUTH;
        };
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
            net.minecraft.world.phys.shapes.CollisionContext context) {
        return getShapeForState(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos,
            net.minecraft.world.phys.shapes.CollisionContext context) {
        return getShapeForState(state);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    private Vec2 rotateHit(BlockState state, double lx, double lz) {

        Direction facing = state.getValue(FACING);

        double x = lx;
        double z = lz;

        switch (facing) {

            case NORTH -> {
                return new Vec2((float) x, (float) z);
            }

            case SOUTH -> {
                return new Vec2((float) (1.0 - x), (float) (1.0 - z));
            }

            case WEST -> {
                return new Vec2((float) z, (float) (1.0 - x));
            }

            case EAST -> {
                return new Vec2((float) (1.0 - z), (float) x);
            }

            default -> {
                return new Vec2((float) x, (float) z);
            }
        }
    }

    // =========================
    // INTERACCIÓN PRINCIPAL
    // =========================

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hit) {

        if (level.isClientSide) {
            return InteractionResult.sidedSuccess(true);
        }

        BottleRackBlockEntity be = (BottleRackBlockEntity) level.getBlockEntity(pos);
        if (be == null)
            return InteractionResult.PASS;

        ItemStack inHand = player.getItemInHand(hand);

        boolean isBottle = inHand.is(ModItems.BOTELLA_VINO_VACIA.get()) ||
                inHand.is(ModItems.BOTELLA_VINO_TINTO.get()) ||
                inHand.is(ModItems.BOTELLA_VINO_BLANCO.get());

        // =========================
        // 1. CLICK → SLOT
        // =========================

        double lx = hit.getLocation().x - pos.getX();
        double ly = hit.getLocation().y - pos.getY();
        double lz = hit.getLocation().z - pos.getZ();

        Vec2 rotated = rotateHit(state, lx, lz);
        int slot = be.getSlotAtHitCoord(rotated.x, rotated.y);

        System.out.println("[BottleRack] slot calculado = " + slot +
                " | lx=" + lx + " ly=" + ly +
                " | rotatedX=" + rotated.x + " rotatedY=" + rotated.y);

        if (slot < 0 || slot >= SIZE)
            return InteractionResult.PASS;

        // =========================
        // 2. SI TIENE BOTELLA → INTENTA INSERTAR
        // =========================

        if (isBottle) {

            if (be.getBottle(slot).isEmpty()) {

                ItemStack copy = new ItemStack(inHand.getItem(), 1);
                be.setBottle(slot, copy);

                inHand.shrink(1);

                return InteractionResult.sidedSuccess(false);
            }

            return InteractionResult.PASS;
        }

        // =========================
        // 3. SI NO TIENE → SACAR
        // =========================

        ItemStack stored = be.getBottle(slot);

        if (!stored.isEmpty()) {

            if (!player.getInventory().add(stored.copy())) {
                Block.popResource(level, pos, stored.copy());
            }

            be.setBottle(slot, ItemStack.EMPTY);

            return InteractionResult.sidedSuccess(false);
        }

        return InteractionResult.PASS;
    }

    // =========================
    // ROTACIÓN DE SLOT
    // =========================

    private int getRotatedSlot(Direction facing, int rawSlot) {
        if (rawSlot < 0)
            return -1;

        // NORTH = normal
        if (facing == Direction.NORTH)
            return rawSlot;

        // EAST / WEST / SOUTH rotan grid 4x6
        int row = rawSlot / COLS;
        int col = rawSlot % COLS;

        switch (facing) {

            case SOUTH -> {
                row = (ROWS - 1) - row;
                col = (COLS - 1) - col;
            }

            case EAST -> {
                int newRow = col;
                int newCol = (ROWS - 1) - row;
                row = newRow;
                col = newCol;
            }

            case WEST -> {
                int newRow = (COLS - 1) - col;
                int newCol = row;
                row = newRow;
                col = newCol;
            }
        }

        return row * COLS + col;
    }

    // =========================
    // BLOCK ENTITY
    // =========================

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BottleRackBlockEntity(pos, state);
    }

    // =========================
    // DROPS AL ROMPER
    // =========================

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos,
            BlockState state, BlockEntity blockEntity, ItemStack tool) {

        super.playerDestroy(level, player, pos, state, blockEntity, tool);

        if (blockEntity instanceof BottleRackBlockEntity rack) {

            for (int i = 0; i < SIZE; i++) {
                ItemStack stored = rack.getBottle(i);
                if (!stored.isEmpty()) {
                    Block.popResource(level, pos, stored.copy());
                }
            }
        }
    }
}
