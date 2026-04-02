package com.explosion_wands.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(
            method = "dropAllDeathLoot",
            at = @At("HEAD"),
            cancellable = true
    )
    //Makes entities which include this tag not drop items upon death
    private void cancelDrops(ServerLevel level, DamageSource source, CallbackInfo ci) {
        LivingEntity spawnedFromExplosion = (LivingEntity)(Object)this;
        if(spawnedFromExplosion.entityTags().contains("no_drops")) {
            ci.cancel();
        }
    }
}
