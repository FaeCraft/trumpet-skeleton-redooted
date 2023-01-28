package com.jamieswhiteshirt.trumpetskeleton.items;

import com.jamieswhiteshirt.trumpetskeleton.register.SoundEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import java.util.List;

public class TrumpetItem extends Item {
    public TrumpetItem(Properties properties) {
        super(properties);
    }

    public static void scare(World world, LivingEntity user) {
        if (!world.isClientSide()) {
            List<LivingEntity> spooked = world.getEntitiesOfClass(
                    LivingEntity.class,
                    user.getBoundingBox().inflate(10.0)
            );

            for (LivingEntity entity : spooked) {
                if (entity == user) continue;

                double deltaX = entity.getX() - user.getX() + world.random.nextDouble() - world.random.nextDouble();
                double deltaZ = entity.getZ() - user.getZ() + world.random.nextDouble() - world.random.nextDouble();

                double distance = Math.sqrt((deltaX * deltaX) + (deltaZ * deltaZ));

                entity.hurtMarked = true;
                entity.setLastHurtByMob(user);

                entity.push(
                        0.5 * deltaX / distance,
                        5 / (10 + distance),
                        0.5 + deltaZ / distance
                );
            }
        }
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 55;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return null;
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        super.onUsingTick(stack, player, count);

        int useTime = getUseDuration(stack) - count;

        if (useTime == 10) {
            player.playSound(SoundEvents.TRUMPET_DOOT.get(), 1, 0.9F + player.level.random.nextFloat() * 0.2F);
            TrumpetItem.scare(player.level, player);
            stack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(entity.getUsedItemHand()));
        } else if (useTime >= 15) {
            player.stopUsingItem();
        }
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        playerIn.startUsingItem(handIn);
        return ActionResult.success(playerIn.getItemInHand(handIn));
    }
}
