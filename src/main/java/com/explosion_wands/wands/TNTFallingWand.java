package com.explosion_wands.wands;

import com.explosion_wands.sharedValues.ExplosionEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class TNTFallingWand {

    public static InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)  {
        ItemStack itemStack = player.getItemInHand(hand);
        if (level instanceof ServerLevel serverLevel) {
            int maxEntities = ExplosionEntities.maxEntities;
            int fuse = ExplosionEntities.fuse;
            int spawnedEntities = ExplosionEntities.spawnedEntities;
            float minExplosion = 1F;
            float maxExplosion = 2F;
            int minSecondFuse = 20;
            int maxSecondFuse = 100;
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
            int randomSecondFuse = minSecondFuse + random.nextInt(maxSecondFuse - minSecondFuse);
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
            PrimedTnt primedTnt1 = new PrimedTnt(EntityType.TNT, level);
            PrimedTnt primedTnt2 = new PrimedTnt(EntityType.TNT, level);
            /*
            EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(Objects.requireNonNull(level.getEntity(0)),
                    playerEyeStart,
                    playerEyeEnd,
                    player.getBoundingBox().expandTowards(dir.scale(reachEntities)).inflate(inflate),
                    Predicate.isEqual(playerLookAngle),
                    0);
             */
            BlockHitResult blockHitResult = level.clip(new ClipContext(
                    playerEyeStart,
                    playerEyeEnd,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    player
            ));
            Vec3 target = blockHitResult.getLocation();
            /*
            if(entityHitResult != null) {
                target = entityHitResult.getEntity().position();
            }
             */
            //Failsafe in-case we spawn more entities than is intended
            if(spawnedEntities <= maxEntities) {
                for (double theta = ExplosionEntities.theta; theta <= lessThanTheta; theta += incrementTheta) {
                    for (double phi = ExplosionEntities.phi; phi <= lessThanPhi; phi += incrementPhi) {
                        //Adds the entities to the world
                        primedTnt1 = new PrimedTnt(EntityType.TNT, level);
                        primedTnt2 = new PrimedTnt(EntityType.TNT, level);
                        //This does not make a perfect circle, but it should not be noticeable
                            if (increment <= randomExplosion) {
                                primedTnt1.setPos(target.x,
                                        target.y + spawnHeight,
                                        target.z
                                );
                                primedTnt1.setFuse(fuse);
                                serverLevel.addFreshEntity(primedTnt1);
                            }
                        if (x != 0 && y != 0 && z != 0) {
                            primedTnt2.setPos(target.x + x,
                                    target.y + y + spawnHeight,
                                    target.z + z
                            );
                            primedTnt2.setFuse(randomSecondFuse);
                            serverLevel.addFreshEntity(primedTnt2);
                            if ((increment % moduloParticle) == moduloRest) {
                                //Particles only spawn 32 blocks away from the player. Might bypass in future
                                serverLevel.sendParticles(ParticleTypes.DRAGON_BREATH, primedTnt2.x, primedTnt2.y, primedTnt2.z, particleThickness, randomDistr, randomDistr, randomDistr, particleSpeed);
                            }
                        } else {
                            primedTnt2.remove();
                        }
                        x = r * Math.sin(theta) * Math.cos(phi);
                        y = r * Math.cos(theta);
                        z = r * Math.sin(theta) * Math.sin(phi);
                        increment++;
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
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
    }
}
