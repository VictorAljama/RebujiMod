package com.example.rebujimod;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


@Mod(RebujiMod.MODID)
public class RebujiMod {

    public static final String MODID = "rebujimod";

    private static final Logger LOGGER = LogUtils.getLogger();

    public RebujiMod(FMLJavaModLoadingContext context) {

        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    private void addCreative(CreativeModeTabEvent.BuildContents event) {

        // ✅ añadir item al creativo (TAB GENERAL DE ITEMS)
        if (event.getTab() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.GREEN_GRAPE);
        }

        // ✅ añadir item al creativo (TAB GENERAL DE ITEMS)
        if (event.getTab() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.PURPLE_GRAPE);
        }

        // ✅ añadir orange_onion al creativo (TAB GENERAL DE ITEMS)
        if (event.getTab() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.ORANGE_ONION);
        }

        // ✅ añadir aubergine al creativo (TAB GENERAL DE ITEMS)
        if(event.getTab() == CreativeModeTabs.INGREDIENTS){
            event.accept(ModItems.AUBERGINE);
        }

        // ✅ añadir rebujito_glass al creativo (TAB GENERAL DE ITEMS)
        if(event.getTab() == CreativeModeTabs.INGREDIENTS){
            event.accept(ModItems.REBUJITO_GLASS);
        }

        // ✅ añadir rebujito_glass_void al creativo (TAB GENERAL DE ITEMS)
        if(event.getTab() == CreativeModeTabs.INGREDIENTS){
            event.accept(ModItems.REBUJITO_GLASS_VOID);
        }

        // ✅ añadir botella de vino vacía al creativo (TAB GENERAL DE ITEMS)
        if(event.getTab() == CreativeModeTabs.INGREDIENTS){
            event.accept(ModItems.BOTELLA_VINO_VACIA);
        }

        // ✅ añadir botella de vino tinto al creativo (TAB GENERAL DE ITEMS)
        if(event.getTab() == CreativeModeTabs.INGREDIENTS){
            event.accept(ModItems.BOTELLA_VINO_TINTO);
        }

        // ✅ añadir botella de vino blanco al creativo (TAB GENERAL DE ITEMS)
        if(event.getTab() == CreativeModeTabs.INGREDIENTS){
            event.accept(ModItems.BOTELLA_VINO_BLANCO);
        }

        // ✅ añadir rebujito_jug_void al creativo (TAB GENERAL DE ITEMS)
        if(event.getTab() == CreativeModeTabs.INGREDIENTS){
            event.accept(ModItems.REBUJITO_JUG_VOID);
        }

        // ✅ añadir rebujito_jug_ al creativo (TAB GENERAL DE ITEMS)
        if(event.getTab() == CreativeModeTabs.INGREDIENTS){
            event.accept(ModItems.REBUJITO_JUG);
        }

        // ✅ añadir barrica al creativo (TAB GENERAL DE ITEMS)
        if(event.getTab() == CreativeModeTabs.BUILDING_BLOCKS){
            event.accept(ModItems.BARRICA);
        }

            // ✅ añadir botellero al creativo (TAB BLOQUES DE CONSTRUCCIÓN)
            if(event.getTab() == CreativeModeTabs.BUILDING_BLOCKS){
                event.accept(ModItems.BOTELLERO);
            }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            // Registrar renderer del botellero
            net.minecraft.client.renderer.blockentity.BlockEntityRenderers.register(
                    com.example.rebujimod.ModBlockEntities.BOTTLE_RACK_BLOCK_ENTITY.get(),
                    com.example.rebujimod.client.BottleRackRenderer::new);
        }
    }
}
