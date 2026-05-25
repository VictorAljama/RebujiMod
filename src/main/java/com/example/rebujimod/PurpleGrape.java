package com.example.rebujimod;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class PurpleGrape extends Item {

    public static final FoodProperties FOOD = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(0.3F)
            .build();

    public PurpleGrape() {
        super(new Item.Properties().food(FOOD));
    }

    public PurpleGrape(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!player.canEat(false)) {
            return InteractionResultHolder.fail(itemstack);
        }
        return super.use(level, player, hand);
    }

    public boolean canEat(ItemStack stack, LivingEntity entity) {
        return entity instanceof Player player && player.canEat(false);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player && player.getAbilities().instabuild) {
            return stack;
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (level.getBlockState(pos).is(Blocks.GRASS_BLOCK) || level.getBlockState(pos).is(Blocks.DIRT)) {
            if (!level.isClientSide) {
                level.setBlock(pos.above(), ModBlocks.PURPLE_GRAPE_VINE.get().defaultBlockState(), 3);
                Player player = context.getPlayer();
                if (player == null || !player.getAbilities().instabuild) {
                    context.getItemInHand().shrink(1);
                }
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
