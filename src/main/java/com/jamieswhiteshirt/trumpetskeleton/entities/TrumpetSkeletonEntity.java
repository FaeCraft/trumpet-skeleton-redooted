package com.jamieswhiteshirt.trumpetskeleton.entities;

import com.jamieswhiteshirt.trumpetskeleton.entities.goals.TrumpetAttackGoal;
import com.jamieswhiteshirt.trumpetskeleton.register.Items;
import com.jamieswhiteshirt.trumpetskeleton.register.SoundEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class TrumpetSkeletonEntity extends SkeletonEntity {
    private final TrumpetAttackGoal<TrumpetSkeletonEntity> trumpetAttackGoal = new TrumpetAttackGoal<>(this, 1, 40, 6);
    private final MeleeAttackGoal meleeAttackGoal = new MeleeAttackGoal(this, 1.2, false) {
        @Override
        public void start() {
            super.start();
            setAggressive(true);
        }

        @Override
        public void stop() {
            super.stop();
            setAggressive(false);
        }
    };

    private final boolean constructed;

    public TrumpetSkeletonEntity(EntityType<? extends SkeletonEntity> p_i50194_1_, World p_i50194_2_) {
        super(p_i50194_1_, p_i50194_2_);

        constructed = true;
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(difficulty);

        startUsingItem(Hand.MAIN_HAND);
        setItemSlot(
                EquipmentSlotType.MAINHAND,
                new ItemStack(Items.TRUMPET_ITEM.get())
        );
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_DOOT.get();
    }

    @Override
    public void playAmbientSound() {
        if (!isAggressive()) super.playAmbientSound();
    }

    @Override
    public void reassessWeaponGoal() {
        if (constructed && this.level != null && !this.level.isClientSide) {
            goalSelector.removeGoal(meleeAttackGoal);
            goalSelector.removeGoal(trumpetAttackGoal);

            ItemStack stack = getItemInHand(ProjectileHelper.getWeaponHoldingHand(this, Items.TRUMPET_ITEM.get()));

            if (stack.getItem() == Items.TRUMPET_ITEM.get()) {
                int attackInterval = 40;

                if (level.getDifficulty() != Difficulty.HARD) {
                    attackInterval = 80;
                }

                trumpetAttackGoal.setAttackInterval(attackInterval);
                goalSelector.addGoal(4, trumpetAttackGoal);
            } else {
                goalSelector.addGoal(4, meleeAttackGoal);
            }
        }
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(Items.TRUMPET_SKELETON_SPAWN_EGG.get());
    }
}
