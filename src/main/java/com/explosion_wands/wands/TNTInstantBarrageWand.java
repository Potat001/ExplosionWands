package com.explosion_wands.wands;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class TNTInstantBarrageWand {

    public static InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)  {
        ItemStack itemStack = player.getItemInHand(hand);
        if (level instanceof ServerLevel serverLevel && !level.isClientSide()) {
            float volume = 0.4F;
            float pitch = 1.0F;
            int spawnHeight = 30;
            double min = 1.0;
            double max = 4.0;
            int spawnHeightSound = 5;
            int reachEntities = 128;
            int reachBlocks = 512;
            int inflate = 100;
            int tntAmount = 80;
            int moduloParticle = 2;
            int moduloRest = 1;
            int particleThickness = 200;
            double particleSpeed = 0.5;
            Random random = new Random();
            //Randomized the distribution of particle effects based on the min/max values specified
            double randomDistr = min + random.nextDouble() * (max - min);
            //Makes the start spawn angle of the TNT be equal to the direction the player is facing (default (0): east)
            final double[] angle = {Math.toRadians(player.getYHeadRot() + 90)};
            double angleStep = Math.PI / ((double) tntAmount / 2); //How smooth the curve looks
            double amplitude = 15; //Width of the curve
            int initialPos = 0;
            int angleValue = 0;
            //So it explodes a bit after they touch the ground for the first
            int fuse = 50;
            float explosionPower = 10.0F;
            boolean explodeOnContact = true;
            Vec3 playerEyeStart = player.getEyePosition(0);
            Vec3 playerLookAngle = player.getLookAngle();
            Vec3 playerEyeEnd = playerEyeStart.add(playerLookAngle.scale(reachBlocks));
            Vec3 dir = new Vec3(0, 0, 0);
            PrimedTnt primedTnt = new PrimedTnt(EntityType.TNT, level);
            /*
            EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(Objects.requireNonNull(level.getEntity(0)),
                    playerEyeStart,
                    playerEyeEnd,
                    player.getBoundingBox().expandTowards(dir.scale(reachEntities)).inflate(inflate),
                    Predicate.isEqual(playerLookAngle),
                    0);
             */
            BlockHitResult blockHitResult = level.clip(new ClipContext(
                    playerEyeStart,
                    playerEyeEnd,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    player
            ));
            Vec3 target = blockHitResult.getLocation();
            /*
            if(entityHitResult != null) {
                target = entityHitResult.getEntity().position();
            }
             */
            final double[] changePosition = {initialPos}; //Initial position of the starting TNT
            for (int i = 0; i < tntAmount; i++) {
                //Creates primed TNTs every iteration
                primedTnt = new PrimedTnt(EntityType.TNT, level);
                //X dir: cos, Z dir: sin, makes a circle
                primedTnt.setPos(target.x + (Math.cos(angle[angleValue]) * amplitude),
                        target.y + spawnHeight,
                        target.z + (Math.sin(angle[angleValue]) * amplitude));
                primedTnt.setFuse(fuse);
                if ((i % moduloParticle) == moduloRest) {
                    //Particles only spawn 32 blocks away from the player. Might bypass in future
                    serverLevel.sendParticles(ParticleTypes.DRAGON_BREATH, primedTnt.x, primedTnt.y, primedTnt.z, particleThickness, randomDistr, randomDistr, randomDistr, particleSpeed);
                }
                //Adds the primed TNT to the world
                serverLevel.addFreshEntity(primedTnt);
                //Changes the initial angle by the value of angleStep every iteration so the TNTs are not static
                angle[angleValue] += angleStep;
                //Height of the cos curve every iteration
                changePosition[angleValue] += Math.PI / ((double) (tntAmount / 4) / 2);
            }
            level.playSound(null,
                    target.x,
                    target.y + spawnHeightSound,
                    target.z,
                    SoundEvents.TNT_PRIMED,
                    SoundSource.PLAYERS,
                    volume,
                    pitch);
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
    }
}
