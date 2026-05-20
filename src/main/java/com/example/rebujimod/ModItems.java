package com.example.rebujimod;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class ModItems {

        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
                        RebujiMod.MODID);

        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RebujiMod.MODID);

        // ✅ Vid uva verde
        public static final RegistryObject<Block> GREEN_GRAPE_VINE = BLOCKS.register("green_grape_vine",
                        () -> new GrapeVineBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).strength(1.3f, 1.3f)));

        // ✅ Vid uva morada
        public static final RegistryObject<Block> PURPLE_GRAPE_VINE = BLOCKS.register("purple_grape_vine",
                        () -> new GrapeVineBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).strength(1.3f, 1.3f)));

        // ✅ Orange Onion
        public static final RegistryObject<Item> ORANGE_ONION = ITEMS.register("orange_onion",
                        () -> new Item(new Item.Properties()));

        // ✅ Aubergine
        public static final RegistryObject<Item> AUBERGINE = ITEMS.register("aubergine",
                        () -> new Item(new Item.Properties()));

        // ✅ Rebujito Glass
        public static final RegistryObject<Item> REBUJITO_GLASS = ITEMS.register("rebujito_glass",
                        () -> new RebujitoGlass(new Item.Properties().stacksTo(1)));

        // ✅ Rebujito Glass Void
        public static final RegistryObject<Item> REBUJITO_GLASS_VOID = ITEMS.register("rebujito_glass_void",
                        () -> new RebujitoGlassVoidItem (new Item.Properties()));

        // ✅ Rebujito Jug Void
        public static final RegistryObject<Item> REBUJITO_JUG_VOID = ITEMS.register("rebujito_jug_void",
                        () -> new Item(new Item.Properties()));

        // ✅ Rebujito Jug
        public static final RegistryObject<Item> REBUJITO_JUG = ITEMS.register("rebujito_jug",
                        () -> new Item(new Item.Properties()));

         // ✅ Uva verde
    public static final RegistryObject<Item> GREEN_GRAPE =
            ITEMS.register("green_grape",
                    () -> new Item(new Item.Properties()) {

                        @Override
                        public InteractionResult useOn(UseOnContext context) {

                            Level level = context.getLevel();
                            BlockPos pos = context.getClickedPos();
                            BlockState state = level.getBlockState(pos);

                            if (state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT)) {

                                if (!level.isClientSide) {
                                    level.setBlock(pos.above(),
                                            GREEN_GRAPE_VINE.get().defaultBlockState(),
                                            3);
                                    context.getItemInHand().shrink(1);
                                }

                                return InteractionResult.SUCCESS;
                            }

                            return InteractionResult.PASS;
                        }
                    });

    // ✅ Uva morada
    public static final RegistryObject<Item> PURPLE_GRAPE = ITEMS.register("purple_grape",
            () -> new Item(new Item.Properties()) {

                @Override
                public InteractionResult useOn(UseOnContext context) {

                    Level level = context.getLevel();
                    BlockPos pos = context.getClickedPos();
                    BlockState state = level.getBlockState(pos);

                    if (state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT)) {

                        if (!level.isClientSide) {
                            level.setBlock(pos.above(),
                                    PURPLE_GRAPE_VINE.get().defaultBlockState(),
                                    3);
                            context.getItemInHand().shrink(1);
                        }

                        return InteractionResult.SUCCESS;
                    }

                    return InteractionResult.PASS;
                }
            });

        
}