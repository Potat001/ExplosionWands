package com.explosion_wands.wands;

import com.explosion_wands.sharedValues.ExplosionEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class FireballScatterWand {

    public static InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (level instanceof ServerLevel serverLevel && !level.isClientSide()) {
            int maxEntities = ExplosionEntities.maxEntities;
            int fuse = ExplosionEntities.fuse;
            int spawnedEntities = ExplosionEntities.spawnedEntities;
            Random random = new Random();

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
            Vec3 dir = new Vec3(0, 0, 0);
            r = 8;
            int spawnHeight;
            spawnHeight = 15;
            float explosionPower = 0F;
            int reachEntities = ExplosionEntities.reachEntities;
            int reachBlock = ExplosionEntities.reachBlock;
            int inflate = ExplosionEntities.inflate;
            Vec3 playerEyeStart = player.getEyePosition(0);
            Vec3 playerLookAngle = player.getLookAngle();
            Vec3 playerEyeEnd = playerEyeStart.add(playerLookAngle.scale(reachBlock));
            LargeFireball fireball = new LargeFireball(
                    level,
                    player,
                    playerLookAngle.x(),
                    playerLookAngle.y(),
                    playerLookAngle.z()
            );
            fireball.explosionPower = fireballExplosionPower;
            BlockHitResult blockHitResult = level.clip(new ClipContext(
                    playerEyeStart,
                    playerEyeEnd,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    player
            ));
            /*
            EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(Objects.requireNonNull(level.getEntity(0)),
                    playerEyeStart,
                    playerEyeEnd,
                    player.getBoundingBox().expandTowards(dir.scale(reachEntities)).inflate(inflate),
                    Predicate.isEqual(playerLookAngle),
                    0);
             */
            Vec3 target = blockHitResult.getLocation();
            /*
            if (entityHitResult != null) {
                target = entityHitResult.getEntity().position();
            }
             */
            //Failsafe in-case we spawn more entities than is intended
            if (spawnedEntities <= maxEntities) {
                for (double theta = ExplosionEntities.theta; theta <= lessThanTheta; theta += incrementTheta) {
                    for (double phi = ExplosionEntities.phi; phi <= lessThanPhi; phi += incrementPhi) {
                        fireball = new LargeFireball(
                                level,
                                player,
                                playerLookAngle.x(),
                                playerLookAngle.y(),
                                playerLookAngle.z()
                        );
                        fireball.explosionPower = fireballExplosionPower;
                        PrimedTnt primedTnt = new PrimedTnt(EntityType.TNT, level);
                        //This does not make a perfect circle, but it should not be noticeable
                        if (increment <= randomExplosion) {
                            primedTnt.setPos(target.x,
                                    target.y + spawnHeight,
                                    target.z
                            );
                            serverLevel.addFreshEntity(primedTnt);
                            primedTnt.setFuse(fuse);
                        }
                        //Creates fireball every iteration
                        //X dir: cos, Z dir: sin, makes a circle
                        if (x != 0 && y != 0 && z != 0) {
                            fireball.setPos(target.x + x,
                                    target.y + spawnHeight,
                                    target.z - z
                            );
                            fireball.addTag("fireball");
                            serverLevel.addFreshEntity(fireball);
                        } else {
                            fireball.remove();
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
        return InteractionResultHolder.success(itemStack);
    }
}
