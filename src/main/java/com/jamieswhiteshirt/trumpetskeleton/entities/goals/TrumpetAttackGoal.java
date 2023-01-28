package com.jamieswhiteshirt.trumpetskeleton.entities.goals;

import com.jamieswhiteshirt.trumpetskeleton.register.Items;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.projectile.ProjectileHelper;

import java.util.EnumSet;

public class TrumpetAttackGoal<T extends MonsterEntity> extends Goal {
    private final T actor;
    private final double speed;
    private final float squaredRange;
    private int attackInterval;
    private int cooldown = -1;
    private int seeCounter;
    private boolean strafeLeft;
    private boolean strafeBack;
    private int strafeChangeTimer = -1;

    public TrumpetAttackGoal(T actor, double speed, int attackInterval, float range) {
        this.actor = actor;
        this.speed = speed;
        this.attackInterval = attackInterval;
        this.squaredRange = range * range;

        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public void setAttackInterval(int attackInterval) {
        this.attackInterval = attackInterval;
    }

    @Override
    public boolean canUse() {
        return actor.getTarget() != null && isHoldingTrumpet();
    }

    protected boolean isHoldingTrumpet() {
        return actor.isHolding(Items.TRUMPET_ITEM.get());
    }

    @Override
    public boolean canContinueToUse() {
        return (canUse() || !actor.getNavigation().isDone()) && isHoldingTrumpet();
    }

    @Override
    public void start() {
        super.start();

        actor.setAggressive(true);
    }

    @Override
    public void stop() {
        super.stop();
        actor.setAggressive(false);
        actor.stopUsingItem();

        seeCounter = 0;
        cooldown = -1;
    }

    @Override
    public void tick() {
        LivingEntity target = actor.getTarget();

        if (target == null) return;

        double squaredDistance = actor.distanceToSqr(target);
        boolean canSeeTarget = actor.canSee(target);
        boolean bool2 = seeCounter > 0;

        if (canSeeTarget != bool2) {
            seeCounter = 0;
        }

        if (canSeeTarget) seeCounter += 1;
        else seeCounter -= 1;

        if (squaredDistance <= squaredRange && seeCounter >= 20) {
            actor.getNavigation().stop();
            strafeChangeTimer += 1;
        } else {
            actor.getNavigation().moveTo(target, speed);
            strafeChangeTimer -= 1;
        }

        if (strafeChangeTimer >= 20) {
            if (actor.level.random.nextFloat() < 0.3) {
                strafeLeft = !strafeLeft;
            }

            if (actor.level.random.nextFloat() < 0.3) {
                strafeBack = !strafeBack;
            }

            strafeChangeTimer = 0;
        }

        if (strafeChangeTimer > -1) {
            if (squaredDistance > squaredRange * 0.75) {
                strafeBack = false;
            } else if (squaredDistance < squaredRange * 0.25) {
                strafeBack = true;
            }

            actor.getMoveControl().strafe(
                    strafeBack ? -0.5f : 0.5f,
                    strafeLeft ? 0.5f : -0.5f
            );

            actor.getLookControl().setLookAt(target, 30, 30);
        }

        if (actor.isUsingItem()) {
            if (!canSeeTarget && seeCounter < -60) {
                actor.stopUsingItem();
            }
        } else if (--cooldown <= 0 && seeCounter >= -60) {
            actor.startUsingItem(ProjectileHelper.getWeaponHoldingHand(actor, Items.TRUMPET_ITEM.get()));
            cooldown = actor.level.random.nextInt(attackInterval);
        }
    }
}
