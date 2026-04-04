package com.explosion_wands.wands;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FireballShotgunWand extends Item {
    public FireballShotgunWand(Properties properties) {
        super(properties);
    }

    public static Projectile asFireballProjectile(Level level, Player player) {
        if(level instanceof ServerLevel server) {
            BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
            float volume = 0.8F;
            float pitch = 1.0F;
            double incremented = 2;
            double changePos = 0;
            double fireballAmount = 50;
            int explosionPowerAir = 9;
            //fireball's velocity
            double velocity = 3;
            double dirX = player.getX();
            double dirY = player.getY();
            double dirZ = player.getZ();
            double playerStartDirForwardScale = 2.5;
            double directlyUpScale = -0.25;
            double playerStartDirRightBlockHitX = 0;
            double playerStartDirRightBlockHitY = 0.25;
            double playerStartDirRightBlockHitZ = 0;
            Vec3 playerStartDirForward = player.getLookAngle();
            Vec3 playerUpDir = player.getUpVector(1.0F);
            Vec3 directlyUp = new Vec3(1,0,0);
            Vec3 playerStartDir = player.getEyePosition();
            playerStartDirForward.add(dirX, dirY, dirZ).normalize();
            Vec3 playerStartDirRight = playerStartDirForward.cross(playerUpDir).normalize();
            for(int i = 0; i < fireballAmount; i++) {
                double playerStartDirRightScale = incremented * (fireballAmount / 2) - (changePos + incremented / 2);
                LargeFireball fireballAir = new LargeFireball(level, player, playerStartDirForward, explosionPowerAir);
                //Fireball's initial spawn position
                if(blockHitResult.getType() != HitResult.Type.BLOCK) {
                    Vec3 fireballInAirPosition = playerStartDir.add(playerStartDirForward.scale(playerStartDirForwardScale)) //in front
                            //Ensures that the fireballs are evenly distributed in front of the player
                            .add(playerStartDirRight.scale((playerStartDirRightScale))) //left/right
                            .add(directlyUp.scale(directlyUpScale)); //up/down
                    //Sets the fireball's position
                    fireballAir.moveOrInterpolateTo(fireballInAirPosition);
                }
                if(blockHitResult.getType() == HitResult.Type.BLOCK) {
                    Vec3 fireballInAirPosition = blockHitResult.getLocation() //in front
                            //Ensures that the fireballs are evenly distributed in front of the player
                            .add(playerStartDirRight.scale((playerStartDirRightScale))) //left/right
                            .add(playerStartDirRightBlockHitX, playerStartDirRightBlockHitY, playerStartDirRightBlockHitZ); //up/down
                    //Sets the fireball's position
                    fireballAir.moveOrInterpolateTo(fireballInAirPosition);
                }
                //Set's the fireball's velocity
                fireballAir.setDeltaMovement(playerStartDirForward.scale(velocity));
                //Ensures the sound is not played for every single fireball that spawns
                fireballAir.addTag("fireball");
                //Spawns the fireball
                server.addFreshEntity(fireballAir);
                if(fireballAir.touchingUnloadedChunk()) {
                    fireballAir.discard();
                }
                changePos += incremented;
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, volume, pitch);
        }
        return null;
    }
}
