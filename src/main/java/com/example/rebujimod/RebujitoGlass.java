package com.example.rebujimod;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class RebujitoGlass extends Item {

    public RebujitoGlass(Properties properties) {
        super(properties);
    }

    // CLICK DERECHO
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        player.startUsingItem(hand);

        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    // DURACION
    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    // ANIMACIÓN
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    // CUANDO TERMINA DE BEBER
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {

        if (entity instanceof Player player) {

            // estadística vanilla
            player.awardStat(Stats.ITEM_USED.get(this));

            // ejemplo: nausea + velocidad
            if (!level.isClientSide) {

                player.addEffect(new MobEffectInstance(
                        MobEffects.CONFUSION,
                        200,
                        0));

                player.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SPEED,
                        400,
                        1));
            }

            //creativo -> no consumir
            if (player.getAbilities().instabuild) {
            return stack;
        }
        }

        

        return new ItemStack(ModItems.REBUJITO_GLASS_VOID.get());
    }

}
