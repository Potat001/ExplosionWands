package com.explosion_wands.wands;

import com.explosion_wands.customFunctions.CustomTnt;
import com.explosion_wands.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class TNTInstantBarrageWand {

    public static InteractionResult use(Level level, Player player)  {
        if (level instanceof ServerLevel serverLevel && player != null && !level.isClientSide()) {
            float volume = 0.4F;
            float pitch = 1.0F;
            int spawnHeight = 30;
            int spawnHeightSound = 5;
            int reachEntities = 128;
            int reachBlocks = 512;
            int inflate = 100;
            int tntAmount = 80;
            //Makes the start spawn angle of the TNT be equal to the direction the player is facing (default (0): east)
            final double[] angle = {Math.toRadians(player.getYRot() + 90)};
            double angleStep = Math.PI / ((double) tntAmount / 2); //How smooth the curve looks
            double amplitude = 15; //Width of the curve
            int initialPos = 0;
            int angleValue = 0;
            int fuse = 200;
            float explosionPower = 10.0F;
            boolean explodeOnContact = true;
            double defaultGravity = 0.04;
            Vec3 playerEyeStart = player.getEyePosition();
            Vec3 playerLookAngle = player.getLookAngle();
            Vec3 playerEyeEnd = playerEyeStart.add(playerLookAngle.scale(reachBlocks));
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
            BlockPos target = blockHitResult.getBlockPos();
            if(entityHitResult != null) {
                target = entityHitResult.getEntity().blockPosition();
            }
            final double[] changePosition = {initialPos}; //Initial position of the starting TNT
            for (int i = 0; i < tntAmount; i++) {
                //Creates primed TNTs every iteration
                customTnt = ModEntities.CUSTOM_TNT.create(level, EntitySpawnReason.TRIGGERED);
                //X dir: cos, Z dir: sin, makes a circle
                if (customTnt != null) {
                    customTnt.setPos(target.getX() + (Math.cos(angle[angleValue]) * amplitude),
                            target.getY() + spawnHeight,
                            target.getZ() + (Math.sin(angle[angleValue]) * amplitude));
                    customTnt.setFuse(fuse);
                    customTnt.setExplosionPower(explosionPower);
                    customTnt.setExplodeOnContact(explodeOnContact);
                    customTnt.setDefaultGravity(defaultGravity);
                    //Adds the primed TNT to the world
                    serverLevel.addFreshEntity(customTnt);
                    if (customTnt.touchingUnloadedChunk()) {
                        customTnt.discard();
                    }
                    //Changes the initial angle by the value of angleStep every iteration so the TNTs are not static
                    angle[angleValue] += angleStep;
                    //Height of the cos curve every iteration
                    changePosition[angleValue] += Math.PI / ((double) (tntAmount / 4) / 2);
                }
            }
            level.playSound(null,
                    target.getX(),
                    target.getY() + spawnHeightSound,
                    target.getZ(),
                    SoundEvents.TNT_PRIMED,
                    SoundSource.PLAYERS,
                    volume,
                    pitch);
        }
        return InteractionResult.SUCCESS;
    }
}
