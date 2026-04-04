package com.explosion_wands.wands;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class FireballHitscanWand extends Item {
    public FireballHitscanWand(Properties properties) {
        super(properties);
    }

    public static Projectile asFireballProjectile(Level level, Player player) {
        float volume = 0.2F;
        float pitch = 1.0F;
        double min = 2.0;
        double max = 10.0;
        RandomSource random = RandomSource.create();
        double randomDistr1 = min + random.nextDouble() * (max - min);
        double randomDistr2 = min + random.nextDouble() * (max - min);
        double randomDistr3 = min + random.nextDouble() * (max - min);
        //Max distance we can click on an entity, set to the maximum render distance where entities can be visible. Possibly a performance boost too
        //Max entity render distance: 128
        int reachEntities = 128;
        //Sets the maximum distance we can click a block on to roughly the length of 32 chunks,
        //with some added leeway
        int reachBlocks = 512;
        int inflate = 100;
        int explosionPowerAir = 10;
        //Think it's fair and more fun to make the explosion power much higher when clicking on entities
        float explosionPowerEntity = 50F;
        float explosionPowerOther = 30F;
        Vector3f particleColor1 = new Vector3f(16711680, 16711680, 16711680);
        Vector3f particleColor2 = new Vector3f(500000, 500000, 500000);
        Vector3f particleColor3 = new Vector3f(3000, 3000, 3000);
        int particleScale = 5;
        int particleThickness = 100;
        int particleSpeed = 2;
        double dirX = player.getX();
        double dirY = player.getY();
        double dirZ = player.getZ();
        Vec3 playerLookDir = player.getLookAngle();
        Vec3 playerStartDir = player.getEyePosition();
        Vec3 playerEndDirEntities = playerStartDir.add(playerLookDir.scale(reachEntities));
        Vec3 playerEndDirBlocks = playerStartDir.add(playerLookDir.scale(reachBlocks));
        playerLookDir.add(dirX, dirY, dirZ).normalize();
        LargeFireball fireballAir = new LargeFireball(
                level,
                player,
                playerLookDir,
                explosionPowerAir);
        //Target entity
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                level,
                fireballAir,
                playerStartDir,
                playerEndDirEntities,
                player.getBoundingBox().expandTowards(playerLookDir.scale(reachEntities)).inflate(inflate),
                //Makes it so that we can hit any type of entity
                entity -> entity instanceof Entity
                //Ensures that we can't hit the hitbox of dead entities
                && entity.isAlive()
                && !entity.isRemoved()
                && entity != player);
        BlockHitResult blockHitResultEntities = level.clip(new ClipContext(
                playerStartDir,
                playerEndDirEntities,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));
        BlockHitResult blockHitResultBlocks = level.clip(new ClipContext(
                playerStartDir,
                playerEndDirBlocks,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));
            double blockDistance = blockHitResultEntities.getLocation().distanceTo(playerStartDir);
            if(entityHitResult != null) {
            double entityDistance = entityHitResult.getLocation().distanceTo(playerStartDir);
                Entity targetEntity = entityHitResult.getEntity();
                    //Ensures that we cannot hit entities through blocks
                    //Also hopeful performance improvements by ensuring that the block distance is less than or equal to the player's reach
                    //...also hopefully no intended consequences of this...
                    if ((blockDistance >= entityDistance && blockHitResultEntities.getType() == HitResult.Type.BLOCK && blockDistance <= reachEntities)
                            || (blockDistance >= entityDistance && blockHitResultEntities.getType() == HitResult.Type.MISS && blockDistance <= reachEntities)) {
                        //Changes the fireball's position to the position of the entity we clicked on
                        Vec3 fireballOnEntityPosition = targetEntity.position();
                        //Teleports the fireball into the entity
                        fireballAir.moveTo(fireballOnEntityPosition);
                        //Filters out entities by if they're living (mobs) or non-living, like
                        //falling blocks and boats
                        if(entityHitResult.getEntity() instanceof LivingEntity) {
                            //Evil fake fireball explosion
                            level.explode(fireballAir, fireballAir.getX(), fireballAir.getY(), fireballAir.getZ(),
                                    explosionPowerEntity, Level.ExplosionInteraction.MOB);
                            level.playSound(null, dirX, dirY, dirZ, SoundEvents.PLAYER_LEVELUP, SoundSource.NEUTRAL, volume, pitch);
                        } else {
                            //Evil fake fireball explosion
                            level.explode(fireballAir, fireballAir.getX(), fireballAir.getY(), fireballAir.getZ(),
                                   explosionPowerOther, Level.ExplosionInteraction.MOB);
                        }
                        if (level instanceof ServerLevel serverLevel) {
                            //Particles spawn up to 32 blocks away from the player
                            //32-bit integer limit: 2147483647
                            serverLevel.sendParticles(new DustParticleOptions(particleColor1, particleScale), targetEntity.getX(), targetEntity.getY(), targetEntity.getZ(), particleThickness, randomDistr1, randomDistr1, randomDistr1, particleSpeed);
                            serverLevel.sendParticles(new DustParticleOptions(particleColor2, particleScale), targetEntity.getX(), targetEntity.getY(), targetEntity.getZ(), particleThickness, randomDistr2, randomDistr2, randomDistr2, particleSpeed);
                            serverLevel.sendParticles(new DustParticleOptions(particleColor3, particleScale), targetEntity.getX(), targetEntity.getY(), targetEntity.getZ(), particleThickness, randomDistr3, randomDistr3, randomDistr3, particleSpeed);
                            //Guarantees that we kill the entity that we clicked on
                            entityHitResult.getEntity().kill();
                        }
                        //Fireball is fake now, discards it when spawned so it doesn't appear after exploding
                        //This also causes the player to not get the return to sender achievement as a side effect
                        fireballAir.discard();
                        fireballAir.addTag("fireball");

                        if(fireballAir.touchingUnloadedChunk()) {
                            fireballAir.discard();
                        }
                        //Since fireball gets removed before it's spawned into the world, we can just return null
                        return null;
                    }
                }
                BlockPos targetBlocks = blockHitResultBlocks.getBlockPos();
                if(level instanceof ServerLevel serverLevel) {
                    serverLevel.explode(fireballAir, targetBlocks.getX(), targetBlocks.getY(), targetBlocks.getZ(),
                    explosionPowerOther, Level.ExplosionInteraction.MOB);
                    serverLevel.sendParticles(new DustParticleOptions(particleColor1, particleScale), targetBlocks.getX(), targetBlocks.getY(), targetBlocks.getZ(), particleThickness, randomDistr1, randomDistr1, randomDistr1, particleSpeed);
                    serverLevel.sendParticles(new DustParticleOptions(particleColor2, particleScale), targetBlocks.getX(), targetBlocks.getY(), targetBlocks.getZ(), particleThickness, randomDistr2, randomDistr2, randomDistr2, particleSpeed);
                    serverLevel.sendParticles(new DustParticleOptions(particleColor3, particleScale), targetBlocks.getX(), targetBlocks.getY(), targetBlocks.getZ(), particleThickness, randomDistr3, randomDistr3, randomDistr3, particleSpeed);
                }
        return null;
    }
}
