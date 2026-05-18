package com.example.rebujimod;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(ExampleMod.MODID)
public class ExampleMod {

    public static final String MODID = "rebujimod";

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Block> EXAMPLE_BLOCK =
            BLOCKS.register("example_block",
                    () -> new Block(BlockBehaviour.Properties.of(Material.STONE)));

    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM =
            ITEMS.register("example_block",
                    () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));

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

    // ✅ Vid uva verde
    public static final RegistryObject<Block> GREEN_GRAPE_VINE =
            BLOCKS.register("green_grape_vine",
                    () -> new GreenGrapeVineBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));


    // ✅ Uva morada
    public static final RegistryObject<Item> PURPLE_GRAPE =
            ITEMS.register("purple_grape",
                    () -> new Item(new Item.Properties()));


    public ExampleMod(FMLJavaModLoadingContext context) {

        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    private void addCreative(CreativeModeTabEvent.BuildContents event) {

        if (event.getTab() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(EXAMPLE_BLOCK_ITEM);
        }

        // ✅ añadir item al creativo (TAB GENERAL DE ITEMS)
        if (event.getTab() == CreativeModeTabs.INGREDIENTS) {
            event.accept(GREEN_GRAPE);
        }

        // ✅ añadir item al creativo (TAB GENERAL DE ITEMS)
        if (event.getTab() == CreativeModeTabs.INGREDIENTS) {
            event.accept(PURPLE_GRAPE);
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
        }
    }
}