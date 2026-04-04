package com.explosion_wands.wands;

import com.explosion_wands.customFunctions.CustomTnt;
import com.explosion_wands.entity.ModEntities;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class TNTTornadoWand extends Item {
    public TNTTornadoWand(Properties properties) {
        super(properties);
    }

    public static PrimedTnt asPrimedTnt(Level level, Player player) {
        float volume = 0.4F;
        float pitch = 1.0F;
        int min = 20;
        int max = 50;
        RandomSource random = RandomSource.create();
        boolean tornado = true;
        boolean gradualEntitySpawnAfterExplosion = false;
        EntityType<?> entityToSpawn = EntityType.TNT;
        boolean explodeOnContact = true;
        float explosionPower = 0.0F;
        boolean entitySpawnAfterExplosion = true;
        int randomFuse = min + random.nextInt(max - min);
        boolean circle = true;
        int amplitude = 20;
        int entityAmount = 200;
        boolean killEntitiesAfterLoop = false;
        double yIncrement = 0.5;
        int velocity = 4;
        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        double dirX = player.getX();
        double dirY = player.getY();
        double dirZ = player.getZ();
        double scale = 5.0;
        double addedXDir = 0;
        double addedYDir = player.getEyeHeight() - 0.25;
        double addedZDir = 0;
        Vec3 playerLookDir = player.getLookAngle();
        playerLookDir.add(dirX, dirY, dirZ).normalize();
        CustomTnt customTnt = ModEntities.CUSTOM_TNT.create(level, EntitySpawnReason.TRIGGERED);
        if(customTnt != null) {
            if(blockHitResult.getType() != HitResult.Type.BLOCK) {
                Vec3 customTntInAirPosition = player.position().add(addedXDir, addedYDir, addedZDir)
                        .add(playerLookDir.scale(scale));
                customTnt.moveOrInterpolateTo(customTntInAirPosition);
            } else {
                //Does not work if it's at the very corner of a block, but it's more than good enough
                Vec3 customTntInAirPosition = blockHitResult.getLocation();
                customTnt.moveOrInterpolateTo(customTntInAirPosition);
            }
            customTnt.setDeltaMovement(playerLookDir.scale(velocity));
            customTnt.setTornado(tornado);
            customTnt.setGradualEntitySpawnAfterExplosion(gradualEntitySpawnAfterExplosion);
            customTnt.setEntityToSpawn(entityToSpawn);
            customTnt.setFuse(randomFuse);
            customTnt.setExplodeOnContact(explodeOnContact);
            customTnt.setExplosionPower(explosionPower);
            customTnt.setEntitySpawnAfterExplosion(entitySpawnAfterExplosion);
            customTnt.setCircle(circle);
            customTnt.setAmplitude(amplitude);
            customTnt.setEntityAmount(entityAmount);
            customTnt.setKillEntitiesAfterLoop(killEntitiesAfterLoop);
            customTnt.setYIncrement(yIncrement);
            if(customTnt.touchingUnloadedChunk()) {
                customTnt.discard();
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
            SoundEvents.TNT_PRIMED, SoundSource.PLAYERS, volume, pitch);
            return customTnt;
        }
        return null;
    }
}
