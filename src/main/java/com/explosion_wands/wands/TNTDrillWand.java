package com.explosion_wands.wands;

import com.explosion_wands.customFunctions.CustomTnt;
import com.explosion_wands.entity.ModEntities;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class TNTDrillWand extends Item {
    public TNTDrillWand(Properties properties) {
        super(properties);
    }

    public static PrimedTnt asPrimedTnt(Level level, Player player) {
        float volume = 0.4F;
        float pitch = 1.0F;
        int velocity = 4;
        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        double dirX = player.getX();
        double dirY = player.getY();
        double dirZ = player.getZ();
        double scale = 3.0;
        double addedXDir = 0;
        double addedYDir = player.getEyeHeight() - 0.25;
        double addedZDir = 0;
        boolean discardOnFirstUse = false;
        boolean explodeOnContact = true;
        float explosionPower = 10.0F;
        int fuse = 500;
        Vec3 playerLookDir = player.getLookAngle();
        playerLookDir.add(dirX, dirY, dirZ).normalize();
        CustomTnt customTnt = ModEntities.CUSTOM_TNT.create(level, EntitySpawnReason.TRIGGERED);
        if(customTnt != null) {
            if (blockHitResult.getType() == HitResult.Type.BLOCK) {
                Vec3 customTntInAirPosition = player.position().add(addedXDir, addedYDir, addedZDir)
                        .add(playerLookDir.scale(scale));
                customTnt.moveOrInterpolateTo(customTntInAirPosition);
                } else {
                Vec3 customTntInAirPosition = blockHitResult.getLocation();
                customTnt.moveOrInterpolateTo(customTntInAirPosition);
                }
                customTnt.setDeltaMovement(playerLookDir.scale(velocity));
                customTnt.setDiscardOnFirstUse(discardOnFirstUse);
                customTnt.setExplodeOnContact(explodeOnContact);
                customTnt.setExplosionPower(explosionPower);
                customTnt.setFuse(fuse);
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
