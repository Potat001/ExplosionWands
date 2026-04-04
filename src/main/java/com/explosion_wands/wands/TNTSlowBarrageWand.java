package com.explosion_wands.wands;

import com.explosion_wands.customFunctions.CustomTnt;
import com.explosion_wands.entity.ModEntities;
import com.explosion_wands.tick.TickQueue;
import com.explosion_wands.tick.TickQueueManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
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

import java.util.*;

public class TNTSlowBarrageWand {
	static int tntAmountPerTick = 4;
	private static final int tntAmount = 100;
	private static final List<Runnable> QUEUE = new ArrayList<>();
	public static void add(Runnable task) {
		QUEUE.add(task);
	}

	public static InteractionResult use(Level level, Player player)  {
		if (level instanceof ServerLevel serverLevel && player != null && !level.isClientSide()) {
			TickQueue queue = TickQueueManager.createQueue(tntAmount, 4);
			float volume = 0.4F;
			float pitch = 1.0F;
			int reachEntities = 128;
			int reachBlocks = 512;
			int inflate = 100;
			final double[] spawnHeight = {30};
			final double[] spawnHeightSound = {5};
			double min = 1.0;
			double max = 4.0;
			double[] initialPos = {0};
			int angleValue = 90;
			int defaultValues = 0;
			float explosionPower = 6.0F;
			boolean explodeOnContact = false;
			double defaultGravity = 0.15;
			int particleThickness = 700;
			int particleSpeed = 1;
			int moduloParticle = 6;
			int moduloRest = 1;
			RandomSource random = RandomSource.create();
			//Randomized the distribution of particle effects based on the min/max values specified
			double randomDistr = min + random.nextDouble() * (max - min);
			//Makes the start spawn angle of the TNT be equal to the direction the player is facing (default (0): east)
			final double[] angle = {Math.toRadians(player.getYRot() + angleValue)};
			double angleStep = Math.PI / ((double) tntAmount / 2); //How smooth the curve looks
			double amplitude = 15; //Width of the curve
			//Making sure the primed TNTs explode when all the primed TNTs in the current loop has spawned
			int tntFuseTimer = (tntAmount * 50) / (50 * tntAmountPerTick) ; //50 ms = 1 tick
			Vec3 playerEyeStart = player.getEyePosition();
			Vec3 playerLookAngle = player.getLookAngle();
			Vec3 playerEyeEnd = playerEyeStart.add(playerLookAngle.scale(reachBlocks));
			//Makes a duplicate, unused CustomTnt so we're able to get entityHitResult working without
			//potentially having to rewrite much of the code
			CustomTnt customTnt1 = ModEntities.CUSTOM_TNT.create(level, EntitySpawnReason.TRIGGERED);
			assert customTnt1 != null;
			EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
					level,
					customTnt1,
					playerEyeStart,
					playerEyeEnd,
					player.getBoundingBox().expandTowards(playerLookAngle.scale(reachEntities)).inflate(inflate),
					entity -> entity instanceof Entity
							&& entity.isAlive()
							&& !entity.isRemoved()
							&& entity != player,
					0);
			final BlockHitResult blockHitResult = level.clip(new ClipContext(
					playerEyeStart,
					playerEyeEnd,
					ClipContext.Block.COLLIDER,
					ClipContext.Fluid.NONE,
					player
			));
			BlockPos target;
			if(entityHitResult != null) {
				target = entityHitResult.getEntity().blockPosition();
			} else {
				target = blockHitResult.getBlockPos();
			}
			final double[] changePosition = initialPos; //Initial position of the starting TNT
			for (int i = 0; i < tntAmount; i++) {
				//Fires a TNT at the interval specified in tick()
				int finalI = i;
				//Adds one primed TNT based on the tickCounter
				int finalI1 = i;
				queue.add(() -> {
					//Creates primed TNTs every iteration
					CustomTnt customTnt = ModEntities.CUSTOM_TNT.create(level, EntitySpawnReason.TRIGGERED);
					if(customTnt != null) {
						//X dir: cos, Z dir: sin, makes a circle
						customTnt.setPos(target.getX() + (Math.cos(angle[defaultValues]) * amplitude),
								target.getY() + spawnHeight[defaultValues],
								target.getZ() + (Math.sin(angle[defaultValues]) * amplitude));
						customTnt.setFuse(tntFuseTimer);
						//Performance improvement: Spawns a particle effect on each TNT that satisfy the modulus criteria instead of on each TNT
						if ((finalI % moduloParticle) == moduloRest) {
							//Particles only spawn 32 blocks away from the player. Might bypass in future
							serverLevel.sendParticles(ParticleTypes.COPPER_FIRE_FLAME, customTnt.getX(), customTnt.getY(), customTnt.getZ(), particleThickness, randomDistr, randomDistr, randomDistr, particleSpeed);
						}
						customTnt.setExplosionPower(explosionPower);
						customTnt.setExplodeOnContact(explodeOnContact);
						customTnt.setDefaultGravity(defaultGravity);
						//Changes the initial angle by the value of angleStep every iteration so the TNTs are not frozen
						angle[defaultValues] += angleStep;
						//Height of the cos curve every iteration
						changePosition[defaultValues] += Math.PI / ((double) (tntAmount / 4) / 2);
						spawnHeight[defaultValues] -= 0.25;
						//Adds the primed TNT to the world
						serverLevel.addFreshEntity(customTnt);
						if(customTnt.touchingUnloadedChunk()) {
							customTnt.discard();
						}
						//Kind of a hacky way to play a sound only at the very start of the loop
						if(finalI1 == 0) {
							//Makes the sound play as close to the y direction the player is at
							level.playSound(null,
									target.getX(),
									//Makes the sound play as close to the y direction the player is at
									target.getY() + spawnHeightSound[defaultValues],
									target.getZ(),
									SoundEvents.TNT_PRIMED,
									SoundSource.PLAYERS,
									volume, pitch);
						}
					}
				});
			}
		}
		return InteractionResult.SUCCESS;
	}
}
