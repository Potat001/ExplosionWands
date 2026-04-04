package com.explosion_wands.wands;

import com.explosion_wands.customFunctions.CustomTnt;
import com.explosion_wands.entity.ModEntities;
import com.explosion_wands.sharedValues.ExplosionEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class FireballScatterWand {

    public static InteractionResult use(Level level, Player player)  {
        if (level instanceof ServerLevel serverLevel && player != null && !level.isClientSide()) {
            int maxEntities = ExplosionEntities.maxEntities;
            int fuse = ExplosionEntities.fuse;
            int spawnedEntities = ExplosionEntities.spawnedEntities;
            RandomSource random = RandomSource.create();
            double maxRandomPos = ExplosionEntities.maxRandomPos;
            double minRandomPos = ExplosionEntities.minRandomPos;
            double randomPos = (maxRandomPos + random.nextDouble() * (maxRandomPos - minRandomPos));
            int fireballExplosionPower = 8;
            int increment = ExplosionEntities.increment;
            float randomExplosion = 0;
            double lessThanTheta = ExplosionEntities.lessThanTheta;
            lessThanTheta = lessThanTheta / 2;
            double lessThanPhi = ExplosionEntities.lessThanPhi;
            double incrementTheta;
            incrementTheta = 0.5;
            double incrementPhi;
            incrementPhi = 0.5;
            double x = ExplosionEntities.x;
            double y = ExplosionEntities.y;
            double z = ExplosionEntities.z;
            double r;
            r = 8;
            int spawnHeight;
            spawnHeight = 17;
            float explosionPower = 0F;
            int reachEntities = ExplosionEntities.reachEntities;
            int reachBlock = ExplosionEntities.reachBlock;
            int inflate = ExplosionEntities.inflate;
            Vec3 playerEyeStart = player.getEyePosition();
            Vec3 playerLookAngle = player.getLookAngle();
            Vec3 playerEyeEnd = playerEyeStart.add(playerLookAngle.scale(reachBlock));
            LargeFireball fireball = new LargeFireball(level, player, playerLookAngle, fireballExplosionPower);
            BlockHitResult blockHitResult = level.clip(new ClipContext(
                    playerEyeStart,
                    playerEyeEnd,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    player
            ));
            EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                    level,
                    fireball,
                    playerEyeStart,
                    playerEyeEnd,
                    player.getBoundingBox().expandTowards(playerLookAngle.scale(reachEntities)).inflate(inflate),
                    //Makes it so that we can hit any type of entity
                    entity -> entity instanceof Entity
                    //Ensures that we can't hit the hitbox of dead entities
                    && entity.isAlive()
                    && !entity.isRemoved()
                    && entity != player);
            BlockPos target = blockHitResult.getBlockPos();
            if(entityHitResult != null) {
                target = entityHitResult.getEntity().blockPosition();
            }
            //Failsafe in-case we spawn more entities than is intended
            if(spawnedEntities <= maxEntities) {
                for (double theta = ExplosionEntities.theta; theta <= lessThanTheta; theta += incrementTheta) {
                    for (double phi = ExplosionEntities.phi; phi <= lessThanPhi; phi += incrementPhi) {
                        fireball = new LargeFireball(level, player, playerLookAngle, fireballExplosionPower);
                        CustomTnt customTnt = ModEntities.CUSTOM_TNT.create(level, EntitySpawnReason.TRIGGERED);
                        //This does not make a perfect circle, but it should not be noticeable
                        if (increment <= randomExplosion && customTnt != null) {
                            customTnt.setPos(target.getX(),
                                    target.getY() + spawnHeight,
                                    target.getZ()
                            );
                            serverLevel.addFreshEntity(customTnt);
                            customTnt.setFuse(fuse);
                            customTnt.setExplosionPower(explosionPower);
                        }
                        //Creates fireball every iteration
                        //X dir: cos, Z dir: sin, makes a circle
                        if(x != 0 && y != 0 && z != 0) {
                            fireball.setPos(target.getX() + x,
                                    target.getY() - y + spawnHeight,
                                    target.getZ() - z
                            );
                            fireball.addTag("fireball");
                            serverLevel.addFreshEntity(fireball);
                        } else {
                            fireball.discard();
                        }
                        //Changes the initial angle by the value of angleStep every iteration so the TNTs are not static
                        //Height of the cos curve every iteration
                        x = r * Math.sin(theta) * Math.cos(phi) + randomPos;
                        y = r * Math.cos(theta) + randomPos;
                        z = r * Math.sin(theta) * Math.sin(phi) + randomPos;
                        increment++;
                    }
                }
            }
        }
        return InteractionResult.SUCCESS;
    }
}