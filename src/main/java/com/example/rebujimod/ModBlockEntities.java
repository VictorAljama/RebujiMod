package com.example.rebujimod;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RebujiMod.MODID);

    public static final RegistryObject<BlockEntityType<BarrelBlockEntity>> BARREL_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("barrel_block_entity",
                    () -> BlockEntityType.Builder.of(BarrelBlockEntity::new, ModBlocks.BARRICA.get()).build(null));

    public static final RegistryObject<BlockEntityType<BottleRackBlockEntity>> BOTTLE_RACK_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("bottle_rack_block_entity",
                    () -> BlockEntityType.Builder.of(BottleRackBlockEntity::new, ModBlocks.BOTELLERO.get()).build(null));
}
