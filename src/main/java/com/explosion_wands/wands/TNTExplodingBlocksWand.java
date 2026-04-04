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
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class TNTExplodingBlocksWand {

    public static InteractionResult use(Level level, Player player)  {
        if (level instanceof ServerLevel serverLevel && player != null) {
            int maxEntities = ExplosionEntities.maxEntities;
            int fuse = ExplosionEntities.fuse;
            int spawnedEntities = ExplosionEntities.spawnedEntities;
            float minExplosion;
            minExplosion = 1.0F;
            float maxExplosion;
            maxExplosion = 4F;
            int minIncrement = ExplosionEntities.minIncrement;
            int maxIncrement = ExplosionEntities.maxIncrement;
            int minRandomEntities = ExplosionEntities.minRandomEntity;
            int maxRandomEntities = ExplosionEntities.maxRandomEntity;
            double maxRandomPos = ExplosionEntities.maxRandomPos;
            double minRandomPos = ExplosionEntities.minRandomPos;
            RandomSource random = RandomSource.create();
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
            int spawnHeight = ExplosionEntities.spawnHeight;
            int reachEntities = ExplosionEntities.reachEntities;
            int reachBlock = ExplosionEntities.reachBlock;
            int inflate = ExplosionEntities.inflate;
            Vec3 playerEyeStart = player.getEyePosition();
            Vec3 playerLookAngle = player.getLookAngle();
            Vec3 playerEyeEnd = playerEyeStart.add(playerLookAngle.scale(reachBlock));
            CustomTnt customTnt = ModEntities.CUSTOM_TNT.create(level, EntitySpawnReason.TRIGGERED);
            assert customTnt != null;
            EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                    level,
                    customTnt,
                    playerEyeStart,
                    playerEyeEnd,
                    player.getBoundingBox().expandTowards(playerLookAngle.scale(reachEntities)).inflate(inflate),
                    entity -> entity instanceof Entity
                    && entity.isAlive()
                    && !entity.isRemoved()
                    && entity != player,
                    0);
            BlockHitResult blockHitResult = level.clip(new ClipContext(
                    playerEyeStart,
                    playerEyeEnd,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    player
            ));
            /**
             * Offsets the target (most likely a block) we click so that it spawns at a certain
             * y-level above where we clicked (spawnHeight). This prevents the block we clicked
             * on from getting deleted, which means that it will instead spawn inside where
             * the TNTs spawn
             */
            BlockPos target = blockHitResult.getBlockPos().offset(0, spawnHeight, 0);
            if(entityHitResult != null) {
                target = entityHitResult.getEntity().blockPosition().offset(0, spawnHeight, 0);
            }
            BlockState blockToSpawn = Blocks.DIAMOND_BLOCK.defaultBlockState();
            //Purely for debugging purposes
            String blockType = "";
            //Failsafe in-case we spawn more entities than is intended
            if(spawnedEntities <= maxEntities) {
                for (double theta = ExplosionEntities.theta; theta <= lessThanTheta; theta += incrementTheta) {
                    for (double phi = ExplosionEntities.phi; phi <= lessThanPhi; phi += incrementPhi) {
                        if(randomEntity <= spawnedEntities / 8 && spawnedEntities >= 0) {
                            blockToSpawn = Blocks.DIAMOND_BLOCK.defaultBlockState();
                            blockType = blockToSpawn.toString();
                        }
                        if(randomEntity <= (spawnedEntities / 4) && randomEntity > (spawnedEntities / 8)) {
                            blockToSpawn = Blocks.CAKE.defaultBlockState();
                            blockType = blockToSpawn.toString();
                        }
                        if(randomEntity <= (spawnedEntities / 8) * 2 + (spawnedEntities / 8) && randomEntity > (spawnedEntities / 4)) {
                            blockToSpawn = Blocks.AMETHYST_BLOCK.defaultBlockState();
                            blockType = blockToSpawn.toString();
                        }
                        if(randomEntity <= spawnedEntities / 2 && randomEntity > (spawnedEntities / 8) * 2 + (spawnedEntities / 8)) {
                            blockToSpawn = Blocks.LANTERN.defaultBlockState();
                            blockType = blockToSpawn.toString();
                        }
                        if(randomEntity <= (spawnedEntities / 2) + (spawnedEntities / 8) && randomEntity > (spawnedEntities / 2)) {
                            blockToSpawn = Blocks.BEACON.defaultBlockState();
                            blockType = blockToSpawn.toString();
                        }
                        if(randomEntity <= (spawnedEntities / 2) + (spawnedEntities / 4) && randomEntity > (spawnedEntities / 2) + (spawnedEntities / 8)) {
                            blockToSpawn = Blocks.GOLD_BLOCK.defaultBlockState();
                            blockType = blockToSpawn.toString();
                        }
                        if(randomEntity <= spawnedEntities - (spawnedEntities / 8) && randomEntity > (spawnedEntities / 2) + (spawnedEntities / 4)) {
                            blockToSpawn = Blocks.JACK_O_LANTERN.defaultBlockState();
                            blockType = blockToSpawn.toString();
                        }
                        if(randomEntity <= spawnedEntities && randomEntity > spawnedEntities - (spawnedEntities / 8)) {
                            blockToSpawn = Blocks.GLOWSTONE.defaultBlockState();
                            blockType = blockToSpawn.toString();
                        }
                        customTnt = ModEntities.CUSTOM_TNT.create(level, EntitySpawnReason.TRIGGERED);
                        //Adds the entity to the world
                        FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(
                                level,
                                target,
                                blockToSpawn
                        );
                        //Simply: having a lot of dropped items in the world is bad for performance
                        fallingBlockEntity.dropItem = false;
                        //This does not make a perfect circle, but it should not be noticeable
                        if (increment <= randomExplosion && customTnt != null) {
                            customTnt.setPos(target.getX(),
                                    target.getY(),
                                    target.getZ()
                            );
                            serverLevel.addFreshEntity(customTnt);
                            customTnt.setFuse(fuse);
                            customTnt.setExplosionPower(randomIncrement);
                        }
                        if(x != 0 && y != 0 && z != 0) {
                            fallingBlockEntity.setPos(target.getX() + x,
                                    target.getY() + y,
                                    target.getZ() + z
                            );
                        } else {
                            fallingBlockEntity.discard();
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
                            + ",   random increment:   " + 1
            );
             */
            /*
            System.out.println(
                    ",   random entity number:    " + randomEntity
                            + ",   entity type: " + blockType
            );
             */
        }
        return InteractionResult.SUCCESS;
    }
}
