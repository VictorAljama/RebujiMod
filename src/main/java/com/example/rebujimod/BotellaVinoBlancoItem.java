package com.example.rebujimod;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class BotellaVinoBlancoItem extends Item {

    private static final FoodProperties FOOD = new FoodProperties.Builder()
            .nutrition(5)
            .saturationMod(0.3F)
            .build();

    public BotellaVinoBlancoItem(Properties properties) {
        super(properties.food(FOOD));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);

        if (entity instanceof Player player) {
            player.awardStat(Stats.ITEM_USED.get(this));

            if (!level.isClientSide) {
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 1200, 0));
            }

            if (player.getAbilities().instabuild) {
                return result;
            }

            return new ItemStack(ModItems.BOTELLA_VINO_VACIA.get());
        }

        return result;
    }
}
