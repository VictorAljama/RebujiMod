package com.example.rebujimod;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
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
                        () -> new RebujitoGlassVoidItem(new Item.Properties()));

        // ✅ Botella de vino vacía
        public static final RegistryObject<Item> BOTELLA_VINO_VACIA = ITEMS.register("botella_vino_vacia",
                        () -> new BotellaVinoVaciaItem(new Item.Properties().stacksTo(64)));

        // ✅ Botella de vino tinto
        public static final RegistryObject<Item> BOTELLA_VINO_TINTO = ITEMS.register("botella_vino_tinto",
                        () -> new BotellaVinoTintoItem(new Item.Properties().stacksTo(16)));

        // ✅ Botella de vino blanco
        public static final RegistryObject<Item> BOTELLA_VINO_BLANCO = ITEMS.register("botella_vino_blanco",
                        () -> new BotellaVinoBlancoItem(new Item.Properties().stacksTo(16)));

        // ✅ Rebujito Jug Void
        public static final RegistryObject<Item> REBUJITO_JUG_VOID = ITEMS.register("rebujito_jug_void",
                        () -> new Item(new Item.Properties()));

        // ✅ Rebujito Jug
        public static final RegistryObject<Item> REBUJITO_JUG = ITEMS.register("rebujito_jug",
                        () -> new Item(new Item.Properties()));

        // ✅ Uva verde
        public static final RegistryObject<Item> GREEN_GRAPE = ITEMS.register("green_grape",
                        GreenGrape::new);

        // ✅ Uva morada
        public static final RegistryObject<Item> PURPLE_GRAPE = ITEMS.register("purple_grape",
                        PurpleGrape::new);

        // ✅ Barrica (item stackeable por defecto y coloca bloque al usarlo)
        public static final RegistryObject<Item> BARRICA = ITEMS.register("barrica",
                        () -> new BlockItem(ModBlocks.BARRICA.get(), new Item.Properties().stacksTo(64)));

        // ✅ Botellero (item stackeable y coloca bloque al usarlo)
        public static final RegistryObject<Item> BOTELLERO = ITEMS.register("botellero",
                        () -> new BlockItem(ModBlocks.BOTELLERO.get(), new Item.Properties().stacksTo(64)));

}
