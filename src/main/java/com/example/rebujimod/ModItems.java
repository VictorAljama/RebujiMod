package com.example.rebujimod;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
                        RebujiMod.MODID);

        // ✅ Orange Onion
        public static final RegistryObject<Item> ORANGE_ONION = ITEMS.register("orange_onion",
                        () -> new Item(new Item.Properties()));

        // ✅ Aubergine
        public static final RegistryObject<Item> AUBERGINE = ITEMS.register("aubergine",
                        () -> new Item(new Item.Properties()));

        // ✅ Rebujito Glass
        public static final RegistryObject<Item> REBUJITO_GLASS = ITEMS.register("rebujito_glass",
                        () -> new RebujitoGlass(new Item.Properties().stacksTo(5)));

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
    public static final RegistryObject<Item> GREEN_GRAPE = ITEMS.register("green_grape",
            () -> new GreenGrapeItem(new Item.Properties()));

    // ✅ Uva morada
    public static final RegistryObject<Item> PURPLE_GRAPE = ITEMS.register("purple_grape",
            () -> new PurpleGrapeItem(new Item.Properties()));

    // ✅ Barrica (item stackeable por defecto y coloca bloque al usarlo)
    public static final RegistryObject<Item> BARRICA = ITEMS.register("barrica",
            () -> new BlockItem(ModBlocks.BARRICA.get(), new Item.Properties().stacksTo(64)));

}
