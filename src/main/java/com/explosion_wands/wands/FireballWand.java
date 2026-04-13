package com.explosion_wands.wands;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FireballWand extends Item {
    public FireballWand(Properties properties) {
        super(properties);
    }

    public static Projectile asFireballProjectile(Level level, Player player) {
        float volume = 0.4F;
        float pitch = 1.0F;
        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        int explosionPowerAir = 50;
        //Fireball's velocity
        //Seems to lag noticeably more in this version when a lot of wands have been used a lot, but we still keep it at
        //5, since it should only be noticeable when there's a lot going on, which will make it lag regardless
        //Makes the explosion power slightly higher to compensate
        int velocity = 5;
        double scale = 2.5;
        double addedXDir = 0;
        double addedYDir = player.getEyeHeight() - 0.25;
        double addedZDir = 0;
        double dirX = player.getX();
        double dirY = player.getY();
        double dirZ = player.getZ();
        Vec3 playerLookDir = player.getLookAngle();
        playerLookDir.add(dirX, dirY, dirZ).normalize();
        LargeFireball fireballAir = new LargeFireball(
                level,
                player,
                playerLookDir.x(),
                playerLookDir.y(),
                playerLookDir.z()
        );
        fireballAir.explosionPower = explosionPowerAir;
        if (level instanceof ServerLevel server) {
            if (blockHitResult.getType() != HitResult.Type.BLOCK) {
                Vec3 fireballInAirPosition = player.position().add(addedXDir, addedYDir, addedZDir)
                        .add(playerLookDir.scale(scale));
                //Sets the fireball's position
                fireballAir.moveTo(fireballInAirPosition);
            } else {
                //Does not work if it's at the very corner of a block, but it's more than good enough
                Vec3 fireballInAirPosition = blockHitResult.getLocation();
                //Sets the fireball's position
                fireballAir.moveTo(fireballInAirPosition);
            }
            //Set's the fireball's velocity
            fireballAir.setDeltaMovement(playerLookDir.scale(velocity));
            fireballAir.addTag("fireball");
            //Spawns the fireball
            server.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, volume, pitch);
        }
        return fireballAir;
    }
}
