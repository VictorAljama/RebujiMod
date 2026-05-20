package com.example.rebujimod;

import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class RebujitoJugBlock extends BushBlock{

    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 3);
    
    public RebujitoJugBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }
}
