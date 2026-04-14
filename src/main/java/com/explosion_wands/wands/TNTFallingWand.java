package com.explosion_wands.wands;

import com.explosion_wands.customFunctions.CustomTnt;
import com.explosion_wands.entity.ModEntities;
import com.explosion_wands.sharedValues.ExplosionEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
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

public class TNTFallingWand {

    public static InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)  {
        ItemStack itemStack = player.getItemInHand(hand);
        if (level instanceof ServerLevel serverLevel) {
            int maxEntities = ExplosionEntities.maxEntities;
            int fuse = ExplosionEntities.fuse;
            int spawnedEntities = ExplosionEntities.spawnedEntities;
            float minExplosion = 1F;
            float maxExplosion = 2F;
            int secondFuse = 200;
            boolean explodeOnContact = true;
            float explosionPower = 10.0F;
            int particleThickness = 700;
            int particleSpeed = 1;
            int moduloParticle = 12;
            int moduloRest = 1;
            int minIncrement = ExplosionEntities.minIncrement;
            int maxIncrement = ExplosionEntities.maxIncrement;
            Random random = new Random();
            float randomExplosion = (minExplosion + random.nextFloat() * (maxExplosion - minExplosion));
            int randomIncrement = minIncrement + random.nextInt(maxIncrement - minIncrement);
            double min = 1.0;
            double max = 4.0;
            double randomDistr = min + random.nextDouble() * (max - min);
            int increment = ExplosionEntities.increment;
            double lessThanTheta = ExplosionEntities.lessThanTheta;
            double lessThanPhi = ExplosionEntities.lessThanPhi;
            double incrementTheta = ExplosionEntities.incrementTheta;
            double incrementPhi = ExplosionEntities.incrementPhi;
            double x = ExplosionEntities.x;
            double y = ExplosionEntities.y;
            double z = ExplosionEntities.z;
            double r = 1.5;
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
            Vec3 target = blockHitResult.getLocation();
            if(entityHitResult != null) {
                target = entityHitResult.getEntity().position();
            }
            //Failsafe in-case we spawn more entities than is intended
            if(spawnedEntities <= maxEntities) {
                for (double theta = ExplosionEntities.theta; theta <= lessThanTheta; theta += incrementTheta) {
                    for (double phi = ExplosionEntities.phi; phi <= lessThanPhi; phi += incrementPhi) {
                        //Adds the entities to the world
                        customTnt = ModEntities.CUSTOM_TNT.create(level);
                        CustomTnt customTnt2 = ModEntities.CUSTOM_TNT.create(level);
                        //This does not make a perfect circle, but it should not be noticeable
                            if (increment <= randomExplosion && customTnt != null) {
                                customTnt.setPos(target.x,
                                        target.y + spawnHeight,
                                        target.z
                                );
                                customTnt.setFuse(fuse);
                                customTnt.setExplosionPower(randomIncrement);
                                serverLevel.addFreshEntity(customTnt);
                            }
                        if (customTnt2 != null) {
                            if (x != 0 && y != 0 && z != 0) {
                                customTnt2.setPos(target.x + x,
                                        target.y + y + spawnHeight,
                                        target.z + z
                                );
                                customTnt2.setFuse(secondFuse);
                                customTnt2.setExplodeOnContact(explodeOnContact);
                                customTnt2.setExplosionPower(explosionPower);
                                serverLevel.addFreshEntity(customTnt2);
                                if ((increment % moduloParticle) == moduloRest) {
                                    //Particles only spawn 32 blocks away from the player. Might bypass in future
                                    serverLevel.sendParticles(ParticleTypes.DRAGON_BREATH, customTnt2.getX(), customTnt2.getY(), customTnt2.getZ(), particleThickness, randomDistr, randomDistr, randomDistr, particleSpeed);
                                }
                            } else {
                                customTnt2.remove();
                            }
                            x = r * Math.sin(theta) * Math.cos(phi);
                            y = r * Math.cos(theta);
                            z = r * Math.sin(theta) * Math.sin(phi);
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
                                + ",   random increment:   " + 1
                );
                */
            }
        }
        return InteractionResultHolder.success(itemStack);
    }
}
