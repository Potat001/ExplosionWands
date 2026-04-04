package com.explosion_wands.wands;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import java.util.ArrayList;
import java.util.List;

public class FireballBarrageWand {
    private static final List<Runnable> QUEUE = new ArrayList<>();

    public static void add(Runnable task) {
        QUEUE.add(task);
    }
    public static InteractionResult use(Level level, Player player) {
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
        if(player != null && level instanceof ServerLevel serverLevel) {
            Vec3 dir = new Vec3(xDir, yDir, zDir);
            double angle = Math.toRadians(player.getYRot() + degrees);
            //Makes the fireballs equally spread out
            double angleStep = Math.PI / ((double) newFireballAmount);
            Vec3 playerEyeStart = player.getEyePosition();
            //Also how far away the fireballs spawn from the player
            Vec3 playerLookAngle = player.getLookAngle();
            Vec3 playerEyeEnd = playerEyeStart.add(playerLookAngle.scale(reachBlock));
            LargeFireball largeFireball = new LargeFireball(
                    level,
                    player,
                    dir,
                    explosionPower);
            EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                    level,
                    largeFireball,
                    playerEyeStart,
                    playerEyeEnd,
                    player.getBoundingBox().expandTowards(dir.scale(reachEntities)).inflate(inflate),
                    //Makes it so that we can hit any type of entity
                    entity -> entity instanceof Entity
                    //Ensures that we can't hit the hitbox of dead entities
                    && entity.isAlive()
                    && !entity.isRemoved()
                    && entity != player);
            BlockHitResult blockHitResult = level.clip(new ClipContext(
                    playerEyeStart,
                    playerEyeEnd,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    player
            ));
            BlockPos target = blockHitResult.getBlockPos();
            if(entityHitResult != null) {
                target = entityHitResult.getEntity().blockPosition();
            }
            for (int i = 0; i < fireballAmount; i++) {
                largeFireball = new LargeFireball(
                        level,
                        player,
                        dir,
                        explosionPower);
                largeFireball.setPos(
                        target.getX() + (Math.cos(angle) * amplitude),
                        target.getY() + spawnHeight,
                        target.getZ() + (Math.sin(angle) * amplitude)
                );
                largeFireball.setDeltaMovement(xDir, yDir, zDir);
                largeFireball.addTag("fireball");
                serverLevel.addFreshEntity(largeFireball);
                angle += angleStep;
            }
                serverLevel.playSound(null,
                        target.getX(),
                        target.getY() + spawnHeightSound,
                        target.getZ(),
                        SoundEvents.FIRECHARGE_USE,
                        SoundSource.PLAYERS,
                        volume,
                        pitch);
        }
        return InteractionResult.SUCCESS;
    }
}
