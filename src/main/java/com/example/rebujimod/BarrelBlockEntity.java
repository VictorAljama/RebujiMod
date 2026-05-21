package com.example.rebujimod;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BarrelBlockEntity extends BlockEntity {
    private int interactionCount = 0; // Contador de interacciones

    public BarrelBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BARREL_BLOCK_ENTITY.get(), pos, state);
    }

    public void incrementInteractionCount() {
        this.interactionCount++;
    }

    public int getInteractionCount() {
        return this.interactionCount;
    }

    public void resetInteractionCount() {
        this.interactionCount = 0;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.interactionCount = tag.getInt("InteractionCount");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("InteractionCount", this.interactionCount);
    }
}
