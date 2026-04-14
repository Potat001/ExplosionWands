package com.explosion_wands.wands;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class FireballBarrageWand {
    private static final List<Runnable> QUEUE = new ArrayList<>();

    public static void add(Runnable task) {
        QUEUE.add(task);
    }

    public static InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        float volume = 0.4F;
        float pitch = 1.0F;
        int reachEntities = 128;
        int reachBlock = 512;
        int inflate = 100;
        int spawnHeight = 50;
        int spawnHeightSound = 10;
        double amplitude = 15;
        int fireballAmount = 40;
        int newFireballAmount = fireballAmount / 2;
        int explosionPower = 10;
        //Direction the fireballs will head towards, and the speed of the fireballs
        double xDir = 0;
        double yDir = -2;
        double zDir = 0;
        int degrees = 90;
        if(level instanceof ServerLevel serverLevel) {
            Vec3 dir = new Vec3(xDir, yDir, zDir);
            double angle = Math.toRadians(player.getViewYRot(0) + degrees);
            //Makes the fireballs equally spread out
            double angleStep = Math.PI / ((double) newFireballAmount);
            Vec3 playerEyeStart = player.getEyePosition(0);
            //Also how far away the fireballs spawn from the player
            Vec3 playerLookAngle = player.getLookAngle();
            Vec3 playerEyeEnd = playerEyeStart.add(playerLookAngle.scale(reachBlock));
            LargeFireball largeFireball = new LargeFireball(
                    level,
                    player,
                    xDir,
                    yDir,
                    zDir
            );
            largeFireball.explosionPower = explosionPower;
            EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(Objects.requireNonNull(level.getEntity(0)),
                    playerEyeStart,
                    playerEyeEnd,
                    player.getBoundingBox().expandTowards(dir.scale(reachEntities)).inflate(inflate),
                    Predicate.isEqual(playerLookAngle),
                    0);
            BlockHitResult blockHitResult = level.clip(new ClipContext(
                    playerEyeStart,
                    playerEyeEnd,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    player
            ));
            Vec3 target = blockHitResult.getLocation();
            if(entityHitResult != null) {
                target = entityHitResult.getEntity().position();
            }
            for (int i = 0; i < fireballAmount; i++) {
                largeFireball = new LargeFireball(
                        level,
                        player,
                        xDir,
                        yDir,
                        zDir
                );
                largeFireball.explosionPower = explosionPower;
                largeFireball.setPos(
                        target.x + (Math.cos(angle) * amplitude),
                        target.y + spawnHeight,
                        target.z + (Math.sin(angle) * amplitude)
                );
                largeFireball.setDeltaMovement(xDir, yDir, zDir);
                largeFireball.addTag("fireball");
                serverLevel.addFreshEntity(largeFireball);
                angle += angleStep;
            }
                serverLevel.playSound(null,
                        target.x,
                        target.y + spawnHeightSound,
                        target.z,
                        SoundEvents.FIRECHARGE_USE,
                        SoundSource.PLAYERS,
                        volume,
                        pitch);
        }
        return InteractionResultHolder.success(itemStack);
    }
}
