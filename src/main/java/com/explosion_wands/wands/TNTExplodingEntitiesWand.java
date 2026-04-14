package com.explosion_wands.wands;

import com.explosion_wands.customFunctions.CustomTnt;
import com.explosion_wands.entity.ModEntities;
import com.explosion_wands.sharedValues.ExplosionEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;
import java.util.Random;
import java.util.function.Predicate;

public class TNTExplodingEntitiesWand {

    public static InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (level instanceof ServerLevel serverLevel && !level.isClientSide()) {
            int maxEntities = ExplosionEntities.maxEntities;
            int fuse = ExplosionEntities.fuse;
            int spawnedEntities = ExplosionEntities.spawnedEntities;
            float minExplosion = ExplosionEntities.minExplosion;
            float maxExplosion = ExplosionEntities.maxExplosion;
            int minIncrement = ExplosionEntities.minIncrement;
            int maxIncrement = ExplosionEntities.maxIncrement;
            int minRandomEntities = ExplosionEntities.minRandomEntity;
            int maxRandomEntities = ExplosionEntities.maxRandomEntity;
            Random random = new Random();
            double maxRandomPos = ExplosionEntities.maxRandomPos;
            double minRandomPos = ExplosionEntities.minRandomPos;
            float randomExplosion = (minExplosion + random.nextFloat() * (maxExplosion - minExplosion));
            int randomIncrement = minIncrement + random.nextInt(maxIncrement - minIncrement);
            int randomEntity = minRandomEntities + random.nextInt(maxRandomEntities - minRandomEntities);
            double randomPos = (maxRandomPos + random.nextDouble() * (maxRandomPos - minRandomPos));
            int increment = ExplosionEntities.increment;
            double lessThanTheta = ExplosionEntities.lessThanTheta;
            double lessThanPhi = ExplosionEntities.lessThanPhi;
            double incrementTheta = ExplosionEntities.incrementTheta;
            double incrementPhi = ExplosionEntities.incrementPhi;
            double x = ExplosionEntities.x;
            double y = ExplosionEntities.y;
            double z = ExplosionEntities.z;
            double r = ExplosionEntities.r;
            Vec3 dir = new Vec3(0, 0, 0);
            int spawnHeight = ExplosionEntities.spawnHeight;
            int reachEntities = ExplosionEntities.reachEntities;
            int reachBlock = ExplosionEntities.reachBlock;
            int inflate = ExplosionEntities.inflate;
            Vec3 playerEyeStart = player.getEyePosition(0);
            Vec3 playerLookAngle = player.getLookAngle();
            Vec3 playerEyeEnd = playerEyeStart.add(playerLookAngle.scale(reachBlock));
            CustomTnt customTnt = ModEntities.CUSTOM_TNT.create(level);
            assert customTnt != null;
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
            EntityType<?> entityToSpawn = EntityType.CHICKEN;
            //For debugging purposes
            String entityType = "";

            Vec3 target = blockHitResult.getLocation();
            if (entityHitResult != null) {
                target = entityHitResult.getEntity().position();
            }
            //Failsafe in-case we spawn more entities than is intended
            if (spawnedEntities <= maxEntities) {
                for (double theta = ExplosionEntities.theta; theta <= lessThanTheta; theta += incrementTheta) {
                    for (double phi = ExplosionEntities.phi; phi <= lessThanPhi; phi += incrementPhi) {
                        if (randomEntity <= spawnedEntities / 8 && spawnedEntities >= 0) {
                            entityToSpawn = EntityType.CHICKEN;
                            entityType = entityToSpawn.toString();
                        }
                        if (randomEntity <= (spawnedEntities / 4) && randomEntity > (spawnedEntities / 8)) {
                            //Breezes don't seem to be an enemy in the version, so replaces them with Blazes instead
                            entityToSpawn = EntityType.BLAZE;
                            entityType = entityToSpawn.toString();
                        }
                        if (randomEntity <= (spawnedEntities / 8) * 2 + (spawnedEntities / 8) && randomEntity > (spawnedEntities / 4)) {
                            entityToSpawn = EntityType.COW;
                            entityType = entityToSpawn.toString();
                        }
                        if (randomEntity <= spawnedEntities / 2 && randomEntity > (spawnedEntities / 8) * 2 + (spawnedEntities / 8)) {
                            entityToSpawn = EntityType.BAT;
                            entityType = entityToSpawn.toString();
                        }
                        if (randomEntity <= (spawnedEntities / 2) + (spawnedEntities / 8) && randomEntity > (spawnedEntities / 2)) {
                            //Armadillos aren't in this version, so we replace them with bees instead
                            entityToSpawn = EntityType.BEE;
                            entityType = entityToSpawn.toString();
                        }
                        if (randomEntity <= (spawnedEntities / 2) + (spawnedEntities / 4) && randomEntity > (spawnedEntities / 2) + (spawnedEntities / 8)) {
                            //Goats don't exist in this version, so replaces them with instead wolves instead
                            entityToSpawn = EntityType.WOLF;
                            entityType = entityToSpawn.toString();
                        }
                        if (randomEntity <= spawnedEntities - (spawnedEntities / 8) && randomEntity > (spawnedEntities / 2) + (spawnedEntities / 4)) {
                            entityToSpawn = EntityType.CREEPER;
                            entityType = entityToSpawn.toString();
                        }
                        if (randomEntity <= spawnedEntities && randomEntity > spawnedEntities - (spawnedEntities / 8)) {
                            entityToSpawn = EntityType.SNOW_GOLEM;
                            entityType = entityToSpawn.toString();
                        }
                        Entity entity = entityToSpawn.create(level);
                        customTnt = ModEntities.CUSTOM_TNT.create(level);
                        //This does not make a perfect circle, but it should not be noticeable
                        if (increment <= randomExplosion && customTnt != null) {
                            customTnt.setPos(target.x,
                                    target.y + spawnHeight,
                                    target.z
                            );
                            serverLevel.addFreshEntity(customTnt);
                            customTnt.setFuse(fuse);
                            customTnt.setExplosionPower(randomIncrement);
                        }
                        if (entity != null) {
                            if (x != 0 && y != 0 && z != 0) {
                                entity.setPos(target.x + x,
                                        target.y + y + spawnHeight,
                                        target.z + z
                                );
                                serverLevel.addFreshEntity(entity);
                            } else {
                                entity.remove();
                            }
                            x = r * Math.sin(theta) * Math.cos(phi) + randomPos;
                            y = r * Math.cos(theta) + randomPos;
                            z = r * Math.sin(theta) * Math.sin(phi) + randomPos;
                            increment++;
                        }
                    }
                }
                //Debugging
                /*
                System.out.println(
                        "Pre-calculated entities:   " + spawnedEntities
                                + ",   entities:   " + increment
                                + ",   random explosion:   " + randomExplosion
                                + ",   random increment:   " + randomIncrement
                );
                */
                /*
                System.out.println(
                        ",   random entity number:    " + randomEntity
                                + ",   entity type: " + entityType
                );
                 */

            }
        }
        return InteractionResultHolder.success(itemStack);
    }
}
