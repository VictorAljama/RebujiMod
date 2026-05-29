package com.example.rebujimod.state;

import net.minecraft.util.StringRepresentable;

public enum ResinState implements StringRepresentable {
    ACTIVE,
    EMPTY;

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }
}
