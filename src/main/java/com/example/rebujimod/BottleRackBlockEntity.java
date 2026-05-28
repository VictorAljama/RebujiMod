package com.example.rebujimod;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class BottleRackBlockEntity extends BlockEntity {

    // 24 botellas (6 filas x 4 columnas, por ejemplo)
    private static final int SIZE = 24;

    // Inventario interno real
    private final ItemStackHandler inventory = new ItemStackHandler(SIZE) {

        @Override
        protected void onContentsChanged(int slot) {
            // Marca el bloque como modificado (guardado + sync)
            System.out.println("[BottleRackBlockEntity] onContentsChanged() - slot: " + slot);
            setChanged();
            // Sincronizar cambios a todos los clientes
            if (BottleRackBlockEntity.this.level != null && !BottleRackBlockEntity.this.level.isClientSide) {
                System.out.println("[BottleRackBlockEntity] Enviando sync a clientes...");
                BottleRackBlockEntity.this.level.sendBlockUpdated(
                    BottleRackBlockEntity.this.worldPosition,
                    BottleRackBlockEntity.this.getBlockState(),
                    BottleRackBlockEntity.this.getBlockState(),
                    3
                );
            }
        }
    };

    public BottleRackBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BOTTLE_RACK_BLOCK_ENTITY.get(), pos, state);
    }

    // =========================
    // ACCESO A INVENTARIO
    // =========================

    public ItemStack getBottle(int slot) {
        if (slot < 0 || slot >= SIZE)
            return ItemStack.EMPTY;
        return inventory.getStackInSlot(slot);
    }

    public void setBottle(int slot, ItemStack stack) {
        if (slot < 0 || slot >= SIZE)
            return;
        System.out.println("[BottleRackBlockEntity] setBottle() - slot: " + slot + ", item: " + stack.getItem() + ", isEmpty: " + stack.isEmpty());
        inventory.setStackInSlot(slot, stack);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    // =========================
    // SLOT LIBRE
    // =========================

    public int getFirstEmptySlot() {
        for (int i = 0; i < SIZE; i++) {
            if (inventory.getStackInSlot(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    // =========================
    // HIT DETECTION (GRID 6x4)
    // =========================

    // lx, ly deben estar en rango 0..1 (coordenadas locales del bloque)
    public int getSlotAtHitCoord(double lx, double ly) {

        // seguridad básica
        if (lx < 0 || lx > 1 || ly < 0 || ly > 1)
            return -1;

        // 6 filas x 4 columnas = 24 slots
        int cols = 4;
        int rows = 6;

        int col = (int) (lx * cols);
        int row = (int) (ly * rows);

        // clamp
        col = Math.max(0, Math.min(cols - 1, col));
        row = Math.max(0, Math.min(rows - 1, row));

        return row * cols + col;
    }

    // =========================
    // NBT SAVE / SYNC
    // =========================

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        // guardado automático completo del inventario
        System.out.println("[BottleRackBlockEntity] saveAdditional() - guardando inventario");
        tag.put("inventory", inventory.serializeNBT());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        System.out.println("[BottleRackBlockEntity] getUpdateTag() - creando tag para sync");
        return tag;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        System.out.println("[BottleRackBlockEntity] getUpdatePacket() - enviando packet");
        return ClientboundBlockEntityDataPacket.create(this);
    }

    // =========================
    // NBT LOAD
    // =========================

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        if (tag.contains("inventory")) {
            System.out.println("[BottleRackBlockEntity] load() - cargando inventario del NBT");
            inventory.deserializeNBT(tag.getCompound("inventory"));
        }
    }

    @Override
    public void onDataPacket(net.minecraft.network.Connection net, ClientboundBlockEntityDataPacket pkt) {
        System.out.println("[BottleRackBlockEntity] onDataPacket() - recibiendo datos del servidor");
        this.load(pkt.getTag());
    }
}
