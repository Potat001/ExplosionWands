package com.explosion_wands.wands;

import com.explosion_wands.tick.TickQueue;
import com.explosion_wands.tick.TickQueueManager;
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
import java.util.*;

public class TNTSlowBarrageWand {
	static int tntAmountPerTick = 4;
	private static final int tntAmount = 70;
	private static final List<Runnable> QUEUE = new ArrayList<>();
	public static void add(Runnable task) {
		QUEUE.add(task);
	}

	public static InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)  {
		ItemStack itemStack = player.getItemInHand(hand);
		if (level instanceof ServerLevel serverLevel && !level.isClientSide()) {
			TickQueue queue = TickQueueManager.createQueue(tntAmount, 4);
			float volume = 0.4F;
			float pitch = 1.0F;
			int reachEntities = 128;
			int reachBlocks = 512;
			int inflate = 100;
			//Since setDefaultGravity is bugged in this version and below,
			//we change the spawn height so most of them explode the blocks under them
			//(if they're falling roughly on the same height as the block we clicked on)
			final double[] spawnHeight = {15};
			final double[] spawnHeightSound = {5};
			double min = 1.0;
			double max = 4.0;
			double[] initialPos = {0};
			int angleValue = 90;
			int defaultValues = 0;
			float explosionPower = 6.0F;
			boolean explodeOnContact = false;
			int particleThickness = 700;
			int particleSpeed = 1;
			int moduloParticle = 6;
			int moduloRest = 1;
			Random random = new Random();
			//Randomized the distribution of particle effects based on the min/max values specified
			double randomDistr = min + random.nextDouble() * (max - min);
			//Makes the start spawn angle of the TNT be equal to the direction the player is facing (default (0): east)
			final double[] angle = {Math.toRadians(player.getYHeadRot() + angleValue)};
			double angleStep = Math.PI / ((double) tntAmount / 2); //How smooth the curve looks
			double amplitude = 15; //Width of the curve
			//Making sure the primed TNTs explode when all the primed TNTs in the current loop has spawned
			int tntFuseTimer = (tntAmount * 50) / (50 * tntAmountPerTick) ; //50 ms = 1 tick
			Vec3 playerEyeStart = player.getEyePosition(0);
			Vec3 playerLookAngle = player.getLookAngle();
			Vec3 playerEyeEnd = playerEyeStart.add(playerLookAngle.scale(reachBlocks));
			Vec3 dir = new Vec3(0, 0, 0);
			/*
			EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(Objects.requireNonNull(level.getEntity(0)),
					playerEyeStart,
					playerEyeEnd,
					player.getBoundingBox().expandTowards(dir.scale(reachEntities)).inflate(inflate),
					Predicate.isEqual(playerLookAngle),
					0);
			 */
			final BlockHitResult blockHitResult = level.clip(new ClipContext(
					playerEyeStart,
					playerEyeEnd,
					ClipContext.Block.COLLIDER,
					ClipContext.Fluid.NONE,
					player
			));
			Vec3 target = blockHitResult.getLocation();
            final double[] changePosition = initialPos; //Initial position of the starting TNT
				for (int i = 0; i < tntAmount; i++) {
						//Fires a TNT at the interval specified in tick()
					int finalI = i;
					//Adds one primed TNT based on the tickCounter
					int finalI1 = i;
					queue.add(() -> {
						//Creates primed TNTs every iteration
						PrimedTnt primedTnt = new PrimedTnt(EntityType.TNT, level);
                        //X dir: cos, Z dir: sin, makes a circle
						primedTnt.setPos(target.x + (Math.cos(angle[defaultValues]) * amplitude),
                                target.y + spawnHeight[defaultValues],
                                target.z + (Math.sin(angle[defaultValues]) * amplitude));
						primedTnt.setFuse(tntFuseTimer);
                        //Performance improvement: Spawns a particle effect on each TNT that satisfy the modulus criteria instead of on each TNT
                        if ((finalI % moduloParticle) == moduloRest) {
                            //Particles only spawn 32 blocks away from the player. Might bypass in future
                            serverLevel.sendParticles(ParticleTypes.DRAGON_BREATH, primedTnt.x, primedTnt.y, primedTnt.z, particleThickness, randomDistr, randomDistr, randomDistr, particleSpeed);
                        }
                        //Changes the initial angle by the value of angleStep every iteration so the TNTs are not frozen
                        angle[defaultValues] += angleStep;
                        //Height of the cos curve every iteration
                        changePosition[defaultValues] += Math.PI / ((double) (tntAmount / 4) / 2);
                        spawnHeight[defaultValues] -= 0.25;
                        //Adds the primed TNT to the world
                        serverLevel.addFreshEntity(primedTnt);
                        //Kind of a hacky way to play a sound only at the very start of the loop
                        if(finalI1 == 0) {
						//Makes the sound play as close to the y direction the player is at
							level.playSound(null,
							target.x,
							//Makes the sound play as close to the y direction the player is at
							target.y + spawnHeightSound[defaultValues],
							target.z,
							SoundEvents.TNT_PRIMED,
							SoundSource.PLAYERS,
							volume, pitch);
							}
						});
                }
		}
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
	}
}
