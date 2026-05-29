package com.example.rebujimod;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;

public class ModBlocks {

      public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
                  RebujiMod.MODID);

      // ✅ Vid uva verde
      public static final RegistryObject<Block> GREEN_GRAPE_VINE = BLOCKS.register("green_grape_vine",
                  () -> new GrapeVineBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).strength(1.3f, 1.3f)));

      // ✅ Vid uva morada
      public static final RegistryObject<Block> PURPLE_GRAPE_VINE = BLOCKS.register("purple_grape_vine",
                  () -> new GrapeVineBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).strength(1.3f, 1.3f)));

      // ✅ Barrica
      public static final RegistryObject<Block> BARRICA = BLOCKS.register("barrica",
                  () -> new BarrelBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).strength(2f, 1.2f)));

      // ✅ Tronco de abedul de resina (con corteza y resina)
      public static final RegistryObject<Block> RESIN_BIRCH_LOG = BLOCKS.register("resin_birch_log",
                  () -> new ResinBirchLogBlock(BlockBehaviour.Properties.copy(Blocks.BIRCH_LOG).strength(2f, 1.2f)));

      // ✅ Tronco de abedul de resina (sin corteza, regenera)
      public static final RegistryObject<Block> RESIN_BIRCH_LOG_EMPTY = BLOCKS.register("resin_birch_log_empty",
                  () -> new ResinBirchLogEmptyBlock(BlockBehaviour.Properties.copy(Blocks.BIRCH_LOG).strength(2f, 1.2f)));

}
