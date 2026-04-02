package com.explosion_wands.mixin;

import net.minecraft.world.entity.projectile.LargeFireball;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LargeFireball.class)

public abstract class LargeFireballMixin {
    //Commented out, since having fireballs make fireblocks makes them way more fun, with performance that's not too noticeable without the fireblocks spawning
    /*
    @Redirect(
            method = "onHit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;explode(" +
                            "Lnet/minecraft/world/entity/Entity;" +
                            "D" +
                            "D" +
                            "D" +
                            "F" +
                            "Z" +
                            "Lnet/minecraft/world/level/Level$ExplosionInteraction;" +
                            ")V"
            )
    )

    private void cancelExplosionFire(Level instance, Entity source, double x, double y, double z, float r, boolean fire, Level.ExplosionInteraction blockInteraction) {
            LargeFireball spawnedFireball = (LargeFireball)(Object)this;
            boolean shouldSpawnFire = spawnedFireball.getTags().contains("fireball");
                instance.explode(source, x, y, z, r, shouldSpawnFire, blockInteraction);
    }
     */
}
